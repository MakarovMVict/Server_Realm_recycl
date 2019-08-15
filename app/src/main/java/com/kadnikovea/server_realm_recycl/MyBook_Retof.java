package com.kadnikovea.server_realm_recycl;


import com.google.gson.annotations.SerializedName;

public class MyBook_Retof {


    @SerializedName("name")
    private String title;
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
