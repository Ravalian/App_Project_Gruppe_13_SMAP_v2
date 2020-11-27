package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.TimeoutError;
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VehicleDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<Logs> VDlogs;
    private Logs VDlog = null;

    public VehicleDetailsAdapter(Context c, List<Logs> VDlogsList) {
        VDlogs = VDlogsList;
        context = c;
    }

    @Override
    public int getCount() {
        if (VDlogs != null) {
            return VDlogs.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (VDlogs != null) {
            return VDlogs.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater VDInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = VDInflater.inflate(R.layout.activity_vehicle_details_list_item, null);
        }

        VDlog = VDlogs.get(position);
        if (VDlog != null) {
            Long date = VDlog.getDate();

            TextView txtDate = (TextView) convertView.findViewById(R.id.txtVDListItemDate);
            txtDate.setText((CharSequence) new Date(VDlog.getDate()).toString());

            TextView txtUser = (TextView) convertView.findViewById(R.id.txtVDListItemUser);
            txtUser.setText(VDlog.getuser());
        }
        return convertView;
    }
}
