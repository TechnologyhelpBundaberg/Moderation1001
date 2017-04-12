package co.automod.bot.commands.config;

import co.automod.bot.core.listener.command.Command;
import co.automod.bot.data.GuildChannel;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.List;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class ModLogCommand extends Command {
    @Override
    public String getTrigger() {
        return "modlog";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Enable modlog and set the channel.";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        List<TextChannel> ments = message.getMentionedChannels();
        if (ments.isEmpty()) {
            channel.sendMessage("\u274C **You must mention a channel you want the modlog to be in**").queue();
            return;
        }
        TextChannel ch = ments.get(0);
        r.table("modlog").insert(new GuildChannel(guild.getId(), ch.getId())).optArg("conflict", "replace")
                .runNoReply(conn);
        channel.sendMessage("\u2705 **Set " + ch.getAsMention() + " as the modlog channel**").queue();
    }
}
