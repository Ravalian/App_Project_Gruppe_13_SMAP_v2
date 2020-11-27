package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.List;

public class VehicleDetailFragment extends Fragment {



    private ListView logList;
    private TextView vName, owner, model, fuel, kml, hp, numseats;
    private Button btnBack, btnNew;
    private VehicleDetailsAdapter adapter;
    private List<Logs> logs;
    private VehicleDataFirebase vehicle;
    private ImageView vehicleImg;
    private VehicleDetailsSelectorInterface InterFace;

    public VehicleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        logList = (ListView) view.findViewById(R.id.ListView);
        vName = view.findViewById(R.id.TxtRegNum);
        owner = view.findViewById(R.id.TxtOwner);
        model = view.findViewById(R.id.TxtModel);
        fuel = view.findViewById(R.id.TxtFuelType);
        kml = view.findViewById(R.id.TxtKmPrL);
        hp = view.findViewById(R.id.TxtHorsePower);
        numseats = view.findViewById(R.id.TxtNumOfSeats);

        btnBack = view.findViewById(R.id.BtnBackVDF);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
        btnNew = view.findViewById(R.id.BtnNewVDF);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLog();
            }
        });


        Log.d("Tester", "pls");
        updateList();

        return view;
    }

    private void newLog() {
        //create new log
    }

    private void Back() {
        //go back
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

    private void updateList() {
        if (InterFace==null){
            logs = InterFace.getVehicleDetailsList();
        }
        if (logs!=null){
            adapter = new VehicleDetailsAdapter(getActivity(), logs);
            logList.setAdapter(adapter);
            Log.d("Tester", "Er vi der?");

            logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onLogSelected(i);
                }
            });
        }
        if (vehicle!=null){
            Log.d("Tester", "Er der en vehicle?");
            vName.setText(vehicle.getRegistrationNumber());
            owner.setText(vehicle.getOwner());
            model.setText(vehicle.getModel());
            fuel.setText(vehicle.getFuelType());
            hp.setText(vehicle.getEnginePower());
            kml.setText("9999999km/l");
            numseats.setText(vehicle.getSeats());
        }
        Log.d("Tester", "Bliver jeg kørt?");
    }

    private void onLogSelected(int position){
        if(InterFace==null){
            InterFace.onVehicleDetailsSelected(position);
        }
    }

    public void setLogs(List<Logs> logList){
        logs = (List<Logs>) logList;
    }

    public void setVehicle(VehicleDataFirebase v){
        Log.d("Tester", "Kommer du herind?");
        vehicle=v;
    }
}