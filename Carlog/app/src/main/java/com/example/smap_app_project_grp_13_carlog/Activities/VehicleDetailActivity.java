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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

//Inspired deeply from SMAP E20 L7 - Kasper Løvborg

public class VehicleDetailActivity extends AppCompatActivity implements VehicleDetailsSelectorInterface {

    //Enum to keep track of which fragment is in use
    public enum UserMode {LOG_VIEW, LIST_VIEW};

    //Fragments
    private VehicleDetailFragment vehicleLogList;
    private VehicleLogFragment vehicleLog;

    //Container to hold fragments
    private LinearLayout listContainer;

    //Local variables
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
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);

        //Initialize local variables
        id = getIntent().getStringExtra(constants.ID);
        um = UserMode.LIST_VIEW;
        selectedLog = 0;

        //Initialize container
        listContainer = (LinearLayout)findViewById(R.id.ListContainer);
        listContainer.setVisibility(View.VISIBLE);

        //Initialize fragments
        if (vehicleLogList==null) {
            vehicleLogList = new VehicleDetailFragment();
        }
        if (vehicleLog==null) {
            vehicleLog = new VehicleLogFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ListContainer, vehicleLog, constants.FRAG_LOG)
                .replace(R.id.ListContainer, vehicleLogList, constants.FRAG_LIST)
                .commit();

        //Setup for LiveData of the current vehicle and its logs
        vm = new ViewModelProvider(this).get(VehicleDetailsVM.class);
        vm.getVehicle(id).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
                vehicleLogList.setVehicle(vehicleDataFirebase);
                updateFragmentState(um);
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

        //Switch to List fragment if in Log fragment else go back with a custom animation
        if (um==UserMode.LOG_VIEW) {
            updateFragmentState(UserMode.LIST_VIEW);
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        }
    }


    @Override
    public void onVehicleDetailsSelected(int position) {

        //Go to Log fragment if a log has been chosen and the logs have been loaded
        if (logList!=null) {
            Log log = logList.get(position);
            if (log!=null) {
                selectedLog = position;
                vehicleLog.setLog(log);
            }
        }
        if (um != UserMode.LOG_VIEW) {
            updateFragmentState(UserMode.LOG_VIEW);
        }
    }

    private void updateFragmentState(UserMode tm) {

        //Switch usermode
       if (tm!=UserMode.LIST_VIEW&&tm!=UserMode.LOG_VIEW){
            //ignore
            return;
        }
        um = tm;
        switchFragment(tm);

    }

    private void switchFragment(UserMode tm) {

        //Update List fragment if going to List fragment
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

        //Swap fragments
        switch(targetMode) {

            case LOG_VIEW:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.ListContainer, vehicleLog, constants.FRAG_LOG)
                        .commit();
                break;

            case LIST_VIEW:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.slide_out, R.anim.slide_in, R.anim.fade_out)
                        .replace(R.id.ListContainer, vehicleLogList, constants.FRAG_LIST)
                        .commit();
                break;
        }
    }
}