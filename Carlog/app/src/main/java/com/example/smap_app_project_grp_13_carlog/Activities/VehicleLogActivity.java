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
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleLogVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
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
                stop.setText(R.string.btn_Stop);
                started = true;
                start();
            }
        });
        
        stop = findViewById(R.id.btnVLStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started){
                    started = false;
                    stop.setText(R.string.btn_save);
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
        Logs log = new Logs();
        Long now = System.currentTimeMillis();
        Log.d("Tester", ""+now);
        log.date = now;
        Log.d("Tester", "Hvad med dette? "+log.date);
        log.vehicle = ID;
        log.logDescription = vehicleLog.getText().toString();
        String uname = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Tester", "Hvem er du? "+uname);
        log.user = uname;
        log.time = 1;
        log.distance = 1;
        vm.saveLog(log);
    }

    private void back() {
        finish();
    }

    private void stop() {
        
    }

    private void start() {

    }

    private void updateUI() {
        vehicleName.setText(vehicle.getRegistrationNumber());
    }
}