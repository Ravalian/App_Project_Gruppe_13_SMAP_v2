package com.example.smap_app_project_grp_13_carlog.Models;

import java.util.Date;

public class LogsTest {

    private String name, logDescription;
    private Date date;
    private int time, distance;

    public LogsTest(String logName, Date datetime, int Distance, int Time, String LogDescription ) {
        name = logName;
        date = datetime;
        distance = Distance;
        time = Time;
        logDescription = LogDescription;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public int getDistance() { return distance; }

    public void setDistance(int distance) { this.distance = distance; }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }

    public String getLogDescription() { return logDescription; }

    public void setLogDescription(String logDescription) { this.logDescription = logDescription; }
}
