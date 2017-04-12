package co.automod.bot.commands.info;

import co.automod.bot.core.listener.CommandListener;
import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.Map;

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
        return "Command list";
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
        StringBuilder desc = new StringBuilder();
        for (Map.Entry<String, Command> entry : CommandListener.commands.entrySet()) {

            String key = entry.getKey();
            Command value = entry.getValue();
            if (!value.isListed()) continue;
            desc.append(String.format("[!%s](https://github.com/repulser/automod) - %s\n\n", key, value.getDescription()));
        }
        final String newDescription = desc.toString();
        invoker.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(new EmbedBuilder()
                .setAuthor("AutoMod Commands!", null, channel.getJDA().getSelfUser().getAvatarUrl())
                .setDescription(newDescription)
                .setFooter("AutoMod ready at your command!", null)
                .build()).queue());
    }
}
