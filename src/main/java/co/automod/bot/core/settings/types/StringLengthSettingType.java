package co.automod.bot.core.settings.types;

import co.automod.bot.core.settings.IGuildSettingType;
import net.dv8tion.jda.core.entities.Guild;

/**
 * string-length settings type
 * the setting has to be between min, and max (including)
 */
public class StringLengthSettingType implements IGuildSettingType {
    private final int min, max;

    /**
     * @param min minimum length
     * @param max maximum length (including)
     */
    public StringLengthSettingType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String typeName() {
        return "between";
    }

    @Override
    public boolean validate(Guild guild, String value) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    @Override
    public String fromInput(Guild guild, String value) {
        return value;
    }

    @Override
    public String toDisplay(Guild guild, String value) {
        return value;
    }
}
