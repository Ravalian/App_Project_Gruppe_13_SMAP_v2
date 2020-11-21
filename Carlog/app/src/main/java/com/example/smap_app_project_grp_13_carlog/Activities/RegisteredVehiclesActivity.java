package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisteredVehiclesActivity extends AppCompatActivity implements RegisteredVehiclesAdapter.IRegisteredVehiclesItemClickedListener {

    //widgets
    private TextView txtVehicleName;
    private RecyclerView rcvList;
    //private Adapter = adaptor;
    private Button btnBack;
    private ArrayList<VehicleDataFirebase> vehicles;
    private RegisteredVehiclesAdapter adapter;
    private Constants constants;
    private RegisteredVehiclesActivityVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_vehicles);

        //init ui
        //setupUI();
        vm = new ViewModelProvider(this).get(RegisteredVehiclesActivityVM.class);
        vm.getVehicles().observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebases) {
                if (adapter==null){
                    setupUI(vehicleDataFirebases);
                }
                Log.d("Tester", vehicleDataFirebases.get(1).getRegistrationNumber());
                adapter.updateVehicles(vehicleDataFirebases);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

        }
    }


    private void setupUI(List<VehicleDataFirebase> vehicleDataFirebases) {
        setContentView(R.layout.activity_registered_vehicles);

        //set up recyclerview with adapter and layout manager
        adapter = new RegisteredVehiclesAdapter(vehicleDataFirebases, this);
        rcvList = findViewById(R.id.rcvRegisteredVehicles);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        txtVehicleName = findViewById(R.id.txtRVVehicleName);

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
    public void onRegisteredVehicleClicked(int RVID) {
        Intent intent = new Intent(this, VehicleDetailsActivity.class);
        startActivityForResult(intent, 101);
    }

    /*
    @Override
    public void onRegisteredVehicleClicked(int RVID) {
        Intent intent = new Intent(this, com.app_project_group_13.carlog.Activities.VehicleDetails.class);
        intent.putExtra(constants.ID, RVID);
    }
    */
}