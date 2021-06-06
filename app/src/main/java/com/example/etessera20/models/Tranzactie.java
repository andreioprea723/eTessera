package com.example.etessera20.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Tranzactie {
    private String id;
    private long valoare;
    private String data;
    private String tip;
    private String nume;
    private String id_user;

    public Tranzactie(){

    }

    public Tranzactie(String id, long valoare, String data, String tip, String nume, String id_user) {
        this.id = id;
        this.valoare = valoare;
        this.data = data;
        this.tip = tip;
        this.nume = nume;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getValoare() {
        return valoare;
    }

    public void setValoare(long valoare) {
        this.valoare = valoare;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
