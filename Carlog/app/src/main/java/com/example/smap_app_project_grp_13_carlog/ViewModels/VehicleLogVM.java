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

    LiveData<List<VehicleDataFirebase>> vehicle;
    Repository repository;

    public VehicleLogVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<VehicleDataFirebase>> getVehicle() {
        if (vehicle==null){
            vehicle = repository.getVehicles();
        }
        return vehicle;
    }

    public void saveLog(Log log) {
        repository.saveLog(log);
    }
}
