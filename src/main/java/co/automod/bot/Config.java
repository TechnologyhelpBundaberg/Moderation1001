package co.automod.bot;

import com.kaaz.configuration.ConfigurationOption;

public class Config {

    @ConfigurationOption
    public static String discord_token = "NDYzNTQwMDQ2NjgzOTYzNDAy.Dhx40w._UZC6DKJGe-4x6-2KRmOk9j4fiY";

    @ConfigurationOption
    public static String rethink_host = "localhost";
    @ConfigurationOption
    public static int rethink_port = 28015;
    @ConfigurationOption
    public static String rethink_db_name = "settings";

    @ConfigurationOption
    public static String default_command_prefix = "p!";
    public final static long SHARD_RATELIMIT = 5_000L;
}
