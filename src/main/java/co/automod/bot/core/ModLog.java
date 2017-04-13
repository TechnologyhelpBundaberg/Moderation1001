package co.automod.bot.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.rethinkdb.gen.exc.ReqlNonExistenceError;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class ModLog {
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final Cache<String, Optional<Message>> MessageCache = CacheBuilder.newBuilder().concurrencyLevel(10).maximumSize(2500).build();
    private final Cache<String, Optional<String>> SelfCache = CacheBuilder.newBuilder().concurrencyLevel(10).maximumSize(2500).build();

    @SubscribeEvent
    public void onMessage(GuildMessageReceivedEvent e) {
        Message msg = e.getMessage();
        if (msg != null) {
            MessageCache.put(msg.getId(), Optional.of(msg));
        }
    }

    private String getChannel(Guild guild) {
        try {
            return r.table("modlog").get(guild.getId()).getField("channelId").run(conn);

        } catch (ReqlNonExistenceError ignored) {
            return null;
        }
    }

    private void deleteGuild(Guild guild) {
        r.table("modlog").get(guild.getId()).delete().runNoReply(conn);
    }

    private String getTime() {
        return dateFormat.format(new Date());
    }

    private String getUser(Member member) {
        return member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
    }

    private Message getFromCache(String id) {
        try {
            return MessageCache.get(id, Optional::empty).orElse(null);
        } catch (ExecutionException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    @SubscribeEvent
    public void onDelete(GuildMessageDeleteEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        try {
            String SelfMessage = SelfCache.get(e.getMessageId(), Optional::empty).orElse(null);
            if (SelfMessage != null) {
                channel.sendMessage(SelfMessage).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
                return;
            }
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        Message DeletedMessage;
        DeletedMessage = getFromCache(e.getMessageId());
        if (DeletedMessage == null) return;
        String content = DeletedMessage.getStrippedContent();
        if (!content.isEmpty() && !DeletedMessage.getAttachments().isEmpty()) {
            content += String.join(", ", DeletedMessage.getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.toList()));
        } else if (content.isEmpty()) {
            if (DeletedMessage.getAttachments().isEmpty()) return;
            content = String.join(", ", DeletedMessage.getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.toList()));
        }
        String time = getTime();
        Member author = DeletedMessage.getGuild().getMember(DeletedMessage.getAuthor());
        String user = getUser(author);
        channel.sendMessage(String.format("\uD83D\uDCDD `[%s]` %s **%s's** message has been deleted `%s`", time, e.getChannel().getAsMention(), user, content)).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }

    @SubscribeEvent
    public void onEdit(GuildMessageUpdateEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        Message after = e.getMessage();
        if (after == null) return;
        Message before = getFromCache(after.getId());
        if (before == null) return;
        String time = getTime();
        Member author = before.getGuild().getMember(before.getAuthor());
        String user = getUser(author);
        channel.sendMessage(String.format("\uD83D\uDCDD `[%s]` %s **%s's** message has been edited\n\nBefore: `%s`\n\nAfter: `%s`", time, e.getChannel().getAsMention(), user, before.getStrippedContent(), after.getStrippedContent())).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));

    }

    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        Member author = e.getMember();
        String user = getUser(author);
        channel.sendMessage(String.format("\u274C `[%s]` **%s** has left the server or was kicked. Total members `%s`", time, user, e.getGuild().getMembers().size())).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }

    @SubscribeEvent
    public void onJoin(GuildMemberJoinEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        Member author = e.getMember();
        String user = getUser(author);
        long days = Duration.between(author.getUser().getCreationTime(), OffsetDateTime.now()).toDays();
        String created = days > 10 ? String.format("**Created %s days ago.**", days) : String.format("\u26A0 **New User - joined %s days ago.**", days);
        channel.sendMessage(String.format("\u2705 `[%s]` **%s** joined the server. %s Total members `%s`", time, user, created, e.getGuild().getMembers().size())).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }

    @SubscribeEvent
    public void onBan(GuildBanEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        User author = e.getUser();
        String user = author.getName() + "#" + author.getDiscriminator();
        channel.sendMessage(String.format("\uD83D\uDD28 `[%s]` **%s**(%s) has been banned. Total members `%s`", time, user, author.getId(), e.getGuild().getMembers().size())).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));

    }

    @SubscribeEvent
    public void onNickChange(GuildMemberNickChangeEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        Member author = e.getMember();
        String user = getUser(author);
        channel.sendMessage(String.format("\u2705 `[%s]` **%s** Changed his name\n\nBefore: `%s`\n\nAfter: `%s`", time, user, e.getPrevNick() == null ? e.getMember().getUser().getName() : e.getPrevNick(), e.getNewNick() == null ? e.getMember().getUser().getName() : e.getNewNick())).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }

    @SubscribeEvent
    public void roleAdd(GuildMemberRoleAddEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        Member author = e.getMember();
        String user = getUser(author);
        String AddedRoles = String.join(", ", e.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        channel.sendMessage(String.format("\u2611 `[%s]` a role has been added to **%s** - `%s`", time, user, AddedRoles)).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }

    @SubscribeEvent
    public void roleRemove(GuildMemberRoleRemoveEvent e) {
        String tempChannel = getChannel(e.getGuild());
        if (tempChannel == null) return;
        TextChannel channel = e.getJDA().getTextChannelById(tempChannel);
        if (channel == null) {
            System.out.println("Could not find channel for id " + tempChannel);
            deleteGuild(e.getGuild());
            return;
        }
        if (!e.getGuild().getId().equals(channel.getGuild().getId())) return;
        String time = getTime();
        Member author = e.getMember();
        String user = getUser(author);
        String AddedRoles = String.join(", ", e.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        channel.sendMessage(String.format("\u274C `[%s]` a role has been removed from **%s** - `%s`", time, user, AddedRoles)).queue(msg -> SelfCache.put(msg.getId(), Optional.of(msg.getRawContent())));
    }


}
