package co.automod.bot.data;

import java.beans.ConstructorProperties;

public class GuildPrefix {

    private String prefix;
    private String GuildID;

    @ConstructorProperties({"id", "prefix"})
    public GuildPrefix(String gid, String prefix) {
        this.prefix = prefix;
        this.GuildID = gid;
    }

    public String getprefix() {
        return prefix;
    }

    public String getId() {
        return GuildID;
    }
}
