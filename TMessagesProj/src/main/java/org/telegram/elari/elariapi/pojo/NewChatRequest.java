package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class NewChatRequest {
    @SerializedName("ApiKey")
    private String apiKey = "debug";

    @SerializedName("man_id")
    private String manId;

    @SerializedName("dev_type")
    private String devType;

    @SerializedName("account_id")
    private long accountId;

    @SerializedName("lang")
    private String lang;

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("fio")
    private String fio;

    @SerializedName("type")
    private String type;

    @SerializedName("phone")
    private String phone;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("access")
    private short access = 0;

    @SerializedName("link")
    private String link = "";
    @SerializedName("member_count")
    private Integer memberCount = -1;
    @SerializedName("description")
    private String description = "";

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public short getAccess() {
        return access;
    }

    public void setAccess(short access) {
        this.access = access;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = "https://t.me/" + link;
    }

    public Integer getMemberCount() {return memberCount;}
    public void setMemberCount(Integer memberCount) {this.memberCount = memberCount;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
