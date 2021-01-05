package com.immortal.vehicletracking.model;

public class DistanceSummary {
	private String item_id;
	private String item_number;
	private String item_img;
	private String item_km;
	private String item_duration;

	public DistanceSummary(String item_number, String item_img, String item_duration, String item_km, String item_id) {
		this.item_number = item_number;
		this.item_img = item_img;
		this.item_duration = item_duration;
		this.item_km=item_km;
		this.item_id = item_id;
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


	public String getItem_duration() {
		return item_duration;
	}

	public void setItem_duration(String item_duration) {
		this.item_duration = item_duration;
	}

	public String getItem_km() {
		return item_km;
	}

	public void setItem_km(String item_km) {
		this.item_km = item_km;
	}
}
