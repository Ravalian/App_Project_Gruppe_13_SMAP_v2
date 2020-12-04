package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class RegisteredVehiclesActivityVM extends AndroidViewModel {
    LiveData<List<VehicleDataFirebase>> vehicles;
    private Repository repository;
    public RegisteredVehiclesActivityVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<VehicleDataFirebase>> getVehicles() {
        if (vehicles==null){
            vehicles = repository.getVehicles();
        }
        return vehicles;
    }

    public LiveData<List<VehicleDataFirebase>> getRegisteredVehicles(String uid) {
        if (vehicles==null){
            vehicles = repository.getRegisteredVehicles(uid);
        }
        return vehicles;
    }
}
