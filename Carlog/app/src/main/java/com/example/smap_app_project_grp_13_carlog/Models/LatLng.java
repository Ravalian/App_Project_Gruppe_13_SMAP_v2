package com.example.smap_app_project_grp_13_carlog.Models;

public class LatLng {

    public LatLng(){

    }

    public LatLng(Double Lat, Double Long){
        this.latitude = Lat;
        this.longitude = Long;
    }

    public double latitude;
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
