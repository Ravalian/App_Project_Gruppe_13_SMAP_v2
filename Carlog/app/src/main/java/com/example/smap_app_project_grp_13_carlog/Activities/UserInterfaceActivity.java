package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Services.ForegroundService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserInterfaceActivity extends AppCompatActivity {

    //Ui widgets
    private Button btnlogout, btnregisterVehicle, btnregisteredVehicle, btnYourLogs, btnYourVehicles;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);


        //Setup Login button
        btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnYourLogs = findViewById(R.id.btnUIYourLogs);
        btnYourLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToYourLogs();
            }
        });

        //Setup Register Vehicle button
        btnregisterVehicle = findViewById(R.id.UI_RegisterVehicleBtn);
        btnregisterVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoRegisterVehicle();
            }
        });

        //Setup Registered Vehicle button
        btnregisteredVehicle = findViewById(R.id.UI_RegisteredVehiclesBtn);
        btnregisteredVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisteredVehicles();
            }
        });

        btnYourVehicles = findViewById(R.id.UI_YourVehiclesBtn);
        btnYourVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToYourVehicles();
            }
        });
        startService();

    }

    private void startService(){
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        startService(serviceIntent);
    }

    private void GoToYourVehicles() {
        Intent intent = new Intent(this, YourVehiclesActivity.class);
        startActivity(intent);
    }

    private void GoToYourLogs() {
        Intent intent = new Intent(this, YourLogsActivity.class);
        startActivity(intent);
    }

    private void goToRegisteredVehicles() {
        Intent intent = new Intent(this, RegisteredVehiclesActivity.class);
        startActivity(intent);
    }

    //What happens when Register Vehicle is pressed
    private void GotoRegisterVehicle() {
        Intent intent = new Intent(this, RegisterVehicleActivity.class);
        startActivity(intent);
    }

    //What happens when logout button is pressed
    private void signOut() {
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        if(mAuth.getCurrentUser()!=null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            gotoLoginActivity();
                        }
                    });
        }else{
            Toast.makeText(this, "Not Logged In", Toast.LENGTH_SHORT).show();
        }
    }

    //Method for returning to login activity
    private void gotoLoginActivity() {
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}