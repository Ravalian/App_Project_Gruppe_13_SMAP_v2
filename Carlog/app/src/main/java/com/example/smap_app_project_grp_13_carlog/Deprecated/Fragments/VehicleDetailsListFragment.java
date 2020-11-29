package com.example.smap_app_project_grp_13_carlog.Deprecated.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.ArrayList;

public class VehicleDetailsListFragment extends Fragment {

    private ListView VDListView;
    private VehicleDetailsAdapter adapter;
    private ArrayList<Log> logs;
    private VehicleDataFirebase vehicle;

    private ImageView imgVDFragList;

    private VehicleDetailsSelectorInterface vehicleDetailsSelector;
    private View v;

    public VehicleDetailsListFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_vehicle_details_fragment_list, container, false);

        VDListView = (ListView) v.findViewById(R.id.LVVDFragmentList);
        imgVDFragList = (ImageView) v.findViewById(R.id.imgVDFragList);

        updateVD();

        return v;
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            vehicleDetailsSelector = (VehicleDetailsSelectorInterface) activity;
        } catch (ClassCastException ex) {
            //Activity does not implement correct interface
            throw new ClassCastException(activity.toString() + " must implement VehicleDetailsSelectorInterface");
        }
    }

    private void onVDSelected(int position) {
        if (vehicleDetailsSelector != null) {
            vehicleDetailsSelector.onVehicleDetailsSelected(position);
        }
    }

    public void setVD(ArrayList<Log> VDList, VehicleDataFirebase vehicle) {
        logs = (ArrayList<Log>) VDList.clone();
        this.vehicle = vehicle;
    }

    private void updateVD() {
        if (vehicleDetailsSelector != null) {
            logs = vehicleDetailsSelector.getVehicleDetailsList();
        }
        if (logs != null) {
            adapter = new VehicleDetailsAdapter(getActivity(), logs);
            VDListView.setAdapter(adapter);

            VDListView.setOnItemClickListener(((adapterView, view, position, id) -> onVDSelected(position)));
        }
    }
}
