package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class YourVehiclesAdapter extends RecyclerView.Adapter<YourVehiclesAdapter.YourVehiclesViewHolder> {

    public interface IYourVehiclesItemClickedListener {
        void onYourVehiclesClicked(int YVID);
    }

    private List<VehicleDataFirebase> vehicles;

    public void updateVehicles(List<VehicleDataFirebase> vehicleDataFirebases) {
        vehicles = vehicleDataFirebases;
    }
    private IYourVehiclesItemClickedListener listener;

    /*
    private List<DBPlaceHolder> YVList = new List<DBPlaceHolder>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<DBPlaceHolder> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(DBPlaceHolder dbPlaceHolder) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends DBPlaceHolder> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends DBPlaceHolder> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public DBPlaceHolder get(int i) {
            return null;
        }

        @Override
        public DBPlaceHolder set(int i, DBPlaceHolder dbPlaceHolder) {
            return null;
        }

        @Override
        public void add(int i, DBPlaceHolder dbPlaceHolder) {

        }

        @Override
        public DBPlaceHolder remove(int i) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<DBPlaceHolder> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<DBPlaceHolder> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<DBPlaceHolder> subList(int i, int i1) {
            return null;
        }
    };

*/
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
        holder.txtVehicleName.setText(vehicles.get(position).registrationNumber);

    }

    public class YourVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder ui wiget references
        ImageView img;
        TextView txtVehicleName;

        IYourVehiclesItemClickedListener listener;

        //constructor
        public YourVehiclesViewHolder(@NonNull View itemView, IYourVehiclesItemClickedListener YourVehiclesItemClickedListener) {
            super(itemView);

            //get references from layout file
            img = itemView.findViewById(R.id.imgYVVehicle);
            txtVehicleName = itemView.findViewById(R.id.txtYVVehicleName);
            listener = YourVehiclesItemClickedListener;

            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //listener.onYourVehiclesClicked(YVList.get(getAdapterPosition()).getYVID());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
