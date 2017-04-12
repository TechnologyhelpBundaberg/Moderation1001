package co.automod.bot.data;

import java.beans.ConstructorProperties;

public class GuildChannel {

    private String ChannelId;
    private String GuildID;

    @ConstructorProperties({"id", "ChannelId"})
    public GuildChannel(String gid, String chid) {
        this.ChannelId = chid;
        this.GuildID = gid;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public String getId() {
        return GuildID;
    }
}
