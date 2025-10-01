package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("error")
    private int error;

    @SerializedName("access")
    private List<ChatAccess> access;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<ChatAccess> getAccess() {
        return access;
    }

    public void setAccess(List<ChatAccess> access) {
        this.access = access;
    }
}
