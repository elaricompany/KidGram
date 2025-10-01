package org.telegram.elari.elariapi.pojo;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendPushToAppRequest {

    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @Expose
    private String channel;
    @SerializedName("content_available")
    private Boolean contentAvailable;
    @Expose
    private JsonObject data;
    @SerializedName("man_id")
    private String manId;
    @Expose
    private String message;
    @SerializedName("mutable_content")
    private Boolean mutableContent;
    @SerializedName("notification_available")
    private Boolean notificationAvailable;
    @Expose
    private String prefix;
    @Expose
    private String priority;
    @Expose
    private String title;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean getContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(Boolean contentAvailable) {
        this.contentAvailable = contentAvailable;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getMutableContent() {
        return mutableContent;
    }

    public void setMutableContent(Boolean mutableContent) {
        this.mutableContent = mutableContent;
    }

    public Boolean getNotificationAvailable() {
        return notificationAvailable;
    }

    public void setNotificationAvailable(Boolean notificationAvailable) {
        this.notificationAvailable = notificationAvailable;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
