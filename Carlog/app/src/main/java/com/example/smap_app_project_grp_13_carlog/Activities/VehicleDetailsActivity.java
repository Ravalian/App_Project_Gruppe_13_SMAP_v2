package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailsDetailsFragment;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailsListFragment;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleDetailsVM;

import java.util.ArrayList;
import java.util.List;

public class VehicleDetailsActivity extends AppCompatActivity implements VehicleDetailsSelectorInterface {

    //keeping track of phone mode (portrait or landscape) and user mode (which view the user has selected)
    public enum PhoneMode {PORTRAIT, LANDSCAPE}
    public enum UserMode {LIST_VIEW, DETAIL_VIEW}
    private VehicleDetailsActivity.PhoneMode phoneMode;
    private VehicleDetailsActivity.UserMode userMode;

    //tags -TOBE moved to Constants.java
    private static final String LIST_FRAG = "list_fragment";
    private static final String DETAILS_FRAG = "details_fragment";

    //the actual fragments we use
    private VehicleDetailsListFragment vehicleDetailsList;
    private VehicleDetailsDetailsFragment vehicleDetailsDetails;

    //containers we use to put out fragments in
    private LinearLayout listContainer;
    private LinearLayout detailsContainer;

    //list of vehicles
    private List<Logs> logs;
    private VehicleDataFirebase vehicle;
    private int selectedVDIndex;

    //Views
    private Button btnBack, btnNew;
    private VehicleDetailsVM vm;
    private String id;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_fragment_list);

        Intent data = getIntent();
        id = data.getStringExtra(constants.ID);
        vm = new ViewModelProvider(this).get(VehicleDetailsVM.class);
        vm.fetch(id);

        //get container views
        listContainer = (LinearLayout)findViewById(R.id.VD_list_container);
        detailsContainer = (LinearLayout)findViewById(R.id.VD_details_container);

        //determine orientation
        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            phoneMode = PhoneMode.PORTRAIT;
        } else {
            phoneMode = PhoneMode.LANDSCAPE;
        }*/


        vm.getVehicle(id).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
                Log.d("Tester", vehicle.getRegistrationNumber());
                //vehicleDetailsList.setVD((ArrayList<Logs>) logs, vehicle);
            }
        });
        vm.getLogs(id).observe(this, new Observer<List<Logs>>() {
            @Override
            public void onChanged(List<Logs> logs) {

                logs = logs;

                if (savedInstanceState == null) {
                    //no persisted state, start the app in list view mode and selected index = 0
                    selectedVDIndex = 0;
                    userMode = UserMode.LIST_VIEW;

                    //initialize fragments
                    vehicleDetailsList = new VehicleDetailsListFragment();
                    vehicleDetailsDetails = new VehicleDetailsDetailsFragment();

                    vehicleDetailsList.setVD((ArrayList<Logs>) logs, vehicle);
                    vehicleDetailsDetails.setVD(logs.get(selectedVDIndex));

                    //add the first two fragments to container (one will be invisible if in portrait mode)
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.VD_list_container, vehicleDetailsList, LIST_FRAG)
                            .add(R.id.VD_details_container, vehicleDetailsDetails, DETAILS_FRAG)
                            .commit();
                } else {
                    //got restarted with persisted state, probably due to orientation change
                    selectedVDIndex = savedInstanceState.getInt("Vehicle_Details_position");
                    userMode = (UserMode) savedInstanceState.getSerializable("User_mode");

                    if (userMode == null) {
                        userMode = UserMode.LIST_VIEW; //default value if none saved
                    }

                    ////check if FragmentManager already holds instance of Fragments, else create them
                    vehicleDetailsList = (VehicleDetailsListFragment)getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
                    if (vehicleDetailsList == null) {
                        vehicleDetailsList = new VehicleDetailsListFragment();
                    }
                    vehicleDetailsDetails = (VehicleDetailsDetailsFragment)getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG);
                    if (vehicleDetailsDetails == null) {
                        vehicleDetailsDetails = new VehicleDetailsDetailsFragment();
                    }
                }

            }
        });



        btnBack = findViewById(R.id.btnVDBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });

        btnNew = findViewById(R.id.btnStart);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewLog();
            }
        });
        updateFragmentViewState(userMode);
    }

    private void gotoNewLog() {
        Intent intent = new Intent(this, VehicleLogActivity.class);
        intent.putExtra(constants.ID, id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (phoneMode == PhoneMode.LANDSCAPE) {
            finish();
        } else {
            if (userMode == UserMode.LIST_VIEW) {
                //return to reives activity
                finish();
            } else if (userMode == UserMode.DETAIL_VIEW) {
                //go back to list mode if in detail mode
                updateFragmentViewState(UserMode.LIST_VIEW);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("VD_position", selectedVDIndex);
        outState.putSerializable("user_mode", userMode);
        super.onSaveInstanceState(outState);
    }

    private void updateFragmentViewState(UserMode targetMode) {
        //update view
        if (targetMode == UserMode.DETAIL_VIEW) {
            userMode = UserMode.DETAIL_VIEW;
            switchFragment(targetMode);
        } if (targetMode == UserMode.LIST_VIEW) {
            userMode = UserMode.LIST_VIEW;
            switchFragment(targetMode);
        }
    }

    private boolean switchFragment(UserMode targetMode) {
        if (phoneMode == PhoneMode.LANDSCAPE.PORTRAIT) {
            if (targetMode == UserMode.LIST_VIEW) {
                listContainer.setVisibility(View.VISIBLE);
                detailsContainer.setVisibility(View.GONE);
            } else if (targetMode == UserMode.DETAIL_VIEW) {
                listContainer.setVisibility(View.GONE);
                detailsContainer.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

    public void onVehicleDetailsSelected(int position) {
        if (vehicleDetailsDetails != null) {
            Logs selectedVD = logs.get(position);
            if (selectedVD != null) {
                selectedVDIndex = position;
                vehicleDetailsDetails.setVD(selectedVD);
            }
        }
        updateFragmentViewState(UserMode.DETAIL_VIEW);
    }

    @Override
    public ArrayList<Logs> getVehicleDetailsList() {
        return (ArrayList) logs;
    }

    //ved ikke om den skal bruges sådan her eller det er firebase
    public ArrayList<Logs> getVDList() { return (ArrayList<Logs>) logs; }

    public Logs getCurrentSelection() {
        if (logs != null) {
            return logs.get(selectedVDIndex);
        } else {
            return null;
        }
    }

    @Override
    public void back() {

    }

    private void Back() {
        finish();
    }
}