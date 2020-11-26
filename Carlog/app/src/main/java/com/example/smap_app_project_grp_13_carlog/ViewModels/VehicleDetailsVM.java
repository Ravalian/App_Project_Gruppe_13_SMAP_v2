package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class VehicleDetailsVM extends AndroidViewModel {
    LiveData<List<Logs>> logs;
    LiveData<VehicleDataFirebase> vehicle;
    Repository repository;
    //VehicleDataFirebase vehicle;

    public VehicleDetailsVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        //repository.setupFirebaseLogsListener(id);
    }

    public LiveData<List<Logs>> getLogs() {
        if (logs==null){
            logs = repository.getLogs();
        }
        return logs;
    }

    public void fetch(String id) {
        repository.setupFirebaseLogsListener(id);
    }

    public LiveData<VehicleDataFirebase> getVehicle(String id) {
        if (vehicle==null){
            vehicle=repository.getvehicle(id);
        }
        return vehicle;
    }
}
