package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailFragment;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleDetailsVM;

import java.util.ArrayList;
import java.util.List;

public class VehicleDetailActivity extends AppCompatActivity implements VehicleDetailsSelectorInterface {

    private VehicleDetailFragment vehicleDetails;

    private LinearLayout listContainer;

    private List<Log> logList;
    private int selectedLog;
    private VehicleDataFirebase vehicle;
    private VehicleDetailsVM vm;
    private String id;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        listContainer = (LinearLayout)findViewById(R.id.DetailContainer);

        Intent data = getIntent();
        id = data.getStringExtra(constants.ID);

        vehicleDetails = new VehicleDetailFragment();

        vm = new ViewModelProvider(this).get(VehicleDetailsVM.class);
        vm.getVehicle(id).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
                vehicleDetails.setVehicle(vehicleDataFirebase);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.DetailContainer, vehicleDetails, "detail_fragment")
                        .commit();
            }
        });
        vm.getLogs(id).observe(this, new Observer<List<Log>>() {
            @Override
            public void onChanged(List<Log> logs) {
                logList = logs;
                vehicleDetails.setLogs(logList);

                updateFragmentState();

            }
        });
    }

    public void back(){
        finish();
    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void updateFragmentState() {
        vehicleDetails.update();
    }

    @Override
    public void onVehicleDetailsSelected(int position) {
        selectedLog = position;
    }

    @Override
    public ArrayList<Log> getVehicleDetailsList() {
        return (ArrayList<Log>) logList;
    }

    @Override
    public Log getCurrentSelection() {
        return logList.get(selectedLog);
    }
}