package com.example.chata.projectcmi;

public class SingleRowForDealsOfACustomer {




    String dealId;
    int total;
    boolean status;

    public SingleRowForDealsOfACustomer(String dealId, int total, boolean status) {
        this.dealId = dealId;
        this.total = total;
        this.status = status;
    }



    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }













}
