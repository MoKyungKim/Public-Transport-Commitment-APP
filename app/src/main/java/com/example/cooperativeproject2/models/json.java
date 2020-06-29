package com.example.cooperativeproject2.models;

import java.util.List;

public class json {
    private List path;

    public List getPath()
    {
        return path;
    }

    public void setPath(List path){
        this.path = path;
    }

    public String toString(){
        return "path=" + path;
    }


}
