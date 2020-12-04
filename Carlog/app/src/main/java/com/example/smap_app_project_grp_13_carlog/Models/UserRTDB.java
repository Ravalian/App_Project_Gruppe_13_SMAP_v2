package com.example.smap_app_project_grp_13_carlog.Models;

import java.util.List;

public class UserRTDB {

    //Public constructor
    public UserRTDB(){
        //empty default constructor
    }

    public UserRTDB(String uid, String name){
        this.userId = uid;
        this.userName = name;
        //this.userEmail = email; //Database cannot store "." so email is a no go
    }

    //Values
    public String userId;
    public String userName;
    //public List<String> vehicleID;
    //public String userEmail; //Database cannot store "." so email is a no go

    //Get/set methods
    public String getUserId(){
        return userId;
    }

    public void setUserId(String uid){
        this.userId = uid;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String name){
        this.userName = name;
    }

}
