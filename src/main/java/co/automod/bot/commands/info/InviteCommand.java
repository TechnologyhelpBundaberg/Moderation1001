package co.automod.bot.commands.info;

import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class InviteCommand extends Command {
    private static final String embedDescription = "[Add me](https://discord.io/protector)\n[Official Discord](https://discord.io/botz)\n[Github](https://github.com/repulser/automod)\n";

    @Override
    public String getTrigger() {
        return "invite";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Links!";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        channel.sendMessage(new EmbedBuilder()
                .setDescription(embedDescription).build()).queue();

    }
}
