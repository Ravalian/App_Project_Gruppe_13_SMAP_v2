package com.example.smap_app_project_grp_13_carlog.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.smap_app_project_grp_13_carlog.Repository.Repository;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityVM extends AndroidViewModel {

    //Repository
    private Repository repository;

    //Constructor
    public LoginActivityVM(Application app) {
        super(app);
        repository = new Repository(app);
    }

    //Method to check already registered users
    public void checkUsersRTDB(){
        repository.GetUsersFromRTDB();
    }

    //Method for adding user to Firebase Realtime Database
    public void addUserToFirebaseRTDB(FirebaseUser user){
        repository.AddUserToFirebaseRTDB(user);
    }
}
