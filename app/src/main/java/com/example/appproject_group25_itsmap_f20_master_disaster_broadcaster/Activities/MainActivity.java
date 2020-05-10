package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.Login;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.camera_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.mydisasters_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.ongoing_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.submitDisaster_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Global;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.Repository;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements camera_fragment.CameraFragmentListener{


    //firebase
    public FirebaseFirestore db;
    public FirebaseStorage storage;
    public StorageReference storageRef;
    //Firebase authentication variable
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public Repository repository;

    //Service
    public Intent serviceIntent;
    public ServiceConnection disasterServiceConnection;
    public DisasterService disasterService;
    public LocalBroadcastManager localBroadcastManager;

    //fragments
       submitDisaster_fragment submit;
       camera_fragment camera;
       home_fragment home;
       mydisasters_fragment userDisasters;


    public boolean isBound;
    //MainActivity view
    ProgressBar progressBar;
    //HomeFragment
    Button btn_myDisasters;
    //disaster
    Disaster disaster;

    //filename from camerafragment
    public String fileName;

    Bundle saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saved = savedInstanceState;
        serviceIntent = new Intent(this, DisasterService.class);
        //bind service
        DisasterServiceConnection();
        bindService(serviceIntent, disasterServiceConnection, Context.BIND_AUTO_CREATE);
        //setup broadcast filters and register it.

        //Firebase
        // Access a Cloud Firestore instance from your Activity
        FirebaseApp.initializeApp(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://disastermasterbroadcaster.appspot.com/");
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        // Create a storage reference from our app
        storageRef = storage.getReference();

        repository = new Repository(db, storage, storageRef, currentUser, getApplicationContext());


        IntentFilter filter = new IntentFilter();
        filter.addAction("GoToCamera");
        filter.addAction("GoToSubmit");
        filter.addAction("ReturnFromCamera");
        filter.addAction("ReturnFromSubmit");
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(DisasterReceiver, filter);



        //check Fragment
        CheckStoragePermission();
        disaster = new Disaster();


        if (findViewById(R.id.mainactivity_framelayout) != null)
        {
            if (savedInstanceState != null)
            {
                return;
            }
             home = new home_fragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, home).commit();

        }



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void DisasterServiceConnection()
    {
        disasterServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DisasterService.DisasterServiceBinder binder = (DisasterService.DisasterServiceBinder) service;
                disasterService = binder.getService();
                isBound = true;
                disasterService.sendRequest(getApplicationContext());
                Log.wtf("Binder", "MainActivity bound to service -- isBound: "+isBound);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
                Log.wtf("Binder", "MainActivity unbound to service -- isBound: "+isBound);
            }
        };
    }
    private BroadcastReceiver DisasterReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {



            if (intent.getAction().equals("NoEvents")) {
                Log.wtf("NoEvents", "There was no events at this time");

                progressBar.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, home).commit();

            }
            else if(intent.getAction().equals("GoToCamera")) {

                CheckCamAudioStoragePermission();

            }
            else if(intent.getAction().equals("GoToSubmit")) {

                String disaster = intent.getStringExtra("Disaster");

                submit = submitDisaster_fragment.newInstance(disaster);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, submit).addToBackStack(null).commit();
            }

            else if (intent.getAction().equals("ReturnFromCamera")) {

                FragmentManager fragmentmanager = getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
            else if (intent.getAction().equals("ReturnFromSubmit")) {
                FragmentManager fragmentmanager = getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
            else if (intent.getAction().equals("ServiceBound")) {
                    isBound = true;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound)
        {
            unbindService(disasterServiceConnection);
            isBound = false;
            localBroadcastManager.unregisterReceiver(DisasterReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
//Get filename from camerafragment and pass it on to SubmitDisasterFragment
    @Override
    public void onImageSent(String input) {

            submit.updateImage(input);

    }

    //permissions
    public void CheckLocationPermission(){
        // Here, thisActivity is the current activity
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, String.valueOf(permissions)) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    String.valueOf(permissions))) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this).setTitle("Permissions needed").setMessage("this permission is needed. The core functionality of the app wont work").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, Global.REQUEST_FINE_LOCATION);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        permissions,
                        Global.REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }
    }
    public void CheckStoragePermission()
    {
        // Here, thisActivity is the current activity
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, String.valueOf(permissions)) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    String.valueOf(permissions))) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("this permission is needed and the app wont function without it").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, Global.REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        permissions,
                        Global.REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }
    }

    public void CheckCamAudioStoragePermission(){
        // Here, thisActivity is the current activity
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, String.valueOf(permissions)) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    String.valueOf(permissions))) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this).setTitle("Permissions needed").setMessage("these permissions is needed and the app wont function without them").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, Global.REQUEST_CAMERA_AUDIO_STORAGE);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        permissions,
                        Global.REQUEST_CAMERA_AUDIO_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Global.REQUEST_CAM_AUDIO_STORAGE_MIC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, ""+R.string.permissions_granted, Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, ""+R.string.permissions_notgranted, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case Global.REQUEST_CAMERA_AUDIO_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    camera = camera_fragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, camera).addToBackStack(null).commit();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //ActivityCompat.requestPermissions(MainActivity.this, permissions, Global.REQUEST_CAMERA_AUDIO_STORAGE);
                }
                return;
            }
            case Global.REQUEST_FINE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    FragmentManager fragmentmanager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentmanager.beginTransaction();

                    transaction.replace(R.id.mainactivity_framelayout, new ongoing_fragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case Global.REQUEST_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    ActivityCompat.requestPermissions(MainActivity.this, permissions, Global.REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return;
            }
        }
    }

}
