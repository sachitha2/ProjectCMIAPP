package com.example.chata.projectcmi;

public class SingleRowForPendingOrders {
    String name;
    String id;
    String shopId;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    String qty;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String total;

    public SingleRowForPendingOrders(String name, String id, String address, String contact, String route, String idCardN, String credit) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.Contact = contact;
        this.route = route;
        this.idCardN = idCardN;
        this.credit = credit;
    }

    //By Chatson
    String address;
    String Contact;
    String route;
    String idCardN;
    String credit;
    //By Chatson


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public SingleRowForPendingOrders(String name, String id, String shopId,String total,String qty) {
        this.name = name;
        this.id = id;
        this.shopId = shopId;
        this.total = total;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public  String getId(){
        return  id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void  setId(String id){
        this.id = id;
    }


    public String getAddress() {
        return address;
    }

    public String getContact() {
        return Contact;
    }

    public String getRoute() {
        return route;
    }

    public String getIdCardN() {
        return idCardN;
    }

    public String getCredit() {
        return credit;
    }
}
