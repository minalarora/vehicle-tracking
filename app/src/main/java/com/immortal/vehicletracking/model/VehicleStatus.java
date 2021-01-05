package com.immortal.vehicletracking.model;

public class VehicleStatus {

    private String item_date;
    private String item_id;
    private String item_img;
    private String item_km;
    private String item_location;
    private String item_number;
    private String item_time;
    private int oil_electricity_alarm_int;
    private int ac_alarm_int;
    private int charging_alarm_int;
    private int gps_tracking_alarm_int;

    public VehicleStatus(String item_number2, String item_img2, String item_km2, String item_date2, String item_time2, String item_location2, String item_id2, int oil_electricity_alarm_int2, int ac_alarm_int2, int charging_alarm_int2, int gps_tracking_alarm_int2) {
        this.item_id = item_id2;
        this.item_number = item_number2;
        this.item_img = item_img2;
        this.item_km = item_km2;
        this.item_date = item_date2;
        this.item_time = item_time2;
        this.item_location = item_location2;
        this.oil_electricity_alarm_int = oil_electricity_alarm_int2;
        this.ac_alarm_int = ac_alarm_int2;
        this.charging_alarm_int = charging_alarm_int2;
        this.gps_tracking_alarm_int = gps_tracking_alarm_int2;
    }

    public String getItem_id() {
        return this.item_id;
    }

    public void setItem_id(String item_id2) {
        this.item_id = item_id2;
    }

    public String getItem_number() {
        return this.item_number;
    }

    public void setItem_number(String item_number2) {
        this.item_number = item_number2;
    }

    public String getItem_img() {
        return this.item_img;
    }

    public void setItem_img(String item_img2) {
        this.item_img = item_img2;
    }

    public String getItem_km() {
        return this.item_km;
    }

    public void setItem_km(String item_km2) {
        this.item_km = item_km2;
    }

    public String getItem_date() {
        return this.item_date;
    }

    public void setItem_date(String item_date2) {
        this.item_date = item_date2;
    }

    public String getItem_time() {
        return this.item_time;
    }

    public void setItem_time(String item_time2) {
        this.item_time = item_time2;
    }

    public String getItem_location() {
        return this.item_location;
    }

    public void setItem_location(String item_location2) {
        this.item_location = item_location2;
    }

    public int getOil_electricity_alarm_int() {
        return this.oil_electricity_alarm_int;
    }

    public void setOil_electricity_alarm_int(int oil_electricity_alarm_int2) {
        this.oil_electricity_alarm_int = oil_electricity_alarm_int2;
    }

    public int getAc_alarm_int() {
        return this.ac_alarm_int;
    }

    public void setAc_alarm_int(int ac_alarm_int2) {
        this.ac_alarm_int = ac_alarm_int2;
    }

    public int getCharging_alarm_int() {
        return this.charging_alarm_int;
    }

    public void setCharging_alarm_int(int charging_alarm_int2) {
        this.charging_alarm_int = charging_alarm_int2;
    }

    public int getGps_tracking_alarm_int() {
        return this.gps_tracking_alarm_int;
    }

    public void setGps_tracking_alarm_int(int gps_tracking_alarm_int2) {
        this.gps_tracking_alarm_int = gps_tracking_alarm_int2;
    }
}
