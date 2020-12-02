package com.example.smap_app_project_grp_13_carlog.Interface;

import com.example.smap_app_project_grp_13_carlog.Models.Log;

import java.util.ArrayList;

public interface YourLogsSelectorInterface {

    public void onYourLogSelected(int position);
    public ArrayList<Log> getYourLogsList();
    public Log getCurrentSelection();
}
