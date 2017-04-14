package co.automod.bot.core.settings.types;

import co.automod.bot.core.settings.IGuildSettingType;
import co.automod.bot.util.Emoji;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * TextChannel settings type
 * the value has to be a real channel in a guild + will be saved as the channel id
 */
public class TextChannelSettingType implements IGuildSettingType {

    private final boolean allowNull;

    /**
     * Allow a null/false value?
     *
     * @param allowNull true if it can be null
     */
    public TextChannelSettingType(boolean allowNull) {

        this.allowNull = allowNull;
    }

    @Override
    public String typeName() {
        return "text-channel";
    }

    @Override
    public boolean validate(Guild guild, String value) {
        if (allowNull && (value == null || value.isEmpty() || value.equalsIgnoreCase("false"))) {
            return true;
        }
        if (Misc.isChannelMention(value)) {
            return guild.getTextChannelById(Misc.channelMentionToId(value)) != null;
        }
        return Misc.findChannel(guild, value) != null;
    }

    @Override
    public String fromInput(Guild guild, String value) {
        if (allowNull && (value == null || value.isEmpty() || value.equalsIgnoreCase("false"))) {
            return "";
        }
        if (Misc.isChannelMention(value)) {
            TextChannel textChannel = guild.getTextChannelById(Misc.channelMentionToId(value));
            if (textChannel != null) {
                return textChannel.getId();
            }
        }
        TextChannel channel = Misc.findChannel(guild, value);
        if (channel != null) {
            return channel.getId();
        }
        return "";
    }

    @Override
    public String toDisplay(Guild guild, String value) {
        if (value == null || value.isEmpty() || !value.matches("\\d{10,}")) {
            return Emoji.X;
        }
        TextChannel channel = guild.getTextChannelById(value);
        if (channel != null) {
            return channel.getName();
        }
        return Emoji.X;
    }
}
