package com.csci448.npohl.npohl_a3;


import java.util.Date;

/**
 * This class will be used to hold data for pins we place on the map
 *
 * This data includes the latitude, longitude, date, temperature, and weather
 */
public class PinData {

    private double mLat;
    private double mLon;
    private Date mDate;
    // TODO: 4/10/17 get weather data


    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double mLon) {
        this.mLon = mLon;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getSnackBarString() {
        String s = "You were here:" + mDate.toString();
        // TODO: 4/10/17 add weather data to its own line
        return s;
    }
}
