package co.automod.bot.commands.info;

import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class HelpCommand extends Command {
    @Override
    public String getTrigger() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"?", "helpme", "commands"};
    }

    @Override
    public String getDescription() {
        return "use me if you need help";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "help", "full overview of all the commands",
                "help <command>", "Info about a specific command"
        };
    }

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        channel.sendMessage("This should probably be implemented :)").queue();
    }
}
