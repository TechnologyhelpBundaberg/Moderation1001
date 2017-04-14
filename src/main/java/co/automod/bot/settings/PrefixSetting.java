package co.automod.bot.settings;

import co.automod.bot.Config;
import co.automod.bot.core.settings.GuildSetting;
import co.automod.bot.core.settings.types.StringLengthSettingType;

public class PrefixSetting extends GuildSetting<StringLengthSettingType> {
    @Override
    protected StringLengthSettingType getSettingsType() {
        return new StringLengthSettingType(1, 16);
    }

    @Override
    public String getKey() {
        return "prefix";
    }

    @Override
    public String dbTable() {
        return "prefixes";
    }

    @Override
    public String dbField() {
        return "value";
    }

    @Override
    public String getDefault() {
        return Config.default_command_prefix;
    }

    @Override
    public String getDescription() {
        return "The prefix used in commands \n\n" +
                "defaults to '" + Config.default_command_prefix + "'\n\n" +
                "Can be set to anything between 1 and 16 characters long";
    }
}