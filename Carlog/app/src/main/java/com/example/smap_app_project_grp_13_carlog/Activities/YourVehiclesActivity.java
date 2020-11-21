package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Adapters.YourVehiclesAdapter;
import com.example.smap_app_project_grp_13_carlog.R;

public class YourVehiclesActivity extends AppCompatActivity {

    //widgets
    private RecyclerView rcvList;
    private YourVehiclesAdapter adapter;
    private Button btnBack;
    private TextView txtYourVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_vehicles);

        //init ui
        setupUI();
    }

    private void setupUI() {
        //set up recyclerview with adapter and layout manager
        //adapter = new YourVehiclesAdapter(this);
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
    }
}