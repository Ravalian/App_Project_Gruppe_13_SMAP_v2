package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smap_app_project_grp_13_carlog.Adapters.addUserAdapter;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.UserRTDB;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.AddUserVM;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements addUserAdapter.IAddUserItemClickedListener {

    private VehicleDataFirebase vehicle;
    private List<UserRTDB> users;
    private ArrayList<UserRTDB> addedUsers;
    private Button btnBack, btnAdd;
    private TextView nameInput;
    private AddUserVM vm;
    private String ID;
    private Constants constants;
    private addUserAdapter adapter;
    private RecyclerView userlist;
    private int timesPressed;
    private UserRTDB lastpressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);                //Inspired by https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/


        ID = getIntent().getStringExtra(constants.ID);
        addedUsers = new ArrayList<UserRTDB>();
        lastpressed = new UserRTDB();

        vm = new ViewModelProvider(this).get(AddUserVM.class);
        vm.getVehicle(ID).observe(this, new Observer<VehicleDataFirebase>() {
            @Override
            public void onChanged(VehicleDataFirebase vehicleDataFirebase) {
                vehicle = vehicleDataFirebase;
            }
        });
        vm.getUsers().observe(this, new Observer<List<UserRTDB>>() {
            @Override
            public void onChanged(List<UserRTDB> userRTDBS) {
                users = userRTDBS;
                //android.util.Log.d("Tester", "" + userRTDBS.get(0));
                if (vehicle.registeredUsers!=null){
                    for (UserRTDB u:
                            users) {
                        for (UserRTDB user:
                                vehicle.registeredUsers) {
                            if (user.getUserId().trim().contains(u.userId)){
                                addedUsers.add(u);
                            }
                        }
                    }
                }
                if (adapter==null){
                    setupUI();

                }
            }
        });


    }

    private void setupUI() {
        adapter = new addUserAdapter(addedUsers, this);
        nameInput = findViewById(R.id.txt_Add_AU);
        userlist = findViewById(R.id.RCVListAddedUsers);
        userlist.setLayoutManager(new LinearLayoutManager(this));
        userlist.setAdapter(adapter);

        btnBack = findViewById(R.id.btn_back_AU);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        btnAdd = findViewById(R.id.btn_Add_AU);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void add() {
        for (UserRTDB u:
             users) {
            if (u.userName.contains(nameInput.getText().toString().trim())){
                for (UserRTDB us:
                     addedUsers) {
                    if (us.getUserName().trim().contains(nameInput.getText().toString().trim())){
                        return;
                    }
                }
                addedUsers.add(u);
                vehicle.addRegisteredUser(u);

            }
        }
        adapter.setUsers(addedUsers);
        vm.addUsersToVehicle(addedUsers);
    }

    private void back() {

        //Go back with a custom animation
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    @Override
    public void onUserClicked(int position) {

        if(lastpressed == addedUsers.get(position)){
            timesPressed++;
        } else{
            timesPressed=0;
        }
        lastpressed = addedUsers.get(position);
        if (timesPressed==10){
            Toast.makeText(this, addedUsers.get(position).getUserName() + " er blevet tilføjet og der sker ikke noget af at du trykker her ;-)", Toast.LENGTH_LONG).show();
        }
    }
}