package co.automod.bot.commands.admin;

import co.automod.bot.core.listener.command.Command;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

public abstract class ModAction extends Command {


    abstract Permission getRequiredPermission();

    abstract boolean doModAction(Guild guild, Member member);

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        if (getRequiredPermission() != null) {
            if (!PermissionUtil.checkPermission(guild, guild.getMember(invoker), getRequiredPermission())) {
                channel.sendMessage(String.format("You have no permission to %s. You need the %s permission node", getTrigger(), getRequiredPermission())).queue();
                return;
            }
            if (!PermissionUtil.checkPermission(guild, guild.getSelfMember(), getRequiredPermission())) {
                channel.sendMessage("I have no permission to %s users. I need the %s permission", getTrigger(), getRequiredPermission()).queue();
                return;
            }
        }
        if (args.isEmpty()) {
            channel.sendMessage(String.format("Who exactly should I %s?", getTrigger())).queue();
            return;
        }
        User targetUser = Misc.findUser(channel, args);
        if (targetUser == null) {
            channel.sendMessage(String.format("Can't find any user matching %s", args)).queue();
            return;
        }
        if (targetUser.getIdLong() == guild.getJDA().getSelfUser().getIdLong()) {
            channel.sendMessage(String.format("I'm not going to %s myself :D", getTrigger())).queue();
            return;
        }
        if (!PermissionUtil.canInteract(guild.getSelfMember(), guild.getMember(targetUser))) {
            channel.sendMessage("I can't %s the user %s", getTrigger(), targetUser.getName()).queue();
            return;
        }
        if (doModAction(guild, guild.getMember(targetUser))) {
            channel.sendMessage(String.format("%s recieved a %s!", targetUser.getName(), getTrigger())).queue();
            return;
        }
        channel.sendMessage("Failed to %s %s", getTrigger(), targetUser.getName());
    }
}
