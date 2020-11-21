package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

public class VehicleLogVM extends AndroidViewModel {

    LiveData<VehicleDataFirebase> vehicle;
    Repository repository;

    public VehicleLogVM(@NonNull Application application) {
        super(application);
    }

    public LiveData<VehicleDataFirebase> getVehicle(String id) {
        if (vehicle==null){
            //repository.getvehicle(id);
        }
        return vehicle;
    }
}
