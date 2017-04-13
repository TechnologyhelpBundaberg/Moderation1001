package co.automod.bot.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class BanCommand extends ModAction {
    @Override
    Permission getRequiredPermission() {
        return Permission.KICK_MEMBERS;
    }

    @Override
    boolean doModAction(Guild guild, Member member) {
        guild.getController().ban(member, 7).complete();
        return true;
    }

    @Override
    public String getTrigger() {
        return "ban";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Bans a user";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "ban @mention", "bans a user based on mention",
                "ban name", "bans a user based on name"
        };
    }
}
