package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleLogFragment;
import com.example.smap_app_project_grp_13_carlog.Fragments.YourLogListFragment;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.YourLogsVM;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class YourLogsActivity extends AppCompatActivity implements VehicleDetailsSelectorInterface {



    public enum UserMode {LOG_VIEW, LIST_VIEW};

    private YourLogListFragment yourLogListFragment;
    private VehicleLogFragment vehicleLog;

    private LinearLayout listContainer;
    private LinearLayout logContainer;

    private List<Log> logList;
    private int selectedLog;

    private YourLogsVM vm;
    private String id;
    private Constants constants;
    private UserMode um;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_logs2);

        listContainer = (LinearLayout)findViewById(R.id.YourLogsContainer);
        logContainer = (LinearLayout)findViewById(R.id.LogDetailContainer);

        listContainer.setVisibility(View.VISIBLE);
        logContainer.setVisibility(View.GONE);

        if (yourLogListFragment==null) {
            yourLogListFragment = new YourLogListFragment();
        }
        if (vehicleLog==null) {
            vehicleLog = new VehicleLogFragment();
        }

        id = getIntent().getStringExtra(constants.ID);
        um = UserMode.LIST_VIEW;
        selectedLog = 0;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.YourLogsContainer, yourLogListFragment, "List_fragment")
                .add(R.id.LogDetailContainer, vehicleLog, "Detail_fragment")
                .commit();
        vm = new ViewModelProvider(this).get(YourLogsVM.class);
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        vm.getLogs(id).observe(this, new Observer<List<Log>>() {
            @Override
            public void onChanged(List<Log> logs) {
                logList = logs;
                yourLogListFragment.setLogs(logList);
                vehicleLog.setLog(logList.get(selectedLog));

                updateFragmentState(um);
            }
        });
    }

    private void updateFragmentState(UserMode tm) {
        if(tm == UserMode.LIST_VIEW) {
            um = UserMode.LIST_VIEW;
            yourLogListFragment.update();
            switchFragment(tm);
        } if(tm == UserMode.LOG_VIEW) {
            um = UserMode.LOG_VIEW;
            switchFragment(tm);
        } else {
            //ignore
        }
    }

    private void switchFragment(UserMode tm) {
        if (tm == UserMode.LIST_VIEW){
            listContainer.setVisibility(View.VISIBLE);
            logContainer.setVisibility(View.GONE);
            changeContainerFragment(UserMode.LIST_VIEW);
        } else if (tm == UserMode.LOG_VIEW){
            listContainer.setVisibility(View.GONE);
            logContainer.setVisibility(View.VISIBLE);
            changeContainerFragment(UserMode.LOG_VIEW);
        }
    }

    private void changeContainerFragment(UserMode targetMode) {
        switch(targetMode) {

            case LOG_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in, R.animator.slide_out)
                        .replace(R.id.LogDetailContainer, vehicleLog, "Detail_fragment")
                        .commit();
                vehicleLog.UpdateUI();
                break;
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

    @Override
    public void back() {
        onBackPressed();

    }

    @Override
    public void onBackPressed(){
        if (um == UserMode.LOG_VIEW) {
            updateFragmentState(UserMode.LIST_VIEW);
        } else {
            this.finish();
        }
    }
}