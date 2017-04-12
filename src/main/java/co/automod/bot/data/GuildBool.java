package co.automod.bot.data;

import java.beans.ConstructorProperties;

public class GuildBool {

    private Boolean bool;
    private String GuildID;

    @ConstructorProperties({"id", "bool"})
    public GuildBool(String gid, Boolean bool) {
        this.bool = bool;
        this.GuildID = gid;
    }

    public Boolean getbool() {
        return bool;
    }

    public String getId() {
        return GuildID;
    }
}
