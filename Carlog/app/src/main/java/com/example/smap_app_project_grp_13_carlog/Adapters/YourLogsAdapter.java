package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smap_app_project_grp_13_carlog.Models.LogsTest;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.ArrayList;
import java.util.Date;

public class YourLogsAdapter extends BaseAdapter {

    Context context;
    ArrayList<LogsTest> yourlogs;
    LogsTest yourlog = null;

    public YourLogsAdapter(Context c, ArrayList<LogsTest> yourlogsList) {
        yourlogs = yourlogsList;
        context = c;
    }


    @Override
    public int getCount() {
        if (yourlogs != null) {
            return yourlogs.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (yourlogs != null) {
            return yourlogs.get(position);
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
            LayoutInflater yourlogInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = yourlogInflater.inflate(R.layout.activity_your_logs_list_item, null);
        }

        yourlog = yourlogs.get(position);
        if (yourlog != null) {
            Date date = yourlog.getDate();

            TextView txtDate = (TextView) convertView.findViewById(R.id.txtYLListItemDate);
            txtDate.setText((CharSequence) yourlog.getDate());

            TextView txtName = (TextView) convertView.findViewById(R.id.txtYLListItemName);
            txtName.setText(yourlog.getName());

        }
        return convertView;
    }
}
