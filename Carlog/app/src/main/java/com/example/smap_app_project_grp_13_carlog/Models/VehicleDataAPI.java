package com.example.smap_app_project_grp_13_carlog.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Class is created with the online resource: http://www.jsonschema2pojo.org/
public class VehicleDataAPI {

    @SerializedName("registration_number")
    @Expose
    private String registrationNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_date")
    @Expose
    private String statusDate;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("use")
    @Expose
    private String use;
    @SerializedName("first_registration")
    @Expose
    private Object firstRegistration;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("own_weight")
    @Expose
    private String ownWeight;
    @SerializedName("total_weight")
    @Expose
    private String totalWeight;
    @SerializedName("axels")
    @Expose
    private String axels;
    @SerializedName("pulling_axels")
    @Expose
    private Object pullingAxels;
    @SerializedName("seats")
    @Expose
    private String seats;
    @SerializedName("coupling")
    @Expose
    private String coupling;
    @SerializedName("doors")
    @Expose
    private String doors;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("model_type")
    @Expose
    private String modelType;
    @SerializedName("model_year")
    @Expose
    private String modelYear;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("chassis_type")
    @Expose
    private String chassisType;
    @SerializedName("engine_cylinders")
    @Expose
    private String engineCylinders;
    @SerializedName("engine_volume")
    @Expose
    private String engineVolume;
    @SerializedName("engine_power")
    @Expose
    private String enginePower;
    @SerializedName("fuel_type")
    @Expose
    private String fuelType;
    @SerializedName("registration_zipcode")
    @Expose
    private Object registrationZipcode;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public Object getFirstRegistration() {
        return firstRegistration;
    }

    public void setFirstRegistration(Object firstRegistration) {
        this.firstRegistration = firstRegistration;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getOwnWeight() {
        return ownWeight;
    }

    public void setOwnWeight(String ownWeight) {
        this.ownWeight = ownWeight;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getAxels() {
        return axels;
    }

    public void setAxels(String axels) {
        this.axels = axels;
    }

    public Object getPullingAxels() {
        return pullingAxels;
    }

    public void setPullingAxels(Object pullingAxels) {
        this.pullingAxels = pullingAxels;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCoupling() {
        return coupling;
    }

    public void setCoupling(String coupling) {
        this.coupling = coupling;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChassisType() {
        return chassisType;
    }

    public void setChassisType(String chassisType) {
        this.chassisType = chassisType;
    }

    public String getEngineCylinders() {
        return engineCylinders;
    }

    public void setEngineCylinders(String engineCylinders) {
        this.engineCylinders = engineCylinders;
    }

    public String getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(String engineVolume) {
        this.engineVolume = engineVolume;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Object getRegistrationZipcode() {
        return registrationZipcode;
    }

    public void setRegistrationZipcode(Object registrationZipcode) {
        this.registrationZipcode = registrationZipcode;
    }
}
