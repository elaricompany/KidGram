package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class SearchNewChatPossibility {
    @SerializedName("error")
    private int error;

    @SerializedName("error_text")
    private String textError;

    @SerializedName("tlg_nc_disabled")
    private int isDisabled;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getTextError() {
        return textError;
    }

    public void setTextError(String textError) {
        this.textError = textError;
    }

    public Boolean getIsDisabled() {
        return isDisabled == 1;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled ? 0 : 1;
    }
}
