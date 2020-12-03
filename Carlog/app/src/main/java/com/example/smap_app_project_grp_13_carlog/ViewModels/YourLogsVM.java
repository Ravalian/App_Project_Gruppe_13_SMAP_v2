package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;

import java.util.List;

public class YourLogsVM extends AndroidViewModel {
    LiveData<List<Log>> logs;
    Repository repository;

    public YourLogsVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<Log>> getLogs(String id) {
        if (logs==null){
            logs = repository.getYourLogs(id);
        }
        return logs;
    }
}
