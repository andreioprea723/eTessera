package com.example.etessera20.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Eveniment {

    private String id;
    private String titlu;
    private String thumbnail;
    private String type;
    private String description;
    private Long price;
    private String cover_img;
    private String video;

    public Eveniment(){

    }

    public Eveniment(String id, String titlu, String thumbnail, String type, String description, Long price, String cover_img, String video) {
        this.id = id;
        this.titlu = titlu;
        this.thumbnail = thumbnail;
        this.type = type;
        this.description = description;
        this.price = price;
        this.cover_img = cover_img;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
