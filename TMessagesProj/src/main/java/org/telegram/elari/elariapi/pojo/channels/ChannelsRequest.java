package org.telegram.elari.elariapi.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressWarnings("unused")
public class ChannelsRequest {

    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @Expose
    private String lang;

    public String getApiKey() {
        return apiKey;
    }

    public ChannelsRequest setApiKey(String apiKey) {
        this.apiKey = apiKey; return this;
    }

    public String getLang() {
        return lang;
    }

    public ChannelsRequest setLang(String lang) {
        this.lang = lang; return this;
    }

}
