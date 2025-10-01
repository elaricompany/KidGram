package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

import org.telegram.elari.C;

@SuppressWarnings("unused")
public class DeleteAccountRequest {

    @SerializedName("account_id")
    private long mAccountId;
    @SerializedName("ApiKey")
    private String mApiKey = "debug";
    @SerializedName("dev_type")
    private String mDevType = C.DEVICE_TYPE;
    @SerializedName("man_id")
    private String mManId;

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    public String getDevType() {
        return mDevType;
    }

    public void setDevType(String devType) {
        mDevType = devType;
    }

    public String getManId() {
        return mManId;
    }

    public void setManId(String manId) {
        mManId = manId;
    }

}
