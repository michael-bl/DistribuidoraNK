package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Modo {
    public Modo(){
    }
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("mode")
    @Expose
    private int mode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
