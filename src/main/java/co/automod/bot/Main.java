package co.automod.bot;

import co.automod.bot.core.ShardContainer;
import com.kaaz.configuration.ConfigurationBuilder;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        new ConfigurationBuilder(Config.class, new File("bot.cfg")).build();
        ShardContainer bot = new ShardContainer();
    }

    public static void exit(ExitStatus status) {
        System.exit(status.getCode());

    }
}
