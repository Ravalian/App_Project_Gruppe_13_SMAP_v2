package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.UserRTDB;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class AddUserVM extends AndroidViewModel {

    LiveData<VehicleDataFirebase> vehicle;
    LiveData<List<UserRTDB>> users;
    Repository repository;

    public AddUserVM(@NonNull Application application) {
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
    public LiveData<List<UserRTDB>> getUsers(){
        if (users == null){

        }
        return users;
    }
}
