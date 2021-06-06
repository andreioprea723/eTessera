package com.example.etessera20.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Artist {
    private String id;
    private String nume;
    private String imagine;

    public Artist(){

    }

    public Artist(String id, String nume, String imagine) {
        this.id = id;
        this.nume = nume;
        this.imagine = imagine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }
}
