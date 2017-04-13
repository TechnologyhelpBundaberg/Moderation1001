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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class AntiLink {
    private static final Pattern discordURL = Pattern.compile("discord\\.(?:me|io|gg)\\/.{0,4}\\w+|discordapp\\.com.{1,4}(?:invite|oauth2).{0,5}\\/");
    private final Permission[] ignoredPerms = {Permission.MANAGE_SERVER, Permission.MANAGE_ROLES};

    private Boolean enabled(Guild guild) {
        try {
            return r.table("antilink").get(guild.getId()).getField("bool").run(conn);
        } catch (ReqlNonExistenceError ignored) {
        }
        return false;
    }

    private String cleanString(String input) {
        input = input.replaceAll("\\p{C}", "");
        input = input.replace(" ", "");
        return input;
    }

    private Boolean hasPerms(Member member) {
        return PermissionUtil.checkPermission(member.getGuild(), member, ignoredPerms);
    }

    private void handleMessage(Message message, Member member) {
        String content = message.getRawContent();
        if (!content.contains("discord")) return;
        if (message.getAuthor().getId().equals(message.getJDA().getSelfUser().getId())) return;
        if (!enabled(message.getGuild())) return;
        if (hasPerms(member)) return;
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
