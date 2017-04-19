package co.automod.bot.commands.useful;

import co.automod.bot.core.listener.command.Command;
import com.sun.security.auth.callback.TextCallbackHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import sun.security.ec.SunEC;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ServerInfoCommand extends Command {
    @Override
    public String getTrigger() {
        return "serverinfo";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sinfo", "si"};
    }

    @Override
    public String getDescription() {
        return "Server information";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }

    private boolean isOnline(Member member) {
        return member.getOnlineStatus().equals(OnlineStatus.ONLINE) || member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB) || member.getOnlineStatus().equals(OnlineStatus.IDLE);
    }

    private boolean isBot(Member member) {
        return member.getUser().isBot();
    }

    private boolean cantSee(Member member, TextChannel channel) {
        return !channel.getMembers().contains(member);
    }

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        Member guildOwner = guild.getOwner();
        String user = member.getEffectiveName() + "#" + invoker.getDiscriminator();
        String owner = guildOwner.getEffectiveName() + "#" + guildOwner.getUser().getDiscriminator();
        String region = guild.getRegion().getName();
        List<TextChannel> channelList = guild.getTextChannels();
        String textChannels = String.valueOf(channelList.size());
        String hiddenChannels = String.valueOf(channelList.stream().filter(channel1 -> cantSee(member, channel1)).count());
        String voiceChannels = String.valueOf(guild.getVoiceChannels().size());
        String roles = String.valueOf(guild.getRoles().size());
        String highestRole = guild.getRoles().get(0).getName();
        String verificationLevel = guild.getVerificationLevel().name();
        String id = guild.getId();
        List<Member> memberList = guild.getMembers();
        String users = String.valueOf(memberList.stream().filter(this::isOnline).count()) + "/" + String.valueOf(memberList.size());
        List<Member> botList = memberList.stream().filter(this::isBot).collect(Collectors.toList());
        String bots = String.valueOf(botList.stream().filter(this::isOnline).count() + "/" + botList.size());
        OffsetDateTime creationTime = guild.getCreationTime();
        String month = creationTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
        String year = String.valueOf(creationTime.getYear());
        String timeString = String.valueOf(creationTime.getHour()) + ":" + String.valueOf(creationTime.getMinute());
        String days = String.valueOf(Duration.between(creationTime, OffsetDateTime.now()).toDays());
        String completeTimeString = String.format("**Created %s %s %s %s | %s Days ago!**", creationTime.getDayOfMonth(), month, year, timeString, days);
        String iconUrl = guild.getIconUrl();
        String SecurityEnabled = guild.getRequiredMFALevel().name().equals("TWO_FACTOR_AUTH") ? "Enabled" : "Disabled";
        String bans;
        try {
            bans = String.valueOf(guild.getController().getBans().complete().size());
        } catch (Exception e) {
            e.printStackTrace();
            bans = "Unknown";
        }
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(guild.getName() + " (" + id + ")", guild.getIconUrl(), null)
                .setDescription(completeTimeString)
                .setThumbnail(iconUrl == null ? "https://pbs.twimg.com/profile_images/843139126920544256/YeVk_bj-.jpg" : iconUrl)
                .addField("Server Region", region, true)
                .addField("2FA Enabled", SecurityEnabled, true)
                .addField("Users online/total", users, true)
                .addField("Bots online/total", bots, true)
                .addField("Text Channels", textChannels + " | Hidden (" + user + ") - " + hiddenChannels, true)
                .addField("Voice Channels", voiceChannels, true)
                .addField("Roles", roles, true)
                .addField("Highest Role", highestRole, true)
                .addField("Verification Level", verificationLevel, true)
                .addField("Bans", bans, true)
                .addField("Owner", String.format("[%s](%s)", owner, guildOwner.getUser().getAvatarUrl()), true);
        channel.sendMessage(embed.build()).queue();

    }
}
