package com.example.cooperativeproject2.fragments;

public class Recent {
    private String address;
    private String link;

    public Recent(String _address,String _link){
        address = _address;
        link= _link;
    }

    public String getAddress() {
        return address;
    }

    public String getLink() {
        return link;
    }
}
