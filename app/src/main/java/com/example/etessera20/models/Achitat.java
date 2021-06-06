package com.example.etessera20.models;

public class Achitat {
    private String id;
    private String id_eveniment;
    private String id_user;

    public Achitat(){

    }

    public Achitat(String id, String id_eveniment, String id_user) {
        this.id = id;
        this.id_eveniment = id_eveniment;
        this.id_user = id_user;
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

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
