package com.example.etessera20.models;

import android.widget.Button;

public class Slide {
    public String image;
    public String title;
    public String video;

    public Slide(String image, String title, String video) {
        this.image = image;
        this.title = title;
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
