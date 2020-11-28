package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.R;
//Kam ikke importeres af en eller anden grund
//import com.google.android.gms.maps.MapView;

import java.util.Date;

public class VehicleDetailsDetailsFragment extends Fragment {

    private TextView txtVDFragDUser, txtVDFragDDate, txtVDLFragDDistance,
            txtVDFragDTime, txtVDFragDLog, txtVDFragDLogDescription;
    private ImageView imgYLFragDVehicle;
    //Den virker ikke lige nu
    //private MapView mapYLFragDGoogle;

    private VehicleDetailsSelectorInterface VDSelector;

    public VehicleDetailsDetailsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_your_logs_fragment_details, container, false);

        //get references to UI widgets
        txtVDFragDUser = (TextView) view.findViewById(R.id.txtYLFragDUser);
        txtVDFragDDate = (TextView) view.findViewById(R.id.txtYLFragDDate);
        txtVDLFragDDistance = (TextView) view.findViewById(R.id.txtYLFragDDistance);
        txtVDFragDTime = (TextView) view.findViewById(R.id.txtYLFragDTime);
        txtVDFragDLog = (TextView) view.findViewById(R.id.txtYLFragDLog);
        txtVDFragDLogDescription = (TextView) view.findViewById(R.id.txtYLFragDLogDescription);

        updateVD();

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            VDSelector = (VehicleDetailsSelectorInterface) activity;
        } catch (ClassCastException ex) {
            //Activity does not implement correct interface
            throw new ClassCastException(activity.toString() + " must implement YourLogsSelectorInterface");
        }
    }

    public void setVD(Log logs) {
        if (txtVDFragDUser != null && txtVDFragDDate != null && txtVDLFragDDistance != null
                && txtVDFragDTime != null && txtVDFragDLogDescription != null) {

            txtVDFragDUser.setText(logs.getuser());
            txtVDFragDDate.setText((CharSequence) new Date(logs.getDate()));
            txtVDLFragDDistance.setText(logs.getDistance());
            txtVDFragDTime.setText(logs.getTime());
            txtVDFragDLogDescription.setText(logs.getLogDescription());
        }
    }

    private void updateVD() {
        if (VDSelector != null) {
            setVD(VDSelector.getCurrentSelection());
        }
    }
}
