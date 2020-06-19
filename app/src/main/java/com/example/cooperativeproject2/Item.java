package com.example.cooperativeproject2;

public class Item {

    String name;
    //photo
    String summary;

    public Item(String name,  String summary) {
        this.name = name;

        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}