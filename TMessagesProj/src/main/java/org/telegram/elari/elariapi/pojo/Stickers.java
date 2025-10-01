package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Stickers {
    @SerializedName("error")
    private short error;
    @SerializedName("data")
    private ArrayList<Long> data;

    public short getError() {
        return error;
    }

    public void setError(short error) {
        this.error = error;
    }

    public ArrayList<Long> getData() {
        return data;
    }

    public void setData(ArrayList<Long> data) {
        this.data = data;
    }
}
