package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.LoginActivityVM;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Viewmodel
    private LoginActivityVM loginActivityVM;

    //Ui widgets
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Login Button
        loginBtn = findViewById(R.id.btnlogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Setup Viewmodel
        loginActivityVM = new ViewModelProvider(this).get(LoginActivityVM.class);

        //Usercheck
        loginActivityVM.checkUsersRTDB();

        //Setup Firebase access
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        if(mAuth.getCurrentUser()!=null){
            gotoUserInterface();
        }

    }

    private void signIn(){
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        if(mAuth.getCurrentUser()!=null){
            gotoUserInterface();
        }else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(), Constants.RC_SIGN_IN
            );
        }
    }

    private void gotoUserInterface() {
        Intent i = new Intent(this, UserInterfaceActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);
            user = FirebaseAuth.getInstance().getCurrentUser();

            if (resultCode == RESULT_OK && user != null) {
                // Successfully signed in
                loginActivityVM.addUserToFirebaseRTDB(user);
                //String uid = mAuth.getCurrentUser().getUid();
                //Toast.makeText(this, "User Logged In\n" +uid, Toast.LENGTH_SHORT).show();
                gotoUserInterface();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}