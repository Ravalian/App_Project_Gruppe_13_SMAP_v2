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

import com.example.smap_app_project_grp_13_carlog.Adapters.RegisteredVehiclesAdapter;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;

import com.example.smap_app_project_grp_13_carlog.ViewModels.RegisteredVehiclesActivityVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RegisteredVehiclesActivity extends AppCompatActivity implements RegisteredVehiclesAdapter.IRegisteredVehiclesItemClickedListener {

    //widgets
    private RecyclerView rcvList;
    private Button btnBack;
    private List<VehicleDataFirebase> vehicles;
    private RegisteredVehiclesAdapter adapter;
    private Constants constants;
    private RegisteredVehiclesActivityVM vm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_vehicles);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);                //Inspired by https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/




        //Setup for LiveData of the registered vehicles
        vm = new ViewModelProvider(this).get(RegisteredVehiclesActivityVM.class);
        vm.getVehicles().observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebases) {

                //Setup UI if there are no adapter
                if (adapter==null){
                    setupUI(vehicleDataFirebases);
                }

                //Update data
                adapter.updateVehicles(vehicleDataFirebases);
                vehicles = vehicleDataFirebases;
            }
        });
    }


    private void setupUI(List<VehicleDataFirebase> vehicleDataFirebases) {

        //set up recyclerview with adapter and layout manager
        adapter = new RegisteredVehiclesAdapter(vehicleDataFirebases, this);
        rcvList = findViewById(R.id.rcvRV);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        //Setup for back button
        btnBack = findViewById(R.id.btnRVBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
    }

    private void Back() {

        //Go back with a custom animation
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    @Override
    public void onRegisteredVehicleClicked(int position) {

        //Go to VehicleDetail activity with the chosen vehicle ID
        Intent intent = new Intent(this, VehicleDetailActivity.class);
        intent.putExtra(constants.ID, vehicles.get(position).getRegistrationNumber());
        startActivity(intent);
    }

}