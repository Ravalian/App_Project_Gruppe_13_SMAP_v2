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

import java.util.ArrayList;
import java.util.List;

public class RegisteredVehiclesActivity extends AppCompatActivity implements RegisteredVehiclesAdapter.IRegisteredVehiclesItemClickedListener {

    //widgets
    private TextView txtVehicleName;
    private RecyclerView rcvList;
    //private Adapter = adaptor;
    private Button btnBack;
    private List<VehicleDataFirebase> vehicles;
    private RegisteredVehiclesAdapter adapter;
    private Constants constants;
    private RegisteredVehiclesActivityVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_vehicles);

        //Setup viewmodel and get data
        vm = new ViewModelProvider(this).get(RegisteredVehiclesActivityVM.class);
        vm.getVehicles().observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebases) {
                //Setup UI if there are no adapter
                if (adapter==null){
                    setupUI(vehicleDataFirebases);
                }
                Log.d(constants.REGISTEREDTAG, vehicleDataFirebases.get(1).getRegistrationNumber());
                //Update data
                adapter.updateVehicles(vehicleDataFirebases);
                vehicles = vehicleDataFirebases;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(constants.REGISTEREDTAG, user.getEmail());
        }
    }


    private void setupUI(List<VehicleDataFirebase> vehicleDataFirebases) {
        //setContentView(R.layout.activity_registered_vehicles_v2);

        //set up recyclerview with adapter and layout manager
        adapter = new RegisteredVehiclesAdapter(vehicleDataFirebases, this);
        rcvList = findViewById(R.id.rcvRV);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        txtVehicleName = findViewById(R.id.txtRVRegisteredVehicle);

        btnBack = findViewById(R.id.btnRVBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
    }

    private void Back() {
        finish();
    }

    @Override
    public void onRegisteredVehicleClicked(int position) {
        Intent intent = new Intent(this, VehicleDetailsActivity.class);
        intent.putExtra(constants.ID, vehicles.get(position).getRegistrationNumber());
        startActivity(intent);
    }

}