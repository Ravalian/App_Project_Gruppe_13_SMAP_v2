package com.example.smap_app_project_grp_13_carlog.Models;

public class VehicleDataFirebase {
    public String Model;
    public String Manufacturer;
    public String LicensePlate;

    public VehicleDataFirebase(){
        //Default constructor (required) (seen in "Demo 2" in lecture about firebase
    }

    public VehicleDataFirebase(String model, String manufacturer, String licensePlate){
        this.Model = model;
        this.Manufacturer = manufacturer;
        this.LicensePlate = licensePlate;
    }
}
