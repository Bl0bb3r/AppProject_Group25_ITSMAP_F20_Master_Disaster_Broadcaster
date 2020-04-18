package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
