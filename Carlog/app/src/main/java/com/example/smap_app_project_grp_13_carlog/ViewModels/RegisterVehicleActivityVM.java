package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

public class RegisterVehicleActivityVM extends AndroidViewModel {

    //repository
    private Repository repository;

    //Internal data
    private Application wmapp;

    public RegisterVehicleActivityVM(Application app){
        super(app);
        repository = new Repository(app);
        wmapp = app;
    }

    //Method to add vehicle to firebase
    public void addVehicletoFirebase(String VehicleVIN){
        repository.GetVehiclefromAPI(VehicleVIN);
    }
}
