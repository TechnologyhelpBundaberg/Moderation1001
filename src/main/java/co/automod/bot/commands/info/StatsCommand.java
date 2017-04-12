package co.automod.bot.commands.info;

import co.automod.bot.Main;
import co.automod.bot.core.ShardContainer;
import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.Arrays;

public class StatsCommand extends Command {
    @Override
    public String getTrigger() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Statistics";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        String MemberCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getUsers().size()).sum());
        String serversCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getGuilds().size()).sum());
        String channelCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getTextChannels().size()).sum());
        int Threads = Thread.getAllStackTraces().keySet().size();
        channel.sendMessage(new EmbedBuilder()
                .setAuthor("ModBot Stats", null, channel.getJDA().getSelfUser().getAvatarUrl())
                .addField("Server Count", serversCount, true)
                .addField("Total Members", MemberCount, true)
                .addField("Text Channels", channelCount, true)
                .addField("Threads", String.valueOf(Threads), true)
                .setThumbnail(channel.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.green)
                .build()).queue();
    }

}
