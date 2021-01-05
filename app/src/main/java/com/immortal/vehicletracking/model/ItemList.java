package com.immortal.vehicletracking.model;

public class ItemList {
    private String item_name;
    private String item_number;
    private String item_img;
    private String item_km;
    private String item_location;
    private double item_lat;
    private double item_long;
    private String item_id;

    public ItemList(String item_name, String item_number, String item_img, String item_km, String item_location,
                    double item_lat, double item_long, String item_id) {
        this.item_name = item_name;
        this.item_number = item_number;
        this.item_img = item_img;
        this.item_km = item_km;
        this.item_location = item_location;
        this.item_lat = item_lat;
        this.item_long = item_long;
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getItem_km() {
        return item_km;
    }

    public void setItem_km(String item_km) {
        this.item_km = item_km;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public double getItem_lat() {
        return item_lat;
    }

    public void setItem_lat(double item_lat) {
        this.item_lat = item_lat;
    }

    public double getItem_long() {
        return item_long;
    }

    public void setItem_long(double item_long) {
        this.item_long = item_long;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
