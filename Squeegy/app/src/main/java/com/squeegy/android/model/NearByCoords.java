package com.squeegy.android.model;


import org.json.JSONArray;
import org.json.JSONObject;

public class NearByCoords {

    private double latitude;
    private double longitude;

    public NearByCoords(JSONArray nearByCordsArray) {
        setLatitude(nearByCordsArray.optDouble(0));
        setLongitude(nearByCordsArray.optDouble(1));
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
