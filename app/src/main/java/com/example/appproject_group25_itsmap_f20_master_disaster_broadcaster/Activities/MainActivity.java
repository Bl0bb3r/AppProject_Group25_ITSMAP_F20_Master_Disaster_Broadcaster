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

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.mydisasters_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    //Service
    public Intent serviceIntent;
    public ServiceConnection disasterServiceConnection;
    public DisasterService disasterService;
    public LocalBroadcastManager localBroadcastManager;
    private boolean isBound;

    //MainActivity view
    ProgressBar progressBar;

    //HomeFragment
    Button btn_myDisasters;

    //disaster
    Disaster disaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disaster = new Disaster();

        //Start service
        serviceIntent = new Intent(this, DisasterService.class);
        startService(serviceIntent);
        Log.wtf("MainActivity","StartService called");

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
                disaster.setTitle("Firebase test4");
                disaster.setDisasterType(DisasterType.Thunderstorm);
                disaster.setLonDisaster(55.0);
                disaster.setLatDisaster(-33.0);
                disaster.setLatUser(40.0);
                disaster.setLonUser(-33.0);
                disaster.setUserImage(""+R.drawable.disasterdude);
                disaster.setEmblemImage(""+R.drawable.storm);


                //disasterService.InsertDisaster(disaster);



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
                    disasterService.GetAllDisasters();
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
            //this is broadcast when the service is started for the first time.
            else if (intent.getAction().equals("FIRST_START")) {
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
}
