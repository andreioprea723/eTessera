package com.example.etessera20.models;

public class UtilizatorCard {
    private String id;
    private String id_user;
    private String id_card;

    public UtilizatorCard(){

    }

    public UtilizatorCard(String id, String id_user, String id_card) {
        this.id = id;
        this.id_user = id_user;
        this.id_card = id_card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }
}
