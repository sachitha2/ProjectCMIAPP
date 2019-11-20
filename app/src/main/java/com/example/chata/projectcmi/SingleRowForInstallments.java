package com.example.chata.projectcmi;

public class SingleRowForInstallments {



    int id;
    String dueDate;
    String amount;
    String cName;
    String cid;
    String area;
    String nic;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }





    public SingleRowForInstallments(int id, String dueDate, String amount, String cName, String cid, String area, String nic) {
        this.id = id;
        this.dueDate = dueDate;
        this.amount = amount;
        this.cName = cName;
        this.cid = cid;
        this.area = area;
        this.nic = nic;
    }


}
