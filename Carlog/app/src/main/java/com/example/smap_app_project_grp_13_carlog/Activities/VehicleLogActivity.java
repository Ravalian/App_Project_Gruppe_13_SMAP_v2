package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.LatLng;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleLogVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class VehicleLogActivity extends AppCompatActivity implements LocationListener {

    private TextView vehicleName, vehicleLog;
    private Button start, stop, back;
    private String ID;
    private VehicleLogVM vm;
    private VehicleDataFirebase vehicle;
    private Boolean started;
    private int duration;
    private Long startTime;
    private ArrayList<LatLng> positions;
    private LocationManager locationManager;
    private Location currentLocation;

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_log);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);

        Intent intent = getIntent();
        ID = intent.getStringExtra(Constants.ID);
        android.util.Log.d("Tester", ID);
        started = false;
        duration = 0;
        positions = new ArrayList<>();
        
        setupIU();
        //Request location access. Location stuff from this activity is inspired from https://howtodoandroid.medium.com/how-to-get-current-latitude-and-longitude-in-android-example-35437a51052a
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            android.util.Log.e("Error", e.toString());
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
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
                    stop.setText(R.string.btn_Save);
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
        log.date = startTime;
        log.vehicle = ID;
        log.logDescription = vehicleLog.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        log.user = user.getUid();
        log.time = duration;
        log.distance = 1;
        log.userName = user.getDisplayName();
        log.setPositions(positions);
        vm.saveLog(log);
        finish();
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    private void stop() {
        duration = (int) ((System.currentTimeMillis()-startTime)/1000)+duration;
        LatLng stop = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        positions.add(stop);
    }

    private void start() {
        startTime = System.currentTimeMillis();
        LatLng start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        android.util.Log.d("Tester", ""+start.getLongitude());
        positions.add(start);

    }

    private void updateUI() {
        vehicleName.setText(vehicle.getRegistrationNumber());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
    }
}