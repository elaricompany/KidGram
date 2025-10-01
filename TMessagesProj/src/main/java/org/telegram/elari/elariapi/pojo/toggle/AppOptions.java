package org.telegram.elari.elariapi.pojo.toggle;

import java.util.List;

public class AppOptions{
    private int code;
    private List<TogglesItem> toggles;
    private int error;
    private String message;

    public int getCode(){
        return code;
    }

    public List<TogglesItem> getToggles(){
        return toggles;
    }

    public int getError(){
        return error;
    }

    public String getMessage(){
        return message;
    }
}
