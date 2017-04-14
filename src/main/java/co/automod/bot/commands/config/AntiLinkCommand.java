package co.automod.bot.commands.config;

import co.automod.bot.core.GuildConfiguration;
import co.automod.bot.core.Settings;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

public class AntiLinkCommand extends Command {
    @Override
    public String getTrigger() {
        return "antilink";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"al"};
    }

    @Override
    public String getDescription() {
        return "Toggle Anti Link setting, deletes invite links to avoid advertising.";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "antilink", "toggles Anti Link",
                "antilink on/off", "turns Anti Link on or off"
        };
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        GuildConfiguration setting = Settings.getSetting(guild);

        if (!hasPerms) return;
        Boolean needsUpdating = true;
        boolean enabled;
        if (Misc.isFuzzyTrue(args) || Misc.isFuzzyFalse(args)) {
            needsUpdating = false;
        }
        if (!needsUpdating) {

        }
        Settings.update(guild, "antilink", String.valueOf(needsUpdating));
        if (needsUpdating) {
            channel.sendMessage("\u2705 **Anti Link has been enabled!**").queue();
        } else {
            channel.sendMessage("\u274C **Anti Link has been disabled**").queue();
        }
    }
}
