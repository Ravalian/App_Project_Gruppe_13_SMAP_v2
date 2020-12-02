package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class VehicleLogVM extends AndroidViewModel {

    LiveData<VehicleDataFirebase> vehicle;
    Repository repository;

    public VehicleLogVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    //Retrieve the current vehicle
    public LiveData<VehicleDataFirebase> getVehicle(String ID) {
        if (vehicle==null){
            vehicle = repository.getvehicle(ID);
        }
        return vehicle;
    }

    //Save the log
    public void saveLog(Log log) {
        repository.saveLog(log);
    }
}
