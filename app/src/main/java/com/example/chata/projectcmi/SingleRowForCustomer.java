package com.example.chata.projectcmi;

public class SingleRowForCustomer {

    String name;
    String age;

    public SingleRowForCustomer(String name,String age) {
        this.name = name;
        this.age = age;
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
}
