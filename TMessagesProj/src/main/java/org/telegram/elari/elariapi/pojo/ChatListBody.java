package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatListBody {

    @SerializedName("ApiKey")
    private String apiKey = "debug";

    @SerializedName("man_id")
    private String imei;

    @SerializedName("dev_type")
    private String deviceType;

    @SerializedName("account_id")
    private long accountId;

    @SerializedName("data")
    private List<ChatBody> data = null;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public List<ChatBody> getData() {
        return data;
    }

    public void setData(List<ChatBody> data) {
        this.data = data;
    }
}
