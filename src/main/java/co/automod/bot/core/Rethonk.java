package co.automod.bot.core;

import co.automod.bot.Config;
import com.rethinkdb.net.Connection;

import static com.rethinkdb.RethinkDB.r;

public class Rethonk {

    private static Connection connection;

    public static void init() {
        connection = r.connection().hostname(Config.rethink_host).port(Config.rethink_port).connect();
        if (!(boolean) r.dbList().contains(Config.rethink_db_name).run(connection)) {
            r.dbCreate(Config.rethink_db_name).run(connection);
        }
    }

    public static void createTable(String tableName) {
        if (!(boolean) r.db(Config.rethink_db_name).tableList().contains(tableName).run(connection)) {
            r.db(Config.rethink_db_name).tableCreate(tableName).run(connection);
        }
    }
}
