package co.automod.bot.commands.config;

import co.automod.bot.core.Settings;
import co.automod.bot.core.listener.CommandListener;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.settings.PrefixSetting;
import co.automod.bot.util.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

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
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        if (args.isEmpty()) {
            channel.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(String.format("**\u274C Missing args**\n\n%sserverprefix [Prefix]", CommandListener.getPrefix(guild))).build()).queue();
            return;
        }
        String prefix = args.replace(" ", "");
        if (Settings.update(guild, PrefixSetting.class, prefix)) {
            channel.sendMessage(Emoji.OK + " **Set the prefix to `" + prefix + "`**").queue();
        } else {
            channel.sendMessage(Emoji.X + " **Failed to update prefix!**").queue();
        }
    }
}
