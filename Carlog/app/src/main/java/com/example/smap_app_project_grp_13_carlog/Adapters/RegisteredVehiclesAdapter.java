package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smap_app_project_grp_13_carlog.Fragments.VehicleDetailFragment;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.bumptech.glide.Glide;

public class RegisteredVehiclesAdapter extends RecyclerView.Adapter<RegisteredVehiclesAdapter.RegisteredVehiclesViewHolder> {

    public void updateVehicles(List<VehicleDataFirebase> vehicleDataFirebases) {
        vehicles = vehicleDataFirebases;
    }

    public interface IRegisteredVehiclesItemClickedListener {
        void onRegisteredVehicleClicked(int RVID);
    }

    private IRegisteredVehiclesItemClickedListener listener;

    private List<VehicleDataFirebase> vehicles;

    //Firebase Storage
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public RegisteredVehiclesAdapter(List<VehicleDataFirebase> vehicles, IRegisteredVehiclesItemClickedListener listener) {
        this.listener = listener;
        this.vehicles=vehicles;
    }

    public void setList(ArrayList<VehicleDataFirebase> vehicles) {this.vehicles = vehicles;}

    @NonNull
    @Override
    public RegisteredVehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_registered_vehicles_list_item, parent, false);
        RegisteredVehiclesViewHolder vh = new RegisteredVehiclesViewHolder(v, listener);
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull RegisteredVehiclesAdapter.RegisteredVehiclesViewHolder holder, int position) {
        holder.txtVehicleName.setText(vehicles.get(position).registrationNumber);
        holder.txtOwnerName.setText(vehicles.get(position).getOwner());

        storageReference = storage.getReference().child("images/" + vehicles.get(position).getRegistrationNumber());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                Glide.with(holder.img.getContext()).load(imageUrl).into(holder.img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Handle errors
                e.printStackTrace();
            }
        });
    }

    public class RegisteredVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder ui widget references
        ImageView img;
        TextView txtVehicleName, txtOwnerName;




        IRegisteredVehiclesItemClickedListener listener;

        //constructor
        public RegisteredVehiclesViewHolder(@NonNull View itemView, IRegisteredVehiclesItemClickedListener RegisteredVehiclesItemClickedListener) {
            super(itemView);

            //get references from the layout file
            //img = itemView.findViewById(R.id.imgRVVehicle);
            txtVehicleName = itemView.findViewById(R.id.txtRVLIVehicleName);
            txtOwnerName = itemView.findViewById(R.id.txtRVLIOwnerName);
            img = itemView.findViewById(R.id.VehicleImg);
            listener = RegisteredVehiclesItemClickedListener;


            //Setup firebase storage
            storage = FirebaseStorage.getInstance();
            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRegisteredVehicleClicked(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
}
