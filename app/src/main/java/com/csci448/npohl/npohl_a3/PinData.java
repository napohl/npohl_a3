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
    private double mTemp;
    private String mCondition;
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

    public double getTemp() {
        return mTemp;
    }

    /**
     * This function assumes the input is in kelvin, and will convert to Fahrenheit
     * This is because the weather api we are using returns the temperature in Kelvin
     *
     * @param temp temperature in Kelvin
     */
    public void setTemp(double temp) {
        //convert from K to F
        mTemp = (temp * 9/5) - 459.6;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String condition) {
        mCondition = condition;
    }

    public String getSnackBarString() {
        String s = "You were here:" + mDate.toString() + "\nTemp: " + String.valueOf(mTemp) + "F(" + mCondition + ")";
        return s;
    }
}
