package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.camera_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.mydisasters_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.submitDisaster_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements camera_fragment.CameraFragmentListener{

    //Service
    public Intent serviceIntent;
    public ServiceConnection disasterServiceConnection;
    public DisasterService disasterService;
    public LocalBroadcastManager localBroadcastManager;

    //fragment
       submitDisaster_fragment submit;
       camera_fragment camera;
       home_fragment home;
       mydisasters_fragment userDisasters;


    private boolean isBound;
    //MainActivity view
    ProgressBar progressBar;
    //HomeFragment
    Button btn_myDisasters;
    //disaster
    Disaster disaster;
    //User ID
    public String userId;

    public String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userID");
        disaster = new Disaster();
        serviceIntent = new Intent(this, DisasterService.class);
        // initiate progress bar and start button
        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        //visible the progress bar
        progressBar.setVisibility(View.VISIBLE);
        //check Fragment
        if (findViewById(R.id.mainactivity_framelayout) != null)
        {
            if (savedInstanceState != null)
            {
                return;
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        //setup broadcast filters and register it.
        IntentFilter filter = new IntentFilter();
        filter.addAction("NewEvent");
        filter.addAction("GetALLDB");
        filter.addAction("GoToCamera");
        filter.addAction("GoToSubmit");
        filter.addAction("ReturnFromCamera");
        filter.addAction("ReturnFromSubmit");
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(DisasterReceiver, filter);

        //bind service
        DisasterServiceConnection();
        bindService(serviceIntent, disasterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void DisasterServiceConnection()
    {
        disasterServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DisasterService.DisasterServiceBinder binder = (DisasterService.DisasterServiceBinder) service;
                disasterService = binder.getService();
                isBound = true;
                //Toast.makeText(disasterService, "user ID: "+disaster.getId(), Toast.LENGTH_SHORT).show();

                //firebase test TODO: DELETE THIS
                disaster.setDistance(21);
                disaster.setTitle("Firebase test9");
                disaster.setDisasterType(DisasterType.Thunderstorm);
                disaster.setLonDisaster(55.0);
                disaster.setLatDisaster(-33.0);
                disaster.setLatUser(40.0);
                disaster.setLonUser(-33.0);
                disaster.setUserImage(""+R.drawable.disasterdude);
                disaster.setEmblemImage(""+R.drawable.storm);
                Date date = new Date();
                date.getTime();
                disaster.setDate(date);

                //disasterService.InsertDisaster(disaster, userId);


                disasterService.GetAllDisasters(userId);
                disasterService.sendRequest(getApplicationContext());
                Log.wtf("Binder", "MainActivity bound to service");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
                Log.wtf("Binder", "MainActivity unbound to service");
            }
        };
    }
    private BroadcastReceiver DisasterReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            home_fragment home_fragment = new home_fragment();
            //TODO: once events has been requested dont request the same events again
            if (intent.getAction().equals("NewEvent"))
            {
                //Hide progressbar
                progressBar.setVisibility(View.INVISIBLE);

                //Toast.makeText(context, "New DATA", Toast.LENGTH_SHORT).show();


                getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, home_fragment).commit();

                //when homepage is loaded get disasters
                if (disasterService.UsersDisasters.size() < 1) {
                    disasterService.GetAllDisasters(userId);
                }

            }
            else if (intent.getAction().equals("GetALLDB")) {
                Log.wtf("UsersDisasters", "size: "+disasterService.UsersDisasters.size());

                    //home_fragment.SetMyDisastersBtnVisible();

            }
            else if (intent.getAction().equals("NoEvents")) {
                Log.wtf("NoEvents", "There was no events at this time");

                progressBar.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, home_fragment).commit();

            }
            else if(intent.getAction().equals("GoToCamera")) {
                camera = camera_fragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, camera).addToBackStack(null).commit();
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
    public void onImageSent(String input) {

            submit.updateImage(input);

    }
}
