package com.example.smap_app_project_grp_13_carlog.Interface;

import com.example.smap_app_project_grp_13_carlog.Models.Logs;

import java.util.ArrayList;

public interface VehicleDetailsSelectorInterface {

    public void onVehicleDetailsSelected(int position);
    public ArrayList<Logs> getVehicleDetailsList();
    public Logs getCurrentSelection();
}
