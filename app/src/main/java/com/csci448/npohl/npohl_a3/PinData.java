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
    //we set temperature to be an int, because it looks ugly on the snackbar as a double
    private int mTemp;
    //the weather condition is all we care about keeping from the weather details
    private String mCondition;


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

    public int getTemp() {
        return mTemp;
    }


    public void setTemp(int temp) {
        mTemp = temp;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String condition) {
        mCondition = condition;
    }

    /**
     * This is a helper function that is used to create a standardized string format for the snackbar
     *
     * @return returns the message that the snackbar will use
     */
    public String getSnackBarString() {
        String s = "You were here: " + mDate.toString() + "\nTemp: " + String.valueOf(mTemp) + "F (" + mCondition + ")";
        return s;
    }
}
