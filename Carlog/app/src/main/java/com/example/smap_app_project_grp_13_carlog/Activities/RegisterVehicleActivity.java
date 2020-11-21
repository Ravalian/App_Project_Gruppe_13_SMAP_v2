package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.RegisterVehicleActivityVM;

public class RegisterVehicleActivity extends AppCompatActivity {

    //UI widgets
    private Button  registerVHAddBtn, registerVHCancelBtn;
    private EditText registerInput;

    //Viewmodel
    private RegisterVehicleActivityVM registerVehicleActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);

        //Setup viewmodel
        registerVehicleActivityVM = new ViewModelProvider(this).get(RegisterVehicleActivityVM.class);

        //Setup Edittext
        registerInput = findViewById(R.id.RegisterVehicleInputField);

        //Setup add Vehicle button
        registerVHAddBtn = findViewById(R.id.RegisterVHAddBtn);
        registerVHAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddVehicle();
            }
        });

        //Setup Cancel button
        registerVHCancelBtn = findViewById(R.id.RegisterVehicleCancelBtn);
        registerVHCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });
    }

    //What happens when add vehicle btn is pressed
    private void AddVehicle() {
        registerVehicleActivityVM.addVehicletoFirebase(registerInput.getText().toString());
    }

    //What happens when cancel btn
    private void Cancel() {
        finish();
    }

    //Inspiration for image upload: http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/

}