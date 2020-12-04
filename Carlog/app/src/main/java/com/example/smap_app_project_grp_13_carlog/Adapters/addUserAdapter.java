package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smap_app_project_grp_13_carlog.Models.UserRTDB;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class addUserAdapter extends RecyclerView.Adapter<addUserAdapter.addUserViewHolder> {
    public void setUsers(ArrayList<UserRTDB> addedUsers) {
        users = addedUsers;
    }

    public interface IAddUserItemClickedListener {
        void onUserClicked(int position);
    }

    //Firebase Storage
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<UserRTDB> users = new ArrayList<UserRTDB>();
    private IAddUserItemClickedListener listener;



    public addUserAdapter(ArrayList<UserRTDB> users, IAddUserItemClickedListener listener) {
        this.listener = listener;
        this.users = users;
    }



    @NonNull
    @Override
    public addUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_item, parent, false);
        addUserViewHolder vh = new addUserViewHolder(v, listener);
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull addUserViewHolder holder, int position) {

        holder.txtName.setText(users.get(position).getUserName());
        holder.txtID.setText(users.get(position).getUserId());
    }

    public class addUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder ui wiget references

        TextView txtName;
        TextView txtID;

        IAddUserItemClickedListener listener;

        //constructor
        public addUserViewHolder(@NonNull View itemView, IAddUserItemClickedListener clickedListener) {
            super(itemView);

            //get references from layout file
            txtName = itemView.findViewById(R.id.txtUserName);
            txtID = itemView.findViewById(R.id.txtUserID);
            listener = clickedListener;

            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onUserClicked(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
