package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class NewChatResponse {
    @SerializedName("error")
    private int error;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
