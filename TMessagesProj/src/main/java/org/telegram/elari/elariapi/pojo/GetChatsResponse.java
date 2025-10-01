package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChatsResponse {
    @SerializedName("error")
    private int error;
    @SerializedName("account_id")
    private long accountId;
    @SerializedName("data")
    private List<ChatAccess> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public List<ChatAccess> getData() {
        return data;
    }

    public void setData(List<ChatAccess> data) {
        this.data = data;
    }
}
