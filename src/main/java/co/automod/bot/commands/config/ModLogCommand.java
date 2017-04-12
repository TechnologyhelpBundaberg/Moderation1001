package co.automod.bot.commands.config;

import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

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

    }
}
