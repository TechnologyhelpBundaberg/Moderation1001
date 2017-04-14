package co.automod.bot.core;

import com.rethinkdb.gen.exc.ReqlNonExistenceError;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.automod.bot.core.Rethink.connection;
import static co.automod.bot.core.Rethink.r;

public class Automod {
    private static final Pattern discordURL = Pattern.compile("discord(?:(\\.(?:me|io|gg)|sites\\.com)\\/.{0,4}|app\\.com.{1,4}(?:invite|oauth2).{0,5}\\/)\\w+");
    private final Permission[] ignoredPerms = {Permission.MANAGE_SERVER, Permission.MANAGE_ROLES, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS};

    private Boolean enabled(Guild guild) {
        try {
            return r.table("antilink").get(guild.getId()).getField("bool").run(connection);
        } catch (ReqlNonExistenceError ignored) {
        }
        return false;
    }

    private String cleanString(String input) {
        input = input.replaceAll("\\p{C}", "");
        input = input.replace(" ", "");
        return input;
    }

    private boolean ignoreMember(Member member) {
        if (member.getUser().getIdLong() == 97433066384928768L) {
            return false;
        }
        return Arrays.stream(ignoredPerms).anyMatch(perm -> PermissionUtil.checkPermission(member.getGuild(), member, perm));
    }

    private void handleMessage(Message message, Member member) {
        String content = message.getRawContent();
        if (!content.contains("discord")) return;
        if (message.getAuthor().getId().equals(message.getJDA().getSelfUser().getId())) return;
        if (!enabled(message.getGuild())) return;
        if (ignoreMember(member)) return;
        String cleanContent = cleanString(content);
        Matcher m = discordURL.matcher(cleanContent);
        if (m.find()) {
            message.delete().queue(a -> message.getChannel().sendMessage(String.format("%s \u26D4 **Advertising is not allowed!**", message.getAuthor().getAsMention())).queue());
        }
    }

    @SubscribeEvent
    public void handleEdit(GuildMessageUpdateEvent e) {
        handleMessage(e.getMessage(), e.getMember());
    }

    @SubscribeEvent
    public void handleMessage(GuildMessageReceivedEvent e) {
        handleMessage(e.getMessage(), e.getMember());
    }
}
