package com.immortal.vehicletracking.model;

public class DocumentModel {
    private String item_expire_date;
    private String item_id;
    private String item_img;
    private String item_issue_date;
    private String item_name;
    private String item_total_days;
    private String item_type;
    private String vehicle_id;

    public DocumentModel(String item_expire_date, String item_id, String item_img, String item_issue_date, String item_name,
                         String item_total_days, String item_type, String vehicle_id) {
        this.item_expire_date = item_expire_date;
        this.item_id = item_id;
        this.item_img = item_img;
        this.item_issue_date = item_issue_date;
        this.item_name = item_name;
        this.item_total_days = item_total_days;
        this.item_type = item_type;
        this.vehicle_id = vehicle_id;
    }

    public String getItem_expire_date() {
        return item_expire_date;
    }

    public void setItem_expire_date(String item_expire_date) {
        this.item_expire_date = item_expire_date;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getItem_issue_date() {
        return item_issue_date;
    }

    public void setItem_issue_date(String item_issue_date) {
        this.item_issue_date = item_issue_date;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_total_days() {
        return item_total_days;
    }

    public void setItem_total_days(String item_total_days) {
        this.item_total_days = item_total_days;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
}