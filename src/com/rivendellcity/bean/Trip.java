package com.rivendellcity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.GeoPt;

public class Trip implements Serializable {
	
	private String tripid;
	private String driverid;
	private GeoPt origin;
	private GeoPt destination;
	private String origin_string;
	private String destination_string;
	private Date time;
	private ArrayList<String> riderid;
	
	public String getTripid() {
		return tripid;
	}
	public void setTripid(String tripid) {
		this.tripid = tripid;
	}
	public String getDriverid() {
		return driverid;
	}
	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}
	public GeoPt getOrigin() {
		return origin;
	}
	public void setOrigin(GeoPt origin) {
		this.origin = origin;
	}
	public GeoPt getDestination() {
		return destination;
	}
	public void setDestination(GeoPt destination) {
		this.destination = destination;
	}
	
	public String getOrigin_string() {
		return origin_string;
	}
	public void setOrigin_string(String origin_string) {
		this.origin_string = origin_string;
	}
	public String getDestination_string() {
		return destination_string;
	}
	public void setDestination_string(String destination_string) {
		this.destination_string = destination_string;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public ArrayList<String> getRiderid() {
		return riderid;
	}
	public void setRiderid(ArrayList<String> riderid) {
		this.riderid = riderid;
	}
	
}
