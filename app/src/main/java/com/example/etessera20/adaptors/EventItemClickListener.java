package com.example.etessera20.adaptors;

import android.widget.ImageView;

import com.example.etessera20.models.Eveniment;

public interface EventItemClickListener {

    void onEventClick(Eveniment eveniment, ImageView eventImageView);
}
