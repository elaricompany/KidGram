package org.telegram.elari.elariapi.pojo.sendmessegesrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Msg {

    @SerializedName("chat_id")
    private Long chatId;
    @SerializedName("chat_name")
    private String chatName;
    @Expose
    private String date;
    @Expose
    private char direct;
    @SerializedName("msg_id")
    private Long msgId;
    @SerializedName("msg_text")
    private String msgText;
    @SerializedName("msg_type")
    private byte msgType;
    @SerializedName("msg_user")
    private String msgUser;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public char getDirect() {
        return direct;
    }

    public void setDirect(char direct) {
        this.direct = direct;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

}
