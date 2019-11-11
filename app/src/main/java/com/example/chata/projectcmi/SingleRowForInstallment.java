package com.example.chata.projectcmi;

public class SingleRowForInstallment {

    String payment;
    String rpayment;
    String rdate;
    String id;
    String dueDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getRpayment() {
        return rpayment;
    }

    public void setRpayment(String rpayment) {
        this.rpayment = rpayment;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }



    public SingleRowForInstallment( String payment, String rpayment, String rdate, String dueDate,String id) {

        this.payment = payment;
        this.rpayment = rpayment;
        this.rdate = rdate;
        this.dueDate = dueDate;
        this.id = id;
    }
    
}
