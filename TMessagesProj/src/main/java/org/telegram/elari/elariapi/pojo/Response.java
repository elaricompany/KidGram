package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("error")
    private int error;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "error=" + error +
                '}';
    }
}
