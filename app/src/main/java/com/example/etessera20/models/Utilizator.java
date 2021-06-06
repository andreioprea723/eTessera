package com.example.etessera20.models;

import java.util.List;

public class Utilizator {

    private String email;
    private String nume;
    private String password;
    private long buget;
    private String card;

    public Utilizator(){

    }

    public Utilizator(String email, String nume, String password, long buget, String card) {
        this.email = email;
        this.nume = nume;
        this.password = password;
        this.buget = buget;
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBuget() {
        return buget;
    }

    public void setBuget(long buget) {
        this.buget = buget;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
