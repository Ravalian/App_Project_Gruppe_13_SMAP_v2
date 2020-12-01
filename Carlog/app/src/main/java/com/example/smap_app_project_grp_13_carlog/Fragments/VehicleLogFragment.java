package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Models.Log;

import java.util.Date;


public class VehicleLogFragment extends Fragment {

    private TextView txtUserName, txtDate, txtDuration, txtDistance, txtLog;
    private Button btnBack, btnNew;
    private VehicleDetailsSelectorInterface InterFace;
    private VehicleDataFirebase vehicle;
    private Log log;

    public VehicleLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_log, container, false);

        txtDate = view.findViewById(R.id.TxtDateDF);
        txtDistance = view.findViewById(R.id.TxtDistanceDF);
        txtDuration = view.findViewById(R.id.TxtNameLabel);
        txtLog = view.findViewById(R.id.TxtLogDF);
        txtUserName = view.findViewById(R.id.TxtUsernameDF);
        
        btnBack = view.findViewById(R.id.BtnBackDF);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        /*btnNew = view.findViewById(R.id.BtnNewDF);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLog();
            }
        });*/

        setupUI();

        return view;
    }

    private void newLog() {
    }

    private void back() {
        InterFace.back();
    }

    public void UpdateUI(){
        setupUI();
    }

    private void setupUI() {
        if (InterFace!=null){
            log = InterFace.getCurrentSelection();

        }
        if (log!=null){
            txtUserName.setText(log.getUserName());
            txtLog.setText(log.getLogDescription());
            txtDuration.setText(""+log.getTime());
            txtDistance.setText(""+log.getDistance());
            Date d = new Date(log.getDate());
            txtDate.setText(""+d.getDate()+"/"+d.getMonth());
        }
    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);

        try{
            InterFace = (VehicleDetailsSelectorInterface) activity;
        } catch (ClassCastException exception){
            throw new ClassCastException(activity.toString()+" doesn't implement the right Interface (VehicleDetailSelecterInterface)");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void setLog(Log l){
        log = l;
    }
}