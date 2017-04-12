package co.automod.bot;

import co.automod.bot.core.ShardContainer;
import com.kaaz.configuration.ConfigurationBuilder;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import java.io.File;

public class Main {
    public static ShardContainer bot;
    public static final RethinkDB r = RethinkDB.r;
    public static final Connection conn = r.connection().hostname("localhost").port(28015).db("settings").connect();

    public static void main(String[] args) throws Exception {
        new ConfigurationBuilder(Config.class, new File("bot.cfg")).build();
        bot = new ShardContainer();
    }

    public static void exit(ExitStatus status) {
        System.exit(status.getCode());

    }
}
