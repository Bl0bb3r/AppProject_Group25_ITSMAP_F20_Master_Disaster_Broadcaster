package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;

public class MainActivity extends AppCompatActivity {

    //Service
    Intent serviceIntent;
    ServiceConnection disasterServiceConnection;
    DisasterService disasterService;
    private boolean isBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start service
        serviceIntent = new Intent(this, DisasterService.class);
        startService(serviceIntent);
        Log.wtf("MainActivity","StartService called");

        //bind service
        //DisasterServiceConnection();
        //bindService(serviceIntent, disasterServiceConnection, Context.BIND_AUTO_CREATE);


        //check Fragment
        if (findViewById(R.id.mainactivity_framelayout) != null)
        {
            if (savedInstanceState != null)
            {
                return;
            }

            home_fragment home_fragment = new home_fragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, home_fragment).commit();
        }

    }

    private void DisasterServiceConnection()
    {
        disasterServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DisasterService.DisasterServiceBinder binder = (DisasterService.DisasterServiceBinder) service;
                disasterService = binder.getService();
                isBound = true;
                Log.wtf("Binder", "MainActivity bound to service");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
                Log.wtf("Binder", "MainActivity unbound to service");
            }
        };
    }
}
