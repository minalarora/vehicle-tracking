package com.immortal.vehicletracking.model;

public class Notification {
	private String notification_id;
	private String notification_number;
	private String notification_img;
	private String notification_date;
	private String notification_time;
	private String notification_name;
	private String notification_type;
	private String notification_address;

	public Notification(String notification_id, String notification_number, String notification_img, String notification_date,
						String notification_time, String notification_name, String notification_type, String notification_address) {
		this.notification_id = notification_id;
		this.notification_number = notification_number;
		this.notification_img = notification_img;
		this.notification_date = notification_date;
		this.notification_time = notification_time;
		this.notification_name = notification_name;
		this.notification_type = notification_type;
		this.notification_address = notification_address;
	}

	public String getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(String notification_id) {
		this.notification_id = notification_id;
	}

	public String getNotification_number() {
		return notification_number;
	}

	public void setNotification_number(String notification_number) {
		this.notification_number = notification_number;
	}

	public String getNotification_img() {
		return notification_img;
	}

	public void setNotification_img(String notification_img) {
		this.notification_img = notification_img;
	}

	public String getNotification_date() {
		return notification_date;
	}

	public void setNotification_date(String notification_date) {
		this.notification_date = notification_date;
	}

	public String getNotification_time() {
		return notification_time;
	}

	public void setNotification_time(String notification_time) {
		this.notification_time = notification_time;
	}

	public String getNotification_name() {
		return notification_name;
	}

	public void setNotification_name(String notification_name) {
		this.notification_name = notification_name;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}

	public String getNotification_address() {
		return notification_address;
	}

	public void setNotification_address(String notification_address) {
		this.notification_address = notification_address;
	}
}
