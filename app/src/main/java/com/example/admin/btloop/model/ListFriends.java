package com.example.admin.btloop.model;

import java.util.List;

/**
 * Created by Admin on 11/11/2017.
 */

public class ListFriends {
    public List<Friends> getData() {
        return data;
    }

    public void setData(List<Friends> data) {
        this.data = data;
    }

    private List<Friends> data;
}
