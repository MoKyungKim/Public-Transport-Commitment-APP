package com.example.cooperativeproject2;

public class object {

    String Json;
    //photo

    public object(String Json) {
        this.Json = Json;

    }

    public String getName() {
        return Json;
    }


    public void setName(String name) {
        this.Json = name;
    }

}