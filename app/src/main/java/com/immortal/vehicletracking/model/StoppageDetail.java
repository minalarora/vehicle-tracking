package com.immortal.vehicletracking.model;

public class StoppageDetail {

    private String item_id;
    private String item_number;
    private String item_img;
    private String item_alert;
    private String item_duration;
    private String item_parking;
    private String item_distance;
    private String item_arrival_date;
    private String item_arrival_time;
    private String item_departure_date;
    private String item_departure_time;
    private String item_address;
    private String lat, lng;

    public StoppageDetail(String item_id, String item_number, String item_img, String item_alert, String item_duration, String item_parking, String item_distance,
                          String item_arrival_date, String item_arrival_time, String item_departure_date, String item_departure_time, String item_address, String lat, String lng) {
        this.item_id = item_id;
        this.item_number = item_number;
        this.item_img = item_img;
        this.item_alert = item_alert;
        this.item_duration = item_duration;
        this.item_parking = item_parking;
        this.item_distance = item_distance;
        this.item_arrival_date = item_arrival_date;
        this.item_arrival_time = item_arrival_time;
        this.item_departure_date = item_departure_date;
        this.item_departure_time = item_departure_time;
        this.item_address = item_address;
        this.lat = lat;
        this.lng = lng;
    }


    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public String getItem_alert() {
        return item_alert;
    }

    public void setItem_alert(String item_alert) {
        this.item_alert = item_alert;
    }

    public String getItem_duration() {
        return item_duration;
    }

    public void setItem_duration(String item_duration) {
        this.item_duration = item_duration;
    }

    public String getItem_parking() {
        return item_parking;
    }

    public void setItem_parking(String item_parking) {
        this.item_parking = item_parking;
    }

    public String getItem_distance() {
        return item_distance;
    }

    public void setItem_distance(String item_distance) {
        this.item_distance = item_distance;
    }

    public String getItem_arrival_date() {
        return item_arrival_date;
    }

    public void setItem_arrival_date(String item_arrival_date) {
        this.item_arrival_date = item_arrival_date;
    }

    public String getItem_arrival_time() {
        return item_arrival_time;
    }

    public void setItem_arrival_time(String item_arrival_time) {
        this.item_arrival_time = item_arrival_time;
    }

    public String getItem_departure_date() {
        return item_departure_date;
    }

    public void setItem_departure_date(String item_departure_date) {
        this.item_departure_date = item_departure_date;
    }

    public String getItem_departure_time() {
        return item_departure_time;
    }

    public void setItem_departure_time(String item_departure_time) {
        this.item_departure_time = item_departure_time;
    }

    public String getItem_address() {
        return item_address;
    }

    public void setItem_address(String item_address) {
        this.item_address = item_address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}


