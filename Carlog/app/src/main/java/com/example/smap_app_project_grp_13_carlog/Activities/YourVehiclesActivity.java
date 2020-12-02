package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Adapters.YourVehiclesAdapter;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.YourVehiclesVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class YourVehiclesActivity extends AppCompatActivity implements YourVehiclesAdapter.IYourVehiclesItemClickedListener {

    //widgets
    private YourVehiclesVM vm;
    private RecyclerView rcvList;
    private YourVehiclesAdapter adapter;
    private Button btnBack;
    private TextView txtYourVehicles;
    private Constants constants;
    private List<VehicleDataFirebase> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_vehicles);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);

        //Setup viewmodel and get data
        vm = new ViewModelProvider(this).get(YourVehiclesVM.class);
        vm.getYourVehicles(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebases) {
                //Setup UI if there are no adapter
                if (adapter==null){
                    Log.d(constants.REGISTEREDTAG, vehicleDataFirebases.get(0).getRegistrationNumber());
                    setupUI(vehicleDataFirebases);
                }
                Log.d(constants.REGISTEREDTAG, vehicleDataFirebases.get(0).getRegistrationNumber());
                //Update data
                adapter.updateVehicles(vehicleDataFirebases);
                vehicles = vehicleDataFirebases;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(constants.REGISTEREDTAG, user.getEmail());
        }
        //init ui
    }

    private void setupUI(List<VehicleDataFirebase> vehicleDataFirebases) {
        //set up recyclerview with adapter and layout manager
        adapter = new YourVehiclesAdapter(vehicleDataFirebases, this);
        rcvList = findViewById(R.id.rcvYourVehicles);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);


        txtYourVehicles = findViewById(R.id.txtYourVehicles);

        btnBack = findViewById(R.id.btnYVBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
    }

    private void Back() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    @Override
    public void onYourVehiclesClicked(int YVID) {
        Intent intent = new Intent(this, VehicleDetailActivity.class);
        intent.putExtra(constants.ID, vehicles.get(YVID).registrationNumber);
        startActivity(intent);
    }
}