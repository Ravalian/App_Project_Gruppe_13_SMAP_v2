package com.example.smap_app_project_grp_13_carlog.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.smap_app_project_grp_13_carlog.Adapters.VehicleDetailsAdapter;
import com.example.smap_app_project_grp_13_carlog.Interface.VehicleDetailsSelectorInterface;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.R;

import java.util.List;

public class YourLogListFragment extends Fragment {

    private List<Log> logs;
    private ListView logList;
    private Button btnBack;
    private VehicleDetailsAdapter adapter;
    private VehicleDetailsSelectorInterface InterFace;

    public YourLogListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_log_list, container, false);

        logList = (ListView) view.findViewById(R.id.YourLogList);
        btnBack = view.findViewById(R.id.btnBackYL);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        updateUI();

        return view;
    }

    private void updateUI() {
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
    }

    private void back() {
        InterFace.back();
    }

    @Override
    public void onAttach(Context activity) {
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

    private void onLogSelected(int position){
        if(InterFace!=null){
            InterFace.onVehicleDetailsSelected(position);
        }
    }

    public void setLogs(List<Log> l) {
        logs = l;
    }

    public void update() {
        updateUI();
    }
}