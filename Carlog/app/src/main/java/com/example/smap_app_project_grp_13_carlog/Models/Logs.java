package com.example.smap_app_project_grp_13_carlog.Models;

import java.util.Date;

public class Logs {

    public String logID, logDescription, user, vehicle;
    public Date date;
    public int time, distance;

    public Logs() {

    }

    public Logs(String logid, Date datetime, int Distance, int Time, String LogDescription ) {
        logID = logid;
        date = datetime;
        distance = Distance;
        time = Time;
        logDescription = LogDescription;
    }

    public String getLogID() { return logID; }

    public void setLogID(String logID) { this.logID = logID; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public int getDistance() { return distance; }

    public void setDistance(int distance) { this.distance = distance; }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }

    public String getLogDescription() { return logDescription; }

    public void setLogDescription(String logDescription) { this.logDescription = logDescription; }

    public String getuser() { return user; }

    public void setuser(String user) { this.user = user; }

    public String getvehicle() { return vehicle; }

    public void setvehicle(String vehicle) { this.vehicle = vehicle; }
}
