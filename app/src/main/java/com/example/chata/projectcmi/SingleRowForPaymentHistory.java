package com.example.chata.projectcmi;

public class SingleRowForPaymentHistory {

    int id;
    float collection;
    String date;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCollection() {
        return collection;
    }

    public void setCollection(float collection) {
        this.collection = collection;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public SingleRowForPaymentHistory(int id, float collection, String date) {
        this.id = id;
        this.collection = collection;
        this.date = date;
    }



}
