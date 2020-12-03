package com.example.smap_app_project_grp_13_carlog.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;

import com.example.smap_app_project_grp_13_carlog.Models.Log;
import com.example.smap_app_project_grp_13_carlog.Models.VehicleDataFirebase;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Repository.Repository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ForegroundService extends Service {
    
    private Repository repository;
    private boolean started;
    private ExecutorService executor;
    private List<VehicleDataFirebase> vehicles;
    private NotificationManager notificationManager;
    public static final String SERVICE_CHANNEL = "serviceChannel";
    public static final String NOTIFICATION_CHANNEL = "notificationChannel";
    public static final int SERVICE_NOTIFICATION_ID = 42;
    public static final int NOTIFICATION_NOTIFICATION_ID = 24;
    
    
    @Override
    public void onCreate(){
        super.onCreate();
        //displayNotification("se mig");
        repository = new Repository(getApplication());
        repository.getLogsToCars(FirebaseAuth.getInstance().getCurrentUser().getUid()).observeForever(new Observer<Log>() {
            @Override
            public void onChanged(Log log) {
                displayNotification(log);
            }
        });
        started = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(SERVICE_CHANNEL,"Foreground Service", NotificationManager.IMPORTANCE_LOW);
            NotificationChannel channel2 = new NotificationChannel(NOTIFICATION_CHANNEL,"Country notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }

        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle("Car Log")
                .setContentText("context text")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Hvad er du?")
                .build();

        startForeground(SERVICE_NOTIFICATION_ID, notification);

        //displayNotification("SÅ VIRK DOG");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        started = false;
    }

    private void displayNotification(Log log) {

        Notification updatenotofication = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setContentTitle(log.userName + " " + getString(R.string.not_Have) + " " + log.getvehicle())
                .setContentText(log.getLogDescription())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setColor( Color.rgb(255, 0, 0))
                .build();
        notificationManager.notify(NOTIFICATION_NOTIFICATION_ID, updatenotofication);
        //android.util.Log.d("Tester", log);
    }
}
