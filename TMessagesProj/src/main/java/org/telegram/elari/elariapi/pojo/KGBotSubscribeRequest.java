package org.telegram.elari.elariapi.pojo;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KGBotSubscribeRequest {

    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @SerializedName("man_id")
    private String manId;
    @SerializedName("lang")
    private String lang;
    @SerializedName("iswatches")
    private Integer iswatches = 0;


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

    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }


}

