package com.example.model;

import java.io.Serializable;

public class Address_data implements Serializable{
    private double longitude;
    private double  latitude;

    public Address_data(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address_data() {
    	this.latitude = 0.0;
    	this.longitude = 0.0;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
