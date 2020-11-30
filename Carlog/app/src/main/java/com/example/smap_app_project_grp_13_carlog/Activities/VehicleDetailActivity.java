package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailFragment;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleLogFragment;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleDetailsVM;
import com.firebase.ui.auth.data.model.User;

import java.util.ArrayList;
import java.util.List;

//Inspired deeply from SMAP E20 L7 - Kasper Løvborg

public class VehicleDetailActivity extends AppCompatActivity implements VehicleDetailsSelectorInterface {

    public enum UserMode {LOG_VIEW, LIST_VIEW};

    private VehicleDetailFragment vehicleLogList;
    private VehicleLogFragment vehicleLog;

    private LinearLayout listContainer;
    private LinearLayout logContainer;

    private List<Log> logList;
    private int selectedLog;
    private VehicleDataFirebase vehicle;
    private VehicleDetailsVM vm;
    private String id;
    private Constants constants;
    private UserMode um;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        listContainer = (LinearLayout)findViewById(R.id.ListContainer);
        logContainer = (LinearLayout)findViewById(R.id.LogContainer);

        listContainer.setVisibility(View.VISIBLE);
        logContainer.setVisibility(View.GONE);

        if (vehicleLogList==null) {
            vehicleLogList = new VehicleDetailFragment();
        }
        if (vehicleLog==null) {
            vehicleLog = new VehicleLogFragment();
        }

        id = getIntent().getStringExtra(constants.ID);
        um = UserMode.LIST_VIEW;
        selectedLog = 0;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.ListContainer, vehicleLogList, "list_fragment")
                .add(R.id.LogContainer, vehicleLog, "log_fragment")
                .commit();

        vm = new ViewModelProvider(this).get(VehicleDetailsVM.class);
        vm.getVehicle(id).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
                vehicleLogList.setVehicle(vehicleDataFirebase);



            }
        });
        vm.getLogs(id).observe(this, new Observer<List<Log>>() {
            @Override
            public void onChanged(List<Log> logs) {
                logList = logs;
                vehicleLogList.setLogs(logList);
                vehicleLog.setLog(logList.get(selectedLog));

                updateFragmentState(um);
            }
        });
    }

    public void back(){
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        if (um==UserMode.LOG_VIEW) {
            updateFragmentState(UserMode.LIST_VIEW);
        } else {
            this.finish();
        }
    }

    private void updateFragmentState(UserMode tm) {
        if(tm == UserMode.LIST_VIEW) {
            um = UserMode.LIST_VIEW;
            vehicleLogList.update();
            switchFragment(tm);
        } if(tm == UserMode.LOG_VIEW) {
            um = UserMode.LOG_VIEW;
            switchFragment(tm);
        } else {
            //ignore
        }

    }

    @Override
    public void onVehicleDetailsSelected(int position) {
        if (logList!=null) {
            Log log = logList.get(position);
            if (log!=null) {
                selectedLog = position;
                vehicleLog.setLog(log);
            }
        }
        updateFragmentState(UserMode.LOG_VIEW);
    }

    private void switchFragment(UserMode um) {
        if (um==UserMode.LIST_VIEW){
            listContainer.setVisibility(View.VISIBLE);
            logContainer.setVisibility(View.GONE);
            changeContainerFragment(UserMode.LIST_VIEW);
        } else if (um==UserMode.LOG_VIEW){
            listContainer.setVisibility(View.GONE);
            logContainer.setVisibility(View.VISIBLE);
            changeContainerFragment(UserMode.LOG_VIEW);
        }
    }

    @Override
    public ArrayList<Log> getVehicleDetailsList() {
        return (ArrayList<Log>) logList;
    }

    @Override
    public Log getCurrentSelection() {
        if (logList!=null) {
            return logList.get(selectedLog);
        } else {
            return null;
        }
    }

    private void changeContainerFragment(UserMode targetMode){
        switch(targetMode) {
            /*case DETAIL_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.DetailContainer, vehicleDetails, "detail_fragment")
                        .commit();

                break;*/
            case LOG_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in, R.animator.slide_out)
                        .replace(R.id.LogContainer, vehicleLog, "log_fragment")
                        .commit();
                vehicleLog.UpdateUI();
                break;
        }
    }

}