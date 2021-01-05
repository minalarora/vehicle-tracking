package com.immortal.vehicletracking.model;

public class Alert {
	private String alert_id;
	private String item_number;
	private String alert_img;
	private String alert_date;
	private String alert_time;
	private String alert_name;
	private String alert_type;
	private String alert_address;

	public Alert(String alert_id, String item_number, String alert_img, String alert_date, String alert_time, String alert_name,String alert_type, String alert_address) {
		this.alert_id = alert_id;
		this.item_number = item_number;
		this.alert_img = alert_img;
		this.alert_date = alert_date;
		this.alert_time = alert_time;
		this.alert_name = alert_name;
		this.alert_address = alert_address;
		this.alert_type=alert_type;
	}

	public String getAlert_id() {
		return alert_id;
	}

	public void setAlert_id(String alert_id) {
		this.alert_id = alert_id;
	}

	public String getItem_number() {
		return item_number;
	}

	public void setItem_number(String item_number) {
		this.item_number = item_number;
	}

	public String getAlert_img() {
		return alert_img;
	}

	public void setAlert_img(String alert_img) {
		this.alert_img = alert_img;
	}

	public String getAlert_date() {
		return alert_date;
	}

	public void setAlert_date(String alert_date) {
		this.alert_date = alert_date;
	}

	public String getAlert_time() {
		return alert_time;
	}

	public void setAlert_time(String alert_time) {
		this.alert_time = alert_time;
	}

	public String getAlert_name() {
		return alert_name;
	}

	public void setAlert_name(String alert_name) {
		this.alert_name = alert_name;
	}

	public String getAlert_address() {
		return alert_address;
	}

	public void setAlert_address(String alert_address) {
		this.alert_address = alert_address;
	}

	public String getAlert_type() {
		return alert_type;
	}

	public void setAlert_type(String alert_type) {
		this.alert_type = alert_type;
	}
}
