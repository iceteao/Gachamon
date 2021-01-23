package com.example.gachamon.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokeStat {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("base_stat")
    private String base_stat;

    public String getBase_stat() {
        return base_stat;
    }
    public void setBase_stat(String base_stat) {
        this.base_stat = base_stat;
    }


}
