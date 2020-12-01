package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
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
        logContainer = findViewById(R.id.LogContainer);

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
                .add(R.id.ListContainer, vehicleLog, "log_fragment")
                .replace(R.id.ListContainer, vehicleLogList, "list_fragment")
                .commit();

        listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFragmentState(UserMode.LIST_VIEW);
            }
        });
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
            finish();
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
        //vehicleLog.UpdateUI();
    }

    private void updateFragmentState(UserMode tm) {
       if (tm!=UserMode.LIST_VIEW&&tm!=UserMode.LOG_VIEW){
            //ignore
            return;
        }
        um = tm;
        switchFragment(tm);

    }

    private void switchFragment(UserMode tm) {
        if (tm==UserMode.LIST_VIEW){
            vehicleLogList.update();
        }
        changeContainerFragment(tm);
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
            case LOG_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.ListContainer, vehicleLog, "log_fragment")
                        .commit();
                //vehicleLog.UpdateUI();
                break;
            case LIST_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.slide_out, R.anim.slide_in, R.anim.fade_out)
                        .replace(R.id.ListContainer, vehicleLogList, "list_fragment")
                        .commit();
                break;
        }
    }

}