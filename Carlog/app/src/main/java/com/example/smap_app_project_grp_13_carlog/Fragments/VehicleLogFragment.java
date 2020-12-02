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
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;


public class VehicleLogFragment extends Fragment implements OnMapReadyCallback {

    private TextView txtUserName, txtDate, txtDuration, txtDistance, txtLog;
    private Button btnBack;
    private VehicleDetailsSelectorInterface InterFace;
    private Log log;
    private MapView map;
    private GoogleMap gmap;

    public VehicleLogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_log, container, false);

        //UI setup
        txtDate = view.findViewById(R.id.TxtDateDF);
        txtDistance = view.findViewById(R.id.TxtDistanceDF);
        txtDuration = view.findViewById(R.id.TxtNameLabel);
        txtLog = view.findViewById(R.id.TxtLogDF);
        txtUserName = view.findViewById(R.id.TxtUsernameDF);
        map = view.findViewById(R.id.MapDF);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        //Button setup
        btnBack = view.findViewById(R.id.BtnBackDF);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        updateUI();
        return view;
    }

    private void back() {
        InterFace.back();
    }

    private void updateUI() {

        //Updates UI if a log is selected
        if (InterFace != null) {
            log = InterFace.getCurrentSelection();
        }
        if (log != null) {
            txtUserName.setText(log.getUserName());
            txtLog.setText(log.getLogDescription());
            txtDuration.setText("T: " + log.getTime()/120 + " M: " + log.getTime()/60 + " S: " + log.getTime()%60 );
            txtDistance.setText("" + log.getDistance());
            Date d = new Date(log.getDate());
            txtDate.setText("" + d.getDate() + "/" + d.getMonth());
        }
    }

    public void setLog(Log l) {
        log = l;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            InterFace = (VehicleDetailsSelectorInterface) activity;
        } catch (ClassCastException exception) {
            throw new ClassCastException(activity.toString() + " doesn't implement the right Interface (VehicleDetailSelecterInterface)");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        //Getting starting position
        com.example.smap_app_project_grp_13_carlog.Models.LatLng s = log.getPositions().get(0);
        LatLng start = new LatLng(s.getLatitude(), s.getLongitude());
        //Getting ending position
        s = log.getPositions().get(1);
        LatLng stop = new LatLng(s.getLatitude(), s.getLongitude());

        //Adding markers to the map and zooming to destination marker
        gmap.addMarker(new MarkerOptions()
                .position(start)
                .title("Starting point"));
        gmap.addMarker(new MarkerOptions()
                .position(stop)
                .title("Destination"));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(stop, 7));
    }
}