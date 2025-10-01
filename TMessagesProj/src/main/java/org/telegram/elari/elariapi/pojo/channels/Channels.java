package org.telegram.elari.elariapi.pojo.channels;

import java.util.List;
import com.google.gson.annotations.Expose;
@SuppressWarnings("unused")
public class Channels {

    @Expose
    private Long code;
    @Expose
    private List<Item> items;
    @Expose
    private String message;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
