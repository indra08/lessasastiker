package com.testing.testung.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String displayname;
    public String email;
    public String uid;
    public List<Shop> shops;
    public User(){

    }
    public void AddShop(Shop shop){
        if (shops==null){
            shops=new ArrayList<>();
            shops.add(shop);
        }else {
            shops.add(shop);
        }
    }
    public User(String displayname,String email,String uid){
        this.displayname=displayname;
        this.email=email;
        this.uid=uid;
        shops=new ArrayList<>();
    }
}
