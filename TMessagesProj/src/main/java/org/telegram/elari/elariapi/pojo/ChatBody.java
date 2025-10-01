package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class ChatBody {

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
}
