package co.automod.bot.core;

import co.automod.bot.Config;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

public class Rethink {

    public static Connection connection;
    public static final RethinkDB r = RethinkDB.r;

    public static void init() {
        connection = r.connection().hostname(Config.rethink_host).port(Config.rethink_port).db(Config.rethink_db_name).connect();
        if (!(boolean) r.dbList().contains(Config.rethink_db_name).run(connection)) {
            r.dbCreate(Config.rethink_db_name).runNoReply(connection);
        }
        String[] requiredTables = {"modlog", "antilink", "prefixes"};
        for (String tbl : requiredTables) {
            createTable(tbl);
        }
    }

    public static void createTable(String tableName) {
        if (!(boolean) r.db(Config.rethink_db_name).tableList().contains(tableName).run(connection)) {
            r.db(Config.rethink_db_name).tableCreate(tableName).runNoReply(connection);
        }
    }

    public static Object getConfigField(String guildId, String table, String field) {
        return r.table(table).get(guildId).getField(field).run(connection);
    }

    public static void saveConfigField(String table, Object data) {
        r.table(table).insert(data).optArg("conflict", "replace").runNoReply(connection);
    }
}
