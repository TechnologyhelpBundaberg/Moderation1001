package co.automod.bot.commands.config;

import co.automod.bot.core.Shard;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.data.GuildChannel;
import co.automod.bot.data.GuildPrefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class ServerPrefixCommand extends Command {
    @Override
    public String getTrigger() {
        return "serverprefix";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sp"};
    }

    @Override
    public String getDescription() {
        return "Change the bots prefix in your server";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        if (args.isEmpty()) {
            channel.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(String.format("**\u274C Missing args**\n\n%sserverprefix [Prefix]", Shard.getPrefix(guild))).build()).queue();
            return;
        }
        String prefix = args.replace(" ", "");
        r.table("prefixes").insert(new GuildPrefix(guild.getId(), prefix)).optArg("conflict", "replace").runNoReply(conn);
        channel.sendMessage("\u2705 **Set the prefix to `" + prefix + "`**").queue();
    }
}
