package com.example.chata.projectcmi;

public class SingleRowForInstallment {
    String total;
    String payment;
    String rpayment;
    String rdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

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

    String dueDate;

    public SingleRowForInstallment(String total, String payment, String rpayment, String rdate, String dueDate,String id) {
        this.total = total;
        this.payment = payment;
        this.rpayment = rpayment;
        this.rdate = rdate;
        this.dueDate = dueDate;
        this.id = id;
    }




    public SingleRowForInstallment(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }



}
