package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.R;

public class VehicleDetailsActivity extends AppCompatActivity {

    //widgets
    private RecyclerView rcvList;
    private VehicleDetailsAdapter adapter;
    private Button btnBack;
    private TextView txtVDName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        //init ui
        setupUI();
    }

    private void setupUI() {
        //set up recyclerview with adapter and layout manager
        //adapter = new VehicleDetailsAdapter(this);
        rcvList = findViewById(R.id.rcvVehicleDetails);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        txtVDName = findViewById(R.id.txtVDName);

        btnBack = findViewById(R.id.btnVDBack);
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