package com.rivendellcity.bean;

import java.io.Serializable;

public class RequestRide implements Serializable {
	
	private String riderid;
	private String tripid;
	private String driverid;
	private boolean isAccepted;
	
	public String getRiderid() {
		return riderid;
	}
	public void setRiderid(String riderid) {
		this.riderid = riderid;
	}
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
	public boolean isAccepted() {
		return isAccepted;
	}
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
}
