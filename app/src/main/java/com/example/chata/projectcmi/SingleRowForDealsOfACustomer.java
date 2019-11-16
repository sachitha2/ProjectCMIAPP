package com.example.chata.projectcmi;

public class SingleRowForDealsOfACustomer {




    String dealId;
    int total;
    int rpayment;
    int balance;
    boolean status;




    public SingleRowForDealsOfACustomer(String dealId, int total, boolean status,int rpayment,int balance) {
        this.dealId = dealId;
        this.total = total;
        this.status = status;
        this.rpayment = rpayment;
        this.balance = balance;
    }

    public int getRpayment() {
        return rpayment;
    }

    public void setRpayment(int rpayment) {
        this.rpayment = rpayment;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
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
