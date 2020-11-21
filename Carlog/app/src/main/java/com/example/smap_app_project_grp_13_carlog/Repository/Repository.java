package com.example.smap_app_project_grp_13_carlog.Repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataAPI;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    //General Internals
    private Application application;
    private LiveData<List<VehicleDataFirebase>> vehicles;
    Map<String, VehicleDataFirebase> Vehicles;

    //Firebase
    private DatabaseReference mDatabase;
    DatabaseReference vehiclesRef;

    //Networking
    private String BaseAPIURL = "https://v1.motorapi.dk/vehicles/";
    private RequestQueue queue;
    private VehicleDataAPI vehicleDataAPI;
    private String ACCESS_TOKEN = "pl2ycyljhhb2zxveesxl5xajupkm4v3n";



    public Repository (Application app) {
        application = app;
        mDatabase = FirebaseDatabase.getInstance().getReference("/vehicles");
        vehiclesRef = mDatabase.child("vehicles");
        Vehicles = new HashMap<>();
        setupFirebaseListener();
    }

    /////////////////// Firebase Handling /////////////////////

    private void setupFirebaseListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("/vehicles"); //in demo: "users/"+userID+"/places" and tell firebase to look at everything under places in specific user with userID

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //This is called when initialised and when data is changed.
                //Log.d("Tester", "Hejsa:  "+snapshot.getChildren().iterator().next().getValue().toString());


                //vehicles = new MutableLiveData();
                //Iterable<DataSnapshot> snapshots = snapshot.getChildren();
                //while(snapshots.iterator().hasNext()){
                    //vehicles.postValue(snapshots.iterator().next().getValue(VehicleDataFirebase.class));
                //}
                List<VehicleDataFirebase> v = toVehicles(snapshot);
                Log.d("Tester", v.get(0).getRegistrationNumber()+" "+v.get(1).registrationNumber+" "+v.get(2).getRegistrationNumber());
                //vehicles.postValue(toVehicles(snapshot));
                //Log.d("Tester", ""+vehicles.toString());
                /*if(vehicles.size()>0){
                    if(adapter==null){
                        adapter = new RegisteredVehiclesAdapter(vehicles, RegisteredVehicles.this);
                        rcvList.setAdapter(adapter);
                    }else{
                        adapter.setList(vehicles);
                        adapter.notifyDataSetChanged();
                    }
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "failed to read value"); //Jeg er ikke sikker på hvad dette er, det er i demo 2.
            }
        });
    }

    private List<VehicleDataFirebase> toVehicles(DataSnapshot snapshot) {
        ArrayList V = new ArrayList();
        Iterable<DataSnapshot> snapshots = snapshot.getChildren();
        while(snapshots.iterator().hasNext()){
            VehicleDataFirebase ve = snapshots.iterator().next().getValue(VehicleDataFirebase.class);
            V.add(ve);
            Vehicles.put(ve.registrationNumber, ve);
        }
        return V;
    }


    //////////////////////////////////////////////////////////


    ////////////////////// API REQUESTS /////////////////////
    // website used for api data: https://www.motorapi.dk/ //

    //Get Vehicle from API by vin
    public void GetVehiclefromAPI(final String VehicleVIN){
        Log.d(Constants.REPOTAG, "GETVehiclefromAPI: baseurl + input: " + BaseAPIURL+VehicleVIN);


        if(queue == null){
            queue = Volley.newRequestQueue(application.getApplicationContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseAPIURL+VehicleVIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.REPOTAG, "GETVehiclefromAPI response: " + response);
                //Do something here - Either store data internally or redirect to another function
                //Maybe call firebase add function
                parseAPIVehicleData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.REPOTAG, "GetVehiclefromApi: Did not work!!!!");
            }
        }) {
            //Adding authentication header to API Request
            //Inspiration: https://stackoverflow.com/questions/44000212/how-to-send-authorization-header-in-android-using-volley-library
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                headers.put("X-AUTH-TOKEN", ACCESS_TOKEN);
                //headers.put("Authorization", ACCESS_TOKEN);
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

        //Check if the car is already in the firebase database
        //this has to be done on a unique key, this will be reg nr. (number plate)
        //Inspiration: https://stackoverflow.com/questions/39800547/read-data-from-firebase-database
        //Build a reference to the object
        /*DatabaseReference ref = mDatabase.child("Vehicles"); //Vehicles is not a child in the database yet, so it does not work yet!
        Query vehicleQuary = ref.orderByChild(registrationNumber).equalTo(vehicleDataAPI.getRegistrationNumber())

        //Waiting a little with this
         */
        boolean temp = false;

        //If the car does not exists
        //Make If statement when check is done
        if(temp){
            //Tell the user a car with that reg nr. is already in the database
        } else{
            VehicleDataFirebase newVehicle = new VehicleDataFirebase();
            newVehicle.setOwner("test"); // this has to the the UserID from the Firebase Authentication
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
            newVehicle.setEnginePower(vehicleDataAPI.getEnginePower());
            newVehicle.setEngineVolume(vehicleDataAPI.getEngineVolume());
            newVehicle.setFuelType(vehicleDataAPI.getFuelType());

            //Adding car to Firebase

            mDatabase.child(newVehicle.getRegistrationNumber()).setValue(newVehicle);
            Log.d(Constants.REPOTAG, "Vehicle added to database: " + newVehicle.getRegistrationNumber());
        }
    }







    ////////////////////////////////////////////////////////



}
