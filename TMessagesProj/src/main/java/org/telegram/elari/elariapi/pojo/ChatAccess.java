package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class ChatAccess {
    @SerializedName("id")
    private long id;

    @SerializedName("access")
    private short access;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getAccess() {
        return access;
    }

    public void setAccess(short access) {
        this.access = access;
    }
}
