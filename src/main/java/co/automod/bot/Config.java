package co.automod.bot;

import com.kaaz.configuration.ConfigurationOption;

public class Config {

    @ConfigurationOption
    public static String discord_token = "your-discord-token";

    @ConfigurationOption
    public static String rethink_host = "localhost";
    @ConfigurationOption
    public static int rethink_port = 28015;
    @ConfigurationOption
    public static String rethink_db_name = "automod";

    @ConfigurationOption
    public static String default_command_prefix = "!";
    public final static long SHARD_RATELIMIT = 5_000L;
}
