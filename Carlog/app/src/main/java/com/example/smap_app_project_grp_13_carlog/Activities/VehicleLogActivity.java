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
import android.widget.Toast;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.LatLng;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.VehicleLogVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class VehicleLogActivity extends AppCompatActivity implements LocationListener {

    private TextView vehicleName, vehicleLog;
    private Button start, stop, back;
    private String ID;
    private VehicleLogVM vm;
    private VehicleDataFirebase vehicle;
    private Boolean started;
    private int duration;
    private Long startTime;
    private double distance;
    private ArrayList<LatLng> positions;
    private LocationManager locationManager;
    private Location currentLocation;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_log);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);

        //Initializes local variables
        Intent intent = getIntent();
        ID = intent.getStringExtra(constants.ID);
        started = false;
        duration = 0;
        positions = new ArrayList<>();
        distance = 0;
        
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


        //Setup for LiveData of the current vehicle
        vm = new ViewModelProvider(this).get(VehicleLogVM.class);
        vm.getVehicle(ID).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
                updateUI();
            }
        });
    }

    private void setupIU() {
        vehicleName = findViewById(R.id.txtVLVehicleName);
        vehicleLog = findViewById(R.id.txteditVLlog);

        //Setup for start button
        start = findViewById(R.id.btnVLStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentLocation!=null) {
                    stop.setText(R.string.btn_Stop);
                    started = true;
                    start();
                } else {
                    started = false;
                    maketoast();

                }

            }
        });

        //Setup for stop button
        stop = findViewById(R.id.btnVLStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If start has been pressed, stop the recording else save the log
                if (started){
                    started = false;
                    stop.setText(R.string.btn_Save);
                    stop();
                }else {
                    save();
                }
            }
        });

        //Setup for ack button
        back = findViewById(R.id.btnVLBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    private void maketoast() {
        Toast.makeText(this, "You have to move just a bit", Toast.LENGTH_LONG).show();
    }

    private void save() {

        //Save the log and go back
        Log log = new Log();
        if (startTime==null){
            startTime = System.currentTimeMillis();
        }
        log.date = startTime;
        log.vehicle = ID;
        log.logDescription = vehicleLog.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        log.user = user.getUid();
        log.time = duration;
        log.distance = (int)distance;
        log.userName = user.getDisplayName();
        if (positions.size()==0){
            positions = new ArrayList<>();
            positions.add(new LatLng(0.0,0.0));
            positions.add(new LatLng(0.0,0.0));
        }
        log.setPositions(positions);
        log.setVehicleOwner(vehicle.ownerID);
        vm.saveLog(log);
        back();
    }

    private void back() {

        //Goes back with a custom animation
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    private void stop() {

        //Stops the recording and stores the necessary data
        duration = (int) ((System.currentTimeMillis()-startTime)/1000)+duration;
        LatLng stop = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        positions.add(stop);
    }

    private void start() {

        //Starts the recording if currentLocation is set, else makes a toast

        startTime = System.currentTimeMillis();
        LatLng start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        positions.add(start);

    }

    private void updateUI() {
        vehicleName.setText(vehicle.getRegistrationNumber());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (started) {
            distance = getDestance(currentLocation, location) + distance;
        }
        currentLocation = location;
    }

    private double getDestance(Location before, Location after){
        final double R = 6378135;
        final double lat1 = before.getLatitude()*Math.PI/180;
        final double lat2 = after.getLatitude()*Math.PI/180;
        final double dlong = (after.getLongitude()-before.getLongitude())*3.14157/180;
        final double a = Math.sin((lat2-lat1)/2)*Math.sin((lat2-lat1)/2)+Math.cos(lat1)*Math.cos(lat2)*Math.sin((dlong)/2)*Math.sin((dlong)/2);
        final double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        final double d = R*c;

        return d;
    }
}