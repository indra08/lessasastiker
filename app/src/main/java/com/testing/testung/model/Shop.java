package com.testing.testung.model;

public class Shop {
    public String name;
    public String sku;
    public String identifier;
    public String date;
    public Shop(){

    }
    public Shop(String name, String sku,String identifier){
        this.name=name;
        this.sku=sku;
        this.identifier=identifier;
    }
    public Shop(String name, String sku,String identifier,String date){
        this.name=name;
        this.sku=sku;
        this.identifier=identifier;
        this.date=date;
    }

}
