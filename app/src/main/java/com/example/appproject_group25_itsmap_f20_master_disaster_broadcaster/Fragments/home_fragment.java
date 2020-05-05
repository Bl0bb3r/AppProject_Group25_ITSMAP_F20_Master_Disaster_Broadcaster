package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;


public class home_fragment extends Fragment {



    private boolean isBound;

    Button ongoing_btn;
    Button mydisasters_btn;
    Button rankings_btn;
    Button profile_btn;
    Button logout_btn;


    public home_fragment() {
        // Required empty public constructor
    }


    public static home_fragment newInstance() {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_fragments, container, false);

        //get components
        ongoing_btn = (Button) rootView.findViewById(R.id.btn_ongoing);
        mydisasters_btn = (Button) rootView.findViewById(R.id.btn_mydisasters);
        rankings_btn = (Button) rootView.findViewById(R.id.btn_rankings);
        profile_btn = (Button) rootView.findViewById(R.id.btn_profile);
        logout_btn = (Button) rootView.findViewById(R.id.btn_logout);

        //Onclick for all buttons
        mydisasters_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.mainactivity_framelayout, new mydisasters_fragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        rankings_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.mainactivity_framelayout, RankingsFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.mainactivity_framelayout, MyProfileFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ongoing_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).CheckLocationPermission();

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

}
