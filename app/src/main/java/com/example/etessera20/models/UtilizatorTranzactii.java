package com.example.etessera20.models;

public class UtilizatorTranzactii {
    private String id;
    private String id_user;
    private String id_tranzactie;

    public UtilizatorTranzactii(){

    }

    public UtilizatorTranzactii(String id, String id_user, String id_tranzactie) {
        this.id = id;
        this.id_user = id_user;
        this.id_tranzactie = id_tranzactie;
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

    public String getId_tranzactie() {
        return id_tranzactie;
    }

    public void setId_tranzactie(String id_tranzactie) {
        this.id_tranzactie = id_tranzactie;
    }
}
