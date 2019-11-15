package com.example.chata.projectcmi;

public class SingleRowForPaymentHistory {

    int id;
    String collection;
    String date;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public SingleRowForPaymentHistory(int id, String collection, String date) {
        this.id = id;
        this.collection = collection;
        this.date = date;
    }



}
