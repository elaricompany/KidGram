package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class StatRequest {
    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @SerializedName("msg_text")
    private String msg_text;
    @SerializedName("msg_user")
    private String msg_user;
    @SerializedName("chat_name")
    private String chat_name;
    @SerializedName("man_id")
    private String manId;
    @SerializedName("dev_type")
    private String dev_type;
    @SerializedName("account_id")
    private Long accountId;
    @SerializedName("chat_id")
    private Long chatId;
    @SerializedName("msg_id")
    private Long messageId;
    @SerializedName("msg_type")
    private byte messageType;
    @SerializedName("direct")
    private char direct;
    @SerializedName("date")
    private String date;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    public String getMsgText() {
        return msg_text;
    }

    public void setMsgText(String msg_text) {
        this.msg_text = msg_text;
    }

    public String getUserName() {
        return msg_user;
    }
    public void setUserName(String msg_user) {
        this.msg_user = msg_user;
    }

    public String getChatName() {
        return chat_name;
    }
    public void setChatName(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getDev_type() {
        return dev_type;
    }

    public void setDev_type(String dev_type) {
        this.dev_type = dev_type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public char getDirect() {
        return direct;
    }

    public void setDirect(char direct) {
        this.direct = direct;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StatRequest{" +
                "apiKey='" + apiKey + '\'' +
                ", manId='" + manId + '\'' +
                ", dev_type='" + dev_type + '\'' +
                ", accountId=" + accountId +
                ", chatId=" + chatId +
                ", messageId=" + messageId +
                ", messageType=" + messageType +
                ", direct=" + direct +
                ", date='" + date + '\'' +
                '}';
    }
}
