package com.example.chata.projectcmi;

public class SingleRowForCustomer {

    String name;
    String age;
    String nic;
    String id;
    String address;
    String area;
    String tp;


    public SingleRowForCustomer(String name,String age,String nic,String id,String address,String area,String tp) {
        this.name = name;
        this.age = age;
        this.nic = nic;
        this.id = id;
        this.address = address;
        this.area = area;
        this.tp = tp;
    }



    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getName() {
        return name;
    }

    public  String getAge(){
        return  age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void  setAge(String age){
        this.age = age;
    }
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
