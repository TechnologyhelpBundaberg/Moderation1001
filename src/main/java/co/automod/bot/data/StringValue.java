package co.automod.bot.data;

import java.beans.ConstructorProperties;

public class StringValue {

    private String id;
    private String value;

    @ConstructorProperties({"id", "value"})
    public StringValue(String gid, String value) {
        this.id = gid;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
