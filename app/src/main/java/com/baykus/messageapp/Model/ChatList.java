package com.baykus.messageapp.Model;

import java.io.Serializable;

public class ChatList implements Serializable {
    public String id;

    public ChatList(String id) {
        this.id = id;
    }

    public ChatList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
