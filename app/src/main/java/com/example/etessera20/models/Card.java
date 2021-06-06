package com.example.etessera20.models;

public class Card {
    private String cardHolder;
    private String numarCard;
    private String dataExpirare;
    private String cvv;
    private String id_user;

    public Card(){

    }

    public Card(String cardHolder, String numarCard, String dataExpirare, String cvv, String id_user) {
        this.cardHolder = cardHolder;
        this.numarCard = numarCard;
        this.dataExpirare = dataExpirare;
        this.cvv = cvv;
        this.id_user = id_user;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumarCard() {
        return numarCard;
    }

    public void setNumarCard(String numarCard) {
        this.numarCard = numarCard;
    }

    public String getDataExpirare() {
        return dataExpirare;
    }

    public void setDataExpirare(String dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
