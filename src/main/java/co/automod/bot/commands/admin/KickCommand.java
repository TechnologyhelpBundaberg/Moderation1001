package co.automod.bot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class KickCommand extends ModAction {
    @Override
    Permission getRequiredPermission() {
        return Permission.KICK_MEMBERS;
    }

    @Override
    boolean doModAction(Guild guild, Member member) {
        guild.getController().kick(member).complete();
        return true;
    }

    @Override
    public String getTrigger() {
        return "kick";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Kicks a user";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "kick @mention", "kicks a user based on mention",
                "kick name", "kicks based on name"
        };
    }
}
