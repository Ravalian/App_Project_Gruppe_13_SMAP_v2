package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailsDetailsFragment;
import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailsListFragment;
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.ArrayList;

public class VehicleDetailsActivity extends AppCompatActivity {

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
    private ArrayList<Logs> vehicledetails;
    private int selectedVDIndex;

    //
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_fragment_list);

        //get container views
        listContainer = (LinearLayout)findViewById(R.id.VD_list_container);
        detailsContainer = (LinearLayout)findViewById(R.id.VD_details_container);

        //determine orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            phoneMode = PhoneMode.PORTRAIT;
        } else {
            phoneMode = PhoneMode.LANDSCAPE;
        }

        if (savedInstanceState == null) {
            //no persisted state, start the app in list view mode and selected index = 0
            selectedVDIndex = 0;
            userMode = UserMode.LIST_VIEW;

            //initialize fragments
            vehicleDetailsList = new VehicleDetailsListFragment();
            vehicleDetailsDetails = new VehicleDetailsDetailsFragment();

            vehicleDetailsList.setVD(vehicledetails);
            vehicleDetailsDetails.setVD(vehicledetails.get(selectedVDIndex));

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

        btnBack = findViewById(R.id.btnVDBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });

        updateFragmentViewState(userMode);
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
            Logs selectedVD = vehicledetails.get(position);
            if (selectedVD != null) {
                selectedVDIndex = position;
                vehicleDetailsDetails.setVD(selectedVD);
            }
        }
        updateFragmentViewState(UserMode.DETAIL_VIEW);
    }

    //ved ikke om den skal bruges sådan her eller det er firebase
    public ArrayList<Logs> getVDList() { return vehicledetails; }

    public Logs getCurrentSelection() {
        if (vehicledetails != null) {
            return vehicledetails.get(selectedVDIndex);
        } else {
            return null;
        }
    }

    private void Back() {
        finish();
    }
}