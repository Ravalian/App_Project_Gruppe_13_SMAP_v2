package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Activities.VehicleDetailActivity;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class VehicleDetailsVM extends AndroidViewModel {
    LiveData<List<Log>> logs;
    LiveData<VehicleDataFirebase> vehicle;
    Repository repository;
    private VehicleDetailActivity.UserMode um;
    private int selectedLog;

    public VehicleDetailsVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        selectedLog = 0;
    }

    public LiveData<List<Log>> getLogs(String id) {
        if (logs==null){
            logs = repository.getLogs(id);
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

    public int getSelectedLog() {
        return selectedLog;
    }

    public void setSelectedLog(int position) {
        selectedLog = position;
    }

    public VehicleDetailActivity.UserMode getUM() {
        return um;
    }

    public void setUM(VehicleDetailActivity.UserMode um) {
        this.um = um;
    }
}
