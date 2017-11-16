package com.example.admin.btloop.dialog;

/**
 * Created by Admin on 11/10/2017.
 */

public class UserInfo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String name;
    private String id;
    private String avatar;

    public int getCoint() {
        return coint;
    }

    public void setCoint(int coint) {
        this.coint = coint;
    }

    private int coint;
}
