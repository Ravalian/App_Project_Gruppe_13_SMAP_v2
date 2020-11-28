package com.example.smap_app_project_grp_13_carlog.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.smap_app_project_grp_13_carlog.Adapters.YourLogsAdapter;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Interface.YourLogsSelectorInterface;

import java.util.ArrayList;

public class YourLogsListFragment extends Fragment {

    private ListView yourlogsListView;
    private YourLogsAdapter adapter;
    private ArrayList<Log> yourlogs;

    private ImageView imgYLFragList;

    private YourLogsSelectorInterface yourLogsSelector;

    public YourLogsListFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_your_logs_fragment_list, container, false);

        yourlogsListView = (ListView) v.findViewById(R.id.LVYLFragmentList);
        imgYLFragList = (ImageView) v.findViewById(R.id.imgYLFragList);

        updateYourLogs();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            yourLogsSelector = (YourLogsSelectorInterface) activity;
        } catch (ClassCastException ex) {
            //Activity does not implement correct interface
            throw new ClassCastException(activity.toString() + " must implement MovieSelectorInterface");
        }
    }

    private void onLogSelected(int position) {
        if (yourLogsSelector != null) {
            yourLogsSelector.onYourLogSelected(position);
        }
    }

    public void setLogs(ArrayList<Log> logsList) {
        yourlogs = (ArrayList<Log>) logsList.clone();
    }

    private void updateYourLogs() {
        if (yourLogsSelector != null) {
            yourlogs = yourLogsSelector.getYourLogsList();
        }
        if (yourlogs != null) {
            adapter = new YourLogsAdapter(getActivity(), yourlogs);
            yourlogsListView.setAdapter(adapter);

            yourlogsListView.setOnItemClickListener((adapterView, view, position, id) -> onLogSelected(position));
        }
    }
}
