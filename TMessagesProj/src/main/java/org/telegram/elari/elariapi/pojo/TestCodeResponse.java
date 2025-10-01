package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

//{"error":0,"code":"82239"}
public class TestCodeResponse {
    @SerializedName("code")
    private String code;
    @SerializedName("error")
    private int error;
    public String getCode(){
        return code;
    }
    public int getError(){
        return error;
    }
}
