package com.example.smap_app_project_grp_13_carlog.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smap_app_project_grp_13_carlog.Activities.VehicleLogActivity;
import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
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
    private TextView vName, owner, model, fuel, hp, numseats, make;
    private Button btnBack, btnNew, btnReg;
    private VehicleDetailsAdapter adapter;
    private List<Log> logs;
    private VehicleDataFirebase vehicle;
    public ImageView vehicleImg;
    private VehicleDetailsSelectorInterface InterFace;
    private Constants constants;
    private boolean isOwner;

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
        vName = view.findViewById(R.id.carMake);
        owner = view.findViewById(R.id.TxtOwner);
        model = view.findViewById(R.id.TxtModel);
        fuel = view.findViewById(R.id.TxtFuelType);
        hp = view.findViewById(R.id.TxtHorsePower);
        numseats = view.findViewById(R.id.TxtNumOfSeats);
        vehicleImg = view.findViewById(R.id.ImgCar);
        make = view.findViewById(R.id.carMake);

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

        btnReg = view.findViewById(R.id.BtnRegVDF);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        //btnReg.setText(R.string.btn_regnewuser);

        btnReg.setVisibility(View.GONE);
        //Update UI
        updateList();

        return view;
    }

    private void registerUser() {
        InterFace.registerUsers();
    }

    private void newLog() {
        Intent intent = new Intent(getActivity(), VehicleLogActivity.class);
        intent.putExtra(constants.ID, vehicle.getRegistrationNumber());
        startActivity(intent);
        

    }

    private void Back() {
        InterFace.back();
    }

    public void update(boolean o){
        if (o){
            btnReg.setVisibility(View.VISIBLE);

        } else {
            btnReg.setVisibility(View.GONE);
        }
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
            hp.setText(vehicle.getEnginePower());
            numseats.setText(vehicle.getSeats());
            make.setText(vehicle.getMake());

            storageReference = storage.getReference().child("images/" + vehicle.getRegistrationNumber());

            //Inspiration: https://stackoverflow.com/questions/46652380/getting-image-from-firebase-storage-using-glide
            storageReference = storage.getReference().child("images/" + vehicle.getRegistrationNumber());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    Glide.with(VehicleDetailFragment.this).load(imageUrl).into(vehicleImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Handle errors
                    e.printStackTrace();
                }
            });
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

    public void setVehicle(VehicleDataFirebase v, boolean o){
        isOwner = o;
        vehicle=v;
        if (isOwner){
            btnReg.setVisibility(View.VISIBLE);

        } else {
            btnReg.setVisibility(View.GONE);
        }
    }
}