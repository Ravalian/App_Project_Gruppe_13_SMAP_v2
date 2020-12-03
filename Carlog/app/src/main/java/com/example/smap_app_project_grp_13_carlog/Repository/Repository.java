package com.example.smap_app_project_grp_13_carlog.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.UserRTDB;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataAPI;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;

import com.example.smap_app_project_grp_13_carlog.R;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    //General Internals
    private Application application;
    private MutableLiveData<List<VehicleDataFirebase>> vehicles;
    private MutableLiveData<List<Log>> logs;
    private MutableLiveData<VehicleDataFirebase> vehicle;
    private MutableLiveData<List<UserRTDB>> users;


    //Firebase
    private DatabaseReference mDatabase;
    private DatabaseReference LogDatabase;
    private DatabaseReference userDatabase;

    //Networking
    private String BaseAPIURL = "https://v1.motorapi.dk/vehicles/";
    private RequestQueue queue;
    private VehicleDataAPI vehicleDataAPI;
    private String ACCESS_TOKEN = "pl2ycyljhhb2zxveesxl5xajupkm4v3n";
    private Boolean vehicleAlreadyRegistered = false;



    public Repository (Application app) {
        if (application==null) {
            application = app;
            mDatabase = FirebaseDatabase.getInstance().getReference("/vehicles");
            LogDatabase = FirebaseDatabase.getInstance().getReference("/logs");
            userDatabase = FirebaseDatabase.getInstance().getReference("/users");
            vehicles = new MutableLiveData<>();
            logs = new MutableLiveData<>();
            vehicle = new MutableLiveData<>();
            users = new MutableLiveData<>();

        } else{
            return;
        }
    }

    /////////////////// Firebase Handling /////////////////////


        /////////////////////Vehicles/////////////////////

    private void setupFirebaseListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("vehicles"); //in demo: "users/"+userID+"/places" and tell firebase to look at everything under places in specific user with userID

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //This is called when initialised and when data is changed.

                vehicles.setValue(toVehicles(snapshot));
                android.util.Log.d(Constants.REPOTAG, vehicles.getValue().get(0).getRegistrationNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "failed to read value"); //Jeg er ikke sikker på hvad dette er, det er i demo 2.
            }
        });
    }



    private void fireDatabaseYourVehicles(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("vehicles");

        reference.orderByChild("ownerID").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                vehicles.setValue(ToVehicles(snapshot));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "Couldn't get your cars");
            }
        });
    }

    private void getVehicleFromFirebase(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("vehicles");
        reference.orderByChild("registrationNumber").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                vehicle.setValue(snapshot.getValue(VehicleDataFirebase.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "Couldn't get the specified car");
            }
        });
    }


        ///////////////////////Logs///////////////////////////


    private void setupFireDatabaseYourLogsListener(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("logs");

        reference.orderByChild("user").equalTo(id.trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                logs.setValue(tologs(snapshot));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                logs.setValue(tologs(snapshot));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "failed to get your logs");
            }
        });
    }

    public void setupFirebaseLogsListener(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("logs"); //in demo: "users/"+userID+"/places" and tell firebase to look at everything under places in specific user with userID

        reference.orderByChild("vehicle").equalTo(id.trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                logs.setValue(tologs(snapshot));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                logs.setValue(tologs(snapshot));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "Couldn't get the logs to this car");
            }
        });
    }

    //////////////////////////////////////////////////////////

    ////////////////////////// Public User Database /////////////////////////////////
    //All user are stored in the DB so they can be accessed by other user to register

    //Get Users from Firbase
    public void GetUsersFromRTDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //This is called when initialised and when data is changed.
                users.setValue(toUsers(snapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.d("Error", "failed to read value"); //Jeg er ikke sikker på hvad dette er, det er i demo 2.
            }
        });
    }


    //Method for adding users to database
    public void AddUserToFirebaseRTDB(FirebaseUser user){

        //Check if the user is already registered
        if(users != null){
            for(UserRTDB eachUser : users.getValue()){
                if(eachUser.userId.equals(user.getUid())){
                    //User is already registered in the DB
                    return;
                }
            }
        }

        //Create a new user containing the information
        UserRTDB DbUser = new UserRTDB(user.getUid(), user.getDisplayName());

        //Add repository function which creates this user in DB
        userDatabase.child(DbUser.getUserId()).setValue(DbUser);
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////// API REQUESTS /////////////////////
    // website used for api data: https://www.motorapi.dk/ //

    //Get Vehicle from API by vin
    public void GetVehiclefromAPI(final String VehicleVIN){
        android.util.Log.d(Constants.REPOTAG, "GETVehiclefromAPI: baseurl + input: " + BaseAPIURL+VehicleVIN);


        if(queue == null){
            queue = Volley.newRequestQueue(application.getApplicationContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseAPIURL+VehicleVIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                android.util.Log.d(Constants.REPOTAG, "GETVehiclefromAPI response: " + response);
                //Do something here - Either store data internally or redirect to another function
                //Maybe call firebase add function
                parseAPIVehicleData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e(Constants.REPOTAG, "GetVehiclefromApi: Did not work!!!!");
            }
        }) {
            //Adding authentication header to API Request
            //Inspiration: https://stackoverflow.com/questions/44000212/how-to-send-authorization-header-in-android-using-volley-library
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                headers.put("X-AUTH-TOKEN", ACCESS_TOKEN);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    //Transfer only the info we need from the API data to a new model
    public void parseAPIVehicleData(String response) {
        //Parse the response from API and put it in an object of response
        Gson gson = new GsonBuilder().create();
        Type typeMyType = new TypeToken<VehicleDataAPI>(){}.getType(); //Inspiration: https://stackoverflow.com/questions/27253555/com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class
        vehicleDataAPI = gson.fromJson(response, typeMyType);

        //get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        android.util.Log.d(Constants.REPOTAG, ""+user.getDisplayName());

        //Check if the car is already in the firebase database
        //this has to be done on a unique key, this will be reg nr. (number plate)
        if (vehicles.getValue()!=null) {
            for (VehicleDataFirebase vehicle : vehicles.getValue()) {
                if (vehicle.getRegistrationNumber().equals(vehicleDataAPI.getRegistrationNumber())) {
                    //the vehicle is already in the database
                    //set boolean to true
                    android.util.Log.d(Constants.REPOTAG, "Do we enter here?");
                    vehicleAlreadyRegistered = true;
                }
            }
        }

        //If the car does not exists
        //Make If statement when check is done
        if(vehicleAlreadyRegistered){
            //Tell the user a car with that reg nr. is already in the database
            android.util.Log.d(Constants.REPOTAG, "Vehicle already registered: " + vehicleDataAPI.getRegistrationNumber());
            Toast.makeText(application.getApplicationContext(), R.string.VehicleAlreadyRegisteredString, Toast.LENGTH_SHORT).show();
            vehicleAlreadyRegistered = false;
        } else{
            VehicleDataFirebase newVehicle = new VehicleDataFirebase();
            newVehicle.setOwner(user.getDisplayName()); // this has to the the UserID from the Firebase Authentication
            newVehicle.setOwnerID(user.getUid());
            newVehicle.setRegistrationNumber(vehicleDataAPI.getRegistrationNumber());
            newVehicle.setTotalWeight(vehicleDataAPI.getTotalWeight());
            newVehicle.setSeats(vehicleDataAPI.getSeats());
            newVehicle.setDoors(vehicleDataAPI.getDoors());
            newVehicle.setMake(vehicleDataAPI.getMake());
            newVehicle.setModel(vehicleDataAPI.getModel());
            newVehicle.setVariant(vehicleDataAPI.getVariant());
            newVehicle.setModelType(vehicleDataAPI.getModelType());
            newVehicle.setModelYear(vehicleDataAPI.getModelYear());
            newVehicle.setColor(vehicleDataAPI.getColor());
            newVehicle.setChassisType(vehicleDataAPI.getChassisType());
            newVehicle.setEngineCylinders(vehicleDataAPI.getEngineCylinders());
            float hp =  Float.parseFloat(vehicleDataAPI.getEnginePower())*(float)1.34102209;
            newVehicle.setEnginePower(String.valueOf((int)hp)); //Vi skal gange med 1.341 for at få til HP
            newVehicle.setEngineVolume(vehicleDataAPI.getEngineVolume());
            newVehicle.setFuelType(vehicleDataAPI.getFuelType());

            //Adding car to Firebase
            mDatabase.child(newVehicle.getRegistrationNumber()).setValue(newVehicle);
            android.util.Log.d(Constants.REPOTAG, "Vehicle added to database: " + newVehicle.getRegistrationNumber());
        }
    }
    ////////////////////////////////////////////////////////

    ////////////////////Get functions to VMs////////////////

    public MutableLiveData<List<VehicleDataFirebase>> getVehicles(){
        setupFirebaseListener();
        return vehicles;
    }

    public LiveData<List<Log>> getLogs(String id) {
        setupFirebaseLogsListener(id);
        return logs;
    }

    public MutableLiveData<VehicleDataFirebase> getvehicle(String id) {
        getVehicleFromFirebase(id);
        return vehicle;
    }


    public void saveLog(Log log) {
        String ID = LogDatabase.push().getKey(); //https://firebase.google.com/docs/database/admin/save-data#getting-the-unique-key-generated-by-push
        LogDatabase.child(ID).setValue(log);

    }

    public LiveData<List<Log>> getYourLogs(String id) {
        setupFireDatabaseYourLogsListener(id);
        return logs;
    }

    public LiveData<List<VehicleDataFirebase>> getYourVehicles(String id){
        fireDatabaseYourVehicles(id);
        return vehicles;
    }


    ////////////////////////////////////////////////////////
    //////////Private conversion functions//////////////////

    private List<VehicleDataFirebase> toVehicles(DataSnapshot snapshot) {
        ArrayList V = new ArrayList();
        Iterable<DataSnapshot> snapshots = snapshot.getChildren();
        while(snapshots.iterator().hasNext()){
            VehicleDataFirebase ve = snapshots.iterator().next().getValue(VehicleDataFirebase.class);
            V.add(ve);
        }
        return V;
    }

    private List<Log> tologs(DataSnapshot snapshot){
        ArrayList L = new ArrayList();

        if (logs.getValue()!=null) {
            for (Log l :
                    logs.getValue()) {
                L.add(l);
            }
        }
        L.add(snapshot.getValue(Log.class));
        return L;
    }

    private List<VehicleDataFirebase> ToVehicles(DataSnapshot snapshot) {
        ArrayList L = new ArrayList();

        if (vehicles.getValue()!=null) {
            for (VehicleDataFirebase l :
                    vehicles.getValue()) {
                L.add(l);
            }
        }
        L.add(snapshot.getValue(VehicleDataFirebase.class));
        return L;
    }

    private List<UserRTDB> toUsers(DataSnapshot snapshot) {
        ArrayList L = new ArrayList();

        if (users.getValue()!=null){
            for(UserRTDB i : users.getValue()) {
                L.add(i);
            }
        }
        L.add(snapshot.getValue(UserRTDB.class));
        return L;
    }


}
