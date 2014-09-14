package com.rivendellcity.bean;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
	private String category;
	private String place;
	private Date time;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
