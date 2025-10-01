package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import org.telegram.elari.C;


@SuppressWarnings("unused")
public class CheckBoundRequest {

    @SerializedName("account_id")
    private long accountId;
    @SerializedName("dev_type")
    private String devType = C.DEVICE_TYPE;
    @SerializedName("man_id")
    private String manId;
    @SerializedName("ApiKey")
    private String apiKey = "debug";

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

}
