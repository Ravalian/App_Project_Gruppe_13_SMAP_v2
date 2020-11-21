package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Interface.YourLogsSelectorInterface;
import com.google.android.gms.maps.MapView;

public class YourLogsDetailsFragment extends Fragment {

    private TextView txtYLFragDName, txtYLFragDDate, txtYLFragDDistance,
            txtYLFragDTime, txtYLFragDLog, txtYLFragDLogDescription;
    private ImageView imgYLFragDVehicle;
    private MapView mapYLFragDGoogle;

    private YourLogsSelectorInterface yourLogsSelector;

    public YourLogsDetailsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_your_logs_fragment_details, container, false);

        //get references to UI widgets
        txtYLFragDName = (TextView) view.findViewById(R.id.txtYLFragDUser);
        txtYLFragDDate = (TextView) view.findViewById(R.id.txtYLFragDDate);
        txtYLFragDDistance = (TextView) view.findViewById(R.id.txtYLFragDDistance);
        txtYLFragDTime = (TextView) view.findViewById(R.id.txtYLFragDTime);
        txtYLFragDLog = (TextView) view.findViewById(R.id.txtYLFragDLog);
        txtYLFragDLogDescription = (TextView) view.findViewById(R.id.txtYLFragDLogDescription);

        updateLogs();

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            yourLogsSelector = (YourLogsSelectorInterface) activity;
        } catch (ClassCastException ex) {
            //Activity does not implement correct interface
            throw new ClassCastException(activity.toString() + " must implement YourLogsSelectorInterface");
        }
    }

    public void setLogs(Logs logs) {
        if (txtYLFragDName != null && txtYLFragDDate != null && txtYLFragDDistance != null
                && txtYLFragDTime != null && txtYLFragDLogDescription != null) {

            txtYLFragDName.setText(logs.getLogID());
            txtYLFragDDate.setText((CharSequence) logs.getDate());
            txtYLFragDDistance.setText(logs.getDistance());
            txtYLFragDTime.setText(logs.getTime());
            txtYLFragDLogDescription.setText(logs.getLogDescription());
        }
    }

    private void updateLogs() {
        if (yourLogsSelector != null) {
            setLogs(yourLogsSelector.getCurrentSelection());
        }
    }
}
