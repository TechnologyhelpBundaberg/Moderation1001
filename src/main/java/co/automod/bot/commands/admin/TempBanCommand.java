package co.automod.bot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class TempBanCommand extends ModAction {
    @Override
    Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }

    @Override
    boolean doModAction(Guild guild, Member member) {
        guild.getController().ban(member, 7).complete();
        guild.getController().unban(member.getUser()).complete();
        return true;
    }

    @Override
    public String getTrigger() {
        return "tempban";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Kicks a user and clears the history";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "tempban @mention", "kicks a user based on mention",
                "tempban name", "kicks a user based on name"
        };
    }
}
