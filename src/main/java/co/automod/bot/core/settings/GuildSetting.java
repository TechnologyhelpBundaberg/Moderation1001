package co.automod.bot.core.settings;

import net.dv8tion.jda.core.entities.Guild;

abstract public class GuildSetting<T extends IGuildSettingType> {
    final private T type;

    public GuildSetting() {
        type = getSettingsType();
    }

    protected abstract T getSettingsType();

    /**
     * key for the configuration
     * used in discord
     *
     * @return keyname
     */
    public abstract String getKey();

    /**
     * table used
     *
     * @return table name
     */
    public abstract String dbTable();

    /**
     * table field used
     * used when reading/writing to/from the db
     *
     * @return table name
     */
    public abstract String dbField();

    /**
     * default value for the config
     *
     * @return default
     */
    public abstract String getDefault();

    /**
     * Description for the config
     *
     * @return short description
     */
    public abstract String getDescription();

    /**
     * Checks if the value is a valid setting
     *
     * @param input value to check
     * @return is it a valid value
     */
    public boolean isValidValue(Guild guild, String input) {
        return type.validate(guild, input);
    }

    public String getValue(Guild guild, String input) {
        return type.fromInput(guild, input);
    }

    public String toDisplay(Guild guild, String value) {
        return type.toDisplay(guild, value);
    }

    public T getSettingTypeObject() {
        return type;
    }

    public String getSettingType() {
        return type.typeName();
    }
}
