package co.automod.bot;

import com.kaaz.configuration.ConfigurationOption;

public class Config {

    @ConfigurationOption
    public static String discord_token = "your-discord-token";

    @ConfigurationOption
    public static String command_prefix = "!";
    public final static long SHARD_RATELIMIT = 5_000L;
}
