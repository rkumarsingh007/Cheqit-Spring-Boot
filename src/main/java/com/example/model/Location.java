package com.example.model;

import java.sql.Date;

public class Location {

	private long date;
	private long altitude;
	private long longitude;
	
	
	public Location() {
		super();
	}
	public Location(long date, long altitude, long longitude) {
		super();
		this.date = date;
		this.altitude = altitude;
		this.longitude = longitude;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public long getAltitude() {
		return altitude;
	}
	public void setAltitude(long altitude) {
		this.altitude = altitude;
	}
	public long getLongitude() {
		return longitude;
	}
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	
	
}
