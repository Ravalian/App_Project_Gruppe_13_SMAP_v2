package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleLogVM;

import java.util.List;


public class VehicleLogActivity extends AppCompatActivity {

    TextView vehicleName, vehicleLog;
    Button start, stop, back;
    String ID;
    VehicleLogVM vm;
    VehicleDataFirebase vehicle;
    Boolean started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_log);

        Intent intent = getIntent();
        ID = intent.getStringExtra(Constants.ID);
        Log.d("Tester", ID);
        started = false;

        setupIU();
        
        
        vm = new ViewModelProvider(this).get(VehicleLogVM.class);
        vm.getVehicle().observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebase) {
                for (VehicleDataFirebase v:
                     vehicleDataFirebase) {
                    Log.d("Tester", ID+" og "+v.getRegistrationNumber());
                    if (ID.contains(v.getRegistrationNumber().trim())){
                        Log.d("Tester", "Hejsa");
                        vehicle=v;
                    }
                }
                updateUI();
            }
        });
    }

    private void setupIU() {
        vehicleName = findViewById(R.id.txtVLVehicleName);
        vehicleLog = findViewById(R.id.txteditVLlog);
        
        start = findViewById(R.id.btnVLStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        
        stop = findViewById(R.id.btnVLStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started){
                    started = false;
                    stop.setText("Save");
                    stop();
                }
                save();
            }
        });
        
        back = findViewById(R.id.btnVLBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        
    }

    private void save() {
    }

    private void back() {
    }

    private void stop() {
        
    }

    private void start() {
        stop.setText("Stop");
        started = true;
    }

    private void updateUI() {
        vehicleName.setText(vehicle.getRegistrationNumber());
    }
}