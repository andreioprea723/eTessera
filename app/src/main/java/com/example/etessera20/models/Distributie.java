package com.example.etessera20.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Distributie {
    private String id;
    private String id_eveniment;
    private String id_artist;

    public Distributie(){

    }

    public Distributie(String id, String id_eveniment, String id_artist) {
        this.id = id;
        this.id_eveniment = id_eveniment;
        this.id_artist = id_artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_eveniment() {
        return id_eveniment;
    }

    public void setId_eveniment(String id_eveniment) {
        this.id_eveniment = id_eveniment;
    }

    public String getId_artist() {
        return id_artist;
    }

    public void setId_artist(String id_artist) {
        this.id_artist = id_artist;
    }


}
