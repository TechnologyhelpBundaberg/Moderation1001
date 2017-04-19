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
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        if (getRequiredPermission() != null) {
            if (!PermissionUtil.checkPermission(guild, guild.getMember(invoker), getRequiredPermission())) {
                channel.sendMessage(String.format("\u274C| It seems like you don't have the `%s` permission! **Make sure that you have the `%s` permission.**", getTrigger(), getRequiredPermission())).queue();
                return;
            }
            if (!PermissionUtil.checkPermission(guild, guild.getSelfMember(), getRequiredPermission())) {
                channel.sendMessage("\u274C| **I am not allowed** to %s members! Make sure that I have the **`%s`** permission.", getTrigger(), getRequiredPermission()).queue();
                return;
            }
        }
        if (args.isEmpty()) {
            channel.sendMessage(String.format("\u2753| **Who exactly should I %s?**", getTrigger())).queue();
            return;
        }
        User targetUser = Misc.findUser(channel, args);
        if (targetUser == null) {
            channel.sendMessage(String.format("Can't find **%s**! I guess he's pretty good at hide & seek.. \uD83D\uDC40", args)).queue();
            return;
        }
        if (targetUser.getIdLong() == guild.getJDA().getSelfUser().getIdLong()) {
            channel.sendMessage(String.format("I'm not going to %s myself..!", getTrigger())).queue();
            return;
        }
        if (targetUser.getIdLong() == invoker.getIdLong()) {
            channel.sendMessage(String.format("Don't be so hard on yourself! \uD83D\uDC96 \u2728 ", getTrigger())).queue();
            return;
        }
        if (!PermissionUtil.canInteract(guild.getSelfMember(), guild.getMember(targetUser))) {
            channel.sendMessage("I can't %s the user %s!", getTrigger(), targetUser.getName()).queue();
            return;
        }
        if (doModAction(guild, guild.getMember(targetUser))) {
            channel.sendMessage(String.format("%s| **%s** is gone!", getTrigger().equals("kick") ? "\uD83D\uDC62" : "\uD83D\uDD28", targetUser.getName() + "#" + targetUser.getDiscriminator())).queue();
            return;
        }
        channel.sendMessage("Failed to %s %s. Sad isn't it?", getTrigger(), targetUser.getName());
    }
}
