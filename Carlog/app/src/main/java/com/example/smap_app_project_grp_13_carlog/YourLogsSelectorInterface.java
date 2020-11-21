package com.example.smap_app_project_grp_13_carlog;

import com.example.smap_app_project_grp_13_carlog.Models.LogsTest;

import java.util.ArrayList;

public interface YourLogsSelectorInterface {

    public void onYourLogSelected(int position);
    public ArrayList<LogsTest> getYourLogsList();
    public LogsTest getCurrentSelection();
}
