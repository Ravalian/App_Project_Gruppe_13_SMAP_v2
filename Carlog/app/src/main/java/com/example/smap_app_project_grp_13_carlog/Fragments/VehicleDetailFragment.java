package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smap_app_project_grp_13_carlog.Activities.UserInterfaceActivity;
import com.example.smap_app_project_grp_13_carlog.Activities.VehicleDetailActivity;
import com.example.smap_app_project_grp_13_carlog.Activities.VehicleLogActivity;
import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.GlideApp;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class VehicleDetailFragment extends Fragment {

    private ListView logList;
    private TextView vName, owner, model, fuel, kml, hp, numseats;
    private Button btnBack, btnNew;
    private VehicleDetailsAdapter adapter;
    private List<Log> logs;
    private VehicleDataFirebase vehicle;
    public ImageView vehicleImg;
    private VehicleDetailsSelectorInterface InterFace;
    private Constants constants;

    //Firebase Storage
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public VehicleDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        //Setup for widgets
        logList = (ListView) view.findViewById(R.id.ListView);
        vName = view.findViewById(R.id.TxtRegNum);
        owner = view.findViewById(R.id.TxtOwner);
        model = view.findViewById(R.id.TxtModel);
        fuel = view.findViewById(R.id.TxtFuelType);
        kml = view.findViewById(R.id.TxtKmPrL);
        hp = view.findViewById(R.id.TxtHorsePower);
        numseats = view.findViewById(R.id.TxtNumOfSeats);
        vehicleImg = view.findViewById(R.id.ImgCar);

        //Setup firebase storage
        storage = FirebaseStorage.getInstance();

        //Setup for buttons
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

        //Update UI
        updateList();

        return view;
    }

    private void newLog() {
        Intent intent = new Intent(getActivity(), VehicleLogActivity.class);
        intent.putExtra(constants.ID, vehicle.getRegistrationNumber());
        startActivity(intent);
        

    }

    private void Back() {
        InterFace.back();
    }

    public void update(){
        updateList();
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
        if (InterFace!=null){
            logs = InterFace.getVehicleDetailsList();
        }
        if (logs!=null){
            adapter = new VehicleDetailsAdapter(getActivity(), logs);
            logList.setAdapter(adapter);
            logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onLogSelected(i);
                }
            });
        }
        if (vehicle!=null){
            vName.setText(vehicle.getRegistrationNumber());
            owner.setText(vehicle.getOwner());
            model.setText(vehicle.getModel());
            fuel.setText(vehicle.getFuelType());
            hp.setText(vehicle.getEnginePower() + " "  + getString(R.string.txt_hp));
            kml.setText("999" + " "  + getString(R.string.txt_kml));
            numseats.setText(vehicle.getSeats() + " " + getString(R.string.txt_seats));

            storageReference = storage.getReference().child("images/" + vehicle.getRegistrationNumber());
            if(storageReference != null){
                GlideApp.with(this).load(storageReference).into(vehicleImg);
            }
        }
    }

    private void onLogSelected(int position){
        if(InterFace!=null){
            InterFace.onVehicleDetailsSelected(position);
        }
    }

    public void setLogs(List<Log> logList){
        logs = (List<Log>) logList;
    }

    public void setVehicle(VehicleDataFirebase v){
        vehicle=v;
    }

}