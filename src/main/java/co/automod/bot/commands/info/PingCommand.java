package co.automod.bot.commands.info;

import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

public class PingCommand extends Command {
    @Override
    public String getTrigger() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Check the bots response time";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        long startTime = System.currentTimeMillis();
        channel.sendTyping().complete();
        long stopTime = System.currentTimeMillis();
        long ms = stopTime - startTime;
        channel.sendMessage(new EmbedBuilder()
                .setDescription("**Pong.**\nTime: " + String.valueOf(ms) + "ms")
                .setColor(Color.green).build()).queue();

    }
}
