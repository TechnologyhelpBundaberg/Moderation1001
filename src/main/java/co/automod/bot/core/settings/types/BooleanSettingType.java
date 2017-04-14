package co.automod.bot.core.settings.types;

import co.automod.bot.core.settings.IGuildSettingType;
import co.automod.bot.util.Emoji;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.entities.Guild;

/**
 * boolean settings type
 * {@link Misc#isFuzzyTrue(String)}   yes
 * {@link Misc#isFuzzyFalse(String)}  no
 */
public class BooleanSettingType implements IGuildSettingType {
    @Override
    public String typeName() {
        return "toggle";
    }

    @Override
    public boolean validate(Guild guild, String value) {
        return value != null && (Misc.isFuzzyTrue(value) || Misc.isFuzzyFalse(value));
    }

    @Override
    public String fromInput(Guild guild, String value) {
        return Misc.isFuzzyTrue(value) ? "true" : "false";
    }

    @Override
    public String toDisplay(Guild guild, String value) {
        return "true".equals(value) ? Emoji.OK_HAND : Emoji.X;
    }
}
