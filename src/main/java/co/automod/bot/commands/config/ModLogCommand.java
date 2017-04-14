package co.automod.bot.commands.config;

import co.automod.bot.core.Settings;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.settings.ModLogChannelSetting;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.List;


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
        return "Configure the Mod Log which logs deleted messages, edited messages and much more";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "modlog #channel", "Sets the channel the Mod Log will go to",
                "modlog off", "disables the Mod Log"
        };
    }

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        if (args.isEmpty()) {
            if (Settings.getSetting(guild).modlog.isEmpty()) {
                channel.sendMessage("\u274C **Mod Log is not set**").queue();
            } else {
                channel.sendMessage("\u2705 ** The modlog is set to " + guild.getTextChannelById(Settings.getSetting(guild).modlog).getAsMention() + " .**").queue();
            }
            return;
        }
        if (Misc.isFuzzyFalse(args)) {
            Settings.update(guild, ModLogChannelSetting.class, "");
            channel.sendMessage("\u2705 **Disabled modlog**").queue();
            return;
        }
        List<TextChannel> ments = message.getMentionedChannels();
        if (!Settings.update(guild, ModLogChannelSetting.class, args)) {
            channel.sendMessage("\u274C **You must mention a channel you want the modlog to be in**").queue();
            return;
        }
        TextChannel ch = ments.get(0);
        channel.sendMessage("\u2705 **Set " + ch.getAsMention() + " as the modlog channel**").queue();
    }
}
