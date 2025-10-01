package org.telegram.elari.elariapi.pojo.channels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Item {

    @SerializedName("default_enabled")
    private Long defaultEnabled;
    @SerializedName("display_name")
    private String displayName;
    @Expose
    private String name;

    public Long getDefaultEnabled() {
        return defaultEnabled;
    }

    public void setDefaultEnabled(Long defaultEnabled) {
        this.defaultEnabled = defaultEnabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
