package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class LocationBody {
    @SerializedName("man_id")
    private String manId;
    @SerializedName("dev_type")
    private String devType = "kgm";
    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @SerializedName("tag")
    private String tag;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("battery")
    private String battery;

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getDevId() {
        return devType;
    }

    public void setDevId(String devId) {
        this.devType = devId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }
}
