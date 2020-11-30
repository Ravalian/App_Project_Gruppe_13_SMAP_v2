package com.example.smap_app_project_grp_13_carlog.ViewModels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class YourVehiclesVM extends AndroidViewModel {

    LiveData<List<VehicleDataFirebase>> vehicles;
    private Repository repository;
    public YourVehiclesVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<VehicleDataFirebase>> getYourVehicles(String userID) {
        if (vehicles==null){
            vehicles = repository.getYourVehicles(userID);
        }
        return vehicles;

    }

}
