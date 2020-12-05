package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;
import java.util.List;

public class YourVehiclesAdapter extends RecyclerView.Adapter<YourVehiclesAdapter.YourVehiclesViewHolder> {

    public interface IYourVehiclesItemClickedListener {
        void onYourVehiclesClicked(int YVID);
    }

    //Firebase Storage
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<VehicleDataFirebase> vehicles;

    public void updateVehicles(List<VehicleDataFirebase> vehicleDataFirebases) {
        vehicles = vehicleDataFirebases;
    }
    private IYourVehiclesItemClickedListener listener;

    public YourVehiclesAdapter(List<VehicleDataFirebase> vehicles, IYourVehiclesItemClickedListener listener) {
        this.listener = listener;
        this.vehicles = vehicles;
    }

    @NonNull
    @Override
    public YourVehiclesAdapter.YourVehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_your_vehicles_list_item, parent, false);
        YourVehiclesViewHolder vh = new YourVehiclesViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull YourVehiclesAdapter.YourVehiclesViewHolder holder, int position) {
        holder.txtVehicleModel.setText(vehicles.get(position).model);
        holder.txtVehicleMake.setText(vehicles.get(position).make);

        storageReference = storage.getReference().child("images/" + vehicles.get(position).getRegistrationNumber());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                Glide.with(holder.img_.getContext()).load(imageUrl).into(holder.img_);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Handle errors
                e.printStackTrace();
            }
        });

    }

    public class YourVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder ui wiget references
        ImageView img_;
        TextView txtVehicleModel;
        TextView txtVehicleMake;

        IYourVehiclesItemClickedListener listener;

        //constructor
        public YourVehiclesViewHolder(@NonNull View itemView, IYourVehiclesItemClickedListener YourVehiclesItemClickedListener) {
            super(itemView);

            //get references from layout file
            img_ = itemView.findViewById(R.id.imgYVVehicle);
            txtVehicleModel = itemView.findViewById(R.id.txtYVVehicleModel);
            txtVehicleMake = itemView.findViewById(R.id.txtYVVehicleMake);
            listener = YourVehiclesItemClickedListener;

            //Setup firebase storage
            storage = FirebaseStorage.getInstance();

            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onYourVehiclesClicked(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
}
