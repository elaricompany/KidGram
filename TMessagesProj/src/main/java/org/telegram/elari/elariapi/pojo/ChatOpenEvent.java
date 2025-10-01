package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class ChatOpenEvent {
    @SerializedName("id")
    private long id;
    @SerializedName("account_id")
    private long accountId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
