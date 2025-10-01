package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class AccessRequest {
    @SerializedName("ApiKey")
    private String apiKey = "debug";

    @SerializedName("token")
    private String token = "";

    @SerializedName("man_id")
    private String manId;

    @SerializedName("dev_type")
    private String devType;

    @SerializedName("account_id")
    private long accountId;

    @SerializedName("id")
    private long id;

    @SerializedName("access")
    private short access = 2;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AccessRequest{" +
                "apiKey='" + apiKey + '\'' +
                ", manId='" + manId + '\'' +
                ", devType='" + devType + '\'' +
                ", accountId=" + accountId +
                ", id=" + id +
                ", access=" + access +
                '}';
    }
}
