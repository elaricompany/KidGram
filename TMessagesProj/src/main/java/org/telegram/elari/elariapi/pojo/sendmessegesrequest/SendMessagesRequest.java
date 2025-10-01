package org.telegram.elari.elariapi.pojo.sendmessegesrequest;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressWarnings("unused")
public class SendMessagesRequest {

    @SerializedName("account_id")
    private Long accountId;
    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @SerializedName("dev_type")
    private String devType;
    @SerializedName("man_id")
    private String manId;
    @Expose
    private ArrayList<Msg> msgs;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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

    public ArrayList<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(ArrayList<Msg> msgs) {
        this.msgs = msgs;
    }

}
