package com.immortal.vehicletracking.model;

public class TravelSummary {

    private String item_id;
    private String item_number;
    private String item_img;
    private String item_running;
    private String item_idle;
    private String item_stop;
    private String item_distance;
    private String item_max_speed;
    private String item_avg_speed;
    private String item_travel_active;
    private String item_travel_alert;
    private String item_travel_date;

    public TravelSummary(String item_id, String item_number, String item_img, String item_running, String item_idle,
                         String item_stop, String item_distance, String item_travel_active, String item_max_speed,
                         String item_avg_speed, String item_travel_alert, String item_travel_date) {
        this.item_id = item_id;
        this.item_number = item_number;
        this.item_img = item_img;
        this.item_running = item_running;
        this.item_idle = item_idle;
        this.item_stop = item_stop;
        this.item_distance = item_distance;
        this.item_travel_active = item_travel_active;
        this.item_max_speed = item_max_speed;
        this.item_avg_speed = item_avg_speed;
        this.item_travel_alert = item_travel_alert;
        this.item_travel_date = item_travel_date;
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

    public String getItem_running() {
        return item_running;
    }

    public void setItem_running(String item_running) {
        this.item_running = item_running;
    }

    public String getItem_idle() {
        return item_idle;
    }

    public void setItem_idle(String item_idle) {
        this.item_idle = item_idle;
    }

    public String getItem_stop() {
        return item_stop;
    }

    public void setItem_stop(String item_stop) {
        this.item_stop = item_stop;
    }

    public String getItem_distance() {
        return item_distance;
    }

    public void setItem_distance(String item_distance) {
        this.item_distance = item_distance;
    }

    public String getItem_travel_active() {
        return item_travel_active;
    }

    public void setItem_travel_active(String item_travel_active) {
        this.item_travel_active = item_travel_active;
    }

    public String getItem_max_speed() {
        return item_max_speed;
    }

    public void setItem_max_speed(String item_max_speed) {
        this.item_max_speed = item_max_speed;
    }

    public String getItem_avg_speed() {
        return item_avg_speed;
    }

    public void setItem_avg_speed(String item_avg_speed) {
        this.item_avg_speed = item_avg_speed;
    }

    public String getItem_travel_alert() {
        return item_travel_alert;
    }

    public void setItem_travel_alert(String item_travel_alert) {
        this.item_travel_alert = item_travel_alert;
    }

    public String getItem_travel_date() {
        return item_travel_date;
    }

    public void setItem_travel_date(String item_travel_date) {
        this.item_travel_date = item_travel_date;
    }
}