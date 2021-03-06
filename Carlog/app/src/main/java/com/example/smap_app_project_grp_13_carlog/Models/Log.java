package com.example.smap_app_project_grp_13_carlog.Models;

import java.util.ArrayList;

public class Log {

    public Log() {

    }

    public Long date;
    public String logID;
    public String logDescription;
    public String user;
    public String vehicle;
    public int time, distance;
    public String userName;
    public ArrayList<LatLng> positions;
    public String vehicleOwner;

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public String getuser() {
        return user;
    }

    public void setuser(String user) {
        this.user = user;
    }

    public String getvehicle() {
        return vehicle;
    }

    public void setvehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String username){
        this.userName = username;
    }

    public ArrayList<LatLng> getPositions(){
        return positions;
    }

    public void setPositions(ArrayList<LatLng> positions){
        this.positions = positions;
    }

    public String getVehicleOwner(){
        return this.vehicleOwner;
    }

    public void setVehicleOwner(String id){
        this.vehicleOwner = id;
    }

}