package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleLogVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VehicleLogActivity extends AppCompatActivity {

    private TextView vehicleName, vehicleLog;
    private Button start, stop, back;
    private String ID;
    private VehicleLogVM vm;
    private VehicleDataFirebase vehicle;
    private Boolean started;
    private int duration;
    private Long startTime;

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_log);

        Intent intent = getIntent();
        ID = intent.getStringExtra(Constants.ID);
        android.util.Log.d("Tester", ID);
        started = false;
        duration = 0;
        
        setupIU();
        
        
        vm = new ViewModelProvider(this).get(VehicleLogVM.class);
        vm.getVehicle().observe(this, new Observer<List<VehicleDataFirebase>>() {
            @Override
            public void onChanged(List<VehicleDataFirebase> vehicleDataFirebase) {
                for (VehicleDataFirebase v:
                     vehicleDataFirebase) {
                    android.util.Log.d("Tester", ID+" og "+v.getRegistrationNumber());
                    if (ID.contains(v.getRegistrationNumber().trim())){
                        android.util.Log.d("Tester", "Hejsa");
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
                }else {
                    save();
                }
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
        Log log = new Log();
        Long now = System.currentTimeMillis();
        log.date = now;
        log.vehicle = ID;
        log.logDescription = vehicleLog.getText().toString();
        String uname = FirebaseAuth.getInstance().getCurrentUser().getUid();
        log.user = uname;
        log.time = duration;
        log.distance = 1;
        log.userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        vm.saveLog(log);
        finish();
    }

    private void back() {
        finish();
    }

    private void stop() {
        duration = (int) ((System.currentTimeMillis()-startTime)/1000)+duration;
        android.util.Log.d("Tester", ""+duration);
        
    }

    private void start() {
        startTime = System.currentTimeMillis();

    }

    private void updateUI() {
        vehicleName.setText(vehicle.getRegistrationNumber());
    }
}