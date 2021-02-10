package com.baykus.messageapp.Model;

import java.io.Serializable;

public class Users implements Serializable {
    private String id;
    private String userName;
    private String imageURL;
    private String status;
    private String search;

    public Users(String id, String userName, String imageURL, String status, String search) {
        this.id = id;
        this.userName = userName;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public Users() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}