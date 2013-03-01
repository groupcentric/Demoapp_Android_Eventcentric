package com.eventcentric.helper;

public class Town {
    String name;
    int id;
    String latitude,longitude;


    public Town(String name) {
        this.name = name.replace("_", ", ");
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //022912 -  Aded new lat\long
    public void setLatitude (String strLatitude){
        this.latitude = strLatitude;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLongitude (String strLongitude){
        this.longitude = strLongitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
