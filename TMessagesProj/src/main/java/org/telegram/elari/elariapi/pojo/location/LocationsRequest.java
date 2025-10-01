
package org.telegram.elari.elariapi.pojo.location;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.telegram.elari.C;

@SuppressWarnings("unused")
public class LocationsRequest {

    @Expose
    private ArrayList<Location> locations;

    @SerializedName("account_id")
    private long accountId;
    @SerializedName("dev_type")
    private String devType = C.DEVICE_TYPE;
    @SerializedName("man_id")
    private String manId;
    @SerializedName("ApiKey")
    private String apiKey = "debug";
    @SerializedName("tag")
    private String tag = "";

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }


    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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

}
