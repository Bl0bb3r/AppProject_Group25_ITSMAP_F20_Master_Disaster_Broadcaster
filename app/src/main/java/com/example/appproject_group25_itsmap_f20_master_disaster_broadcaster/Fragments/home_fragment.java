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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {

    Button ongoing_btn;
    Button mydisasters_btn;
    Button rankings_btn;
    Button profile_btn;
    Button logout_btn;

    private MainActivity mainActivity;

    public home_fragment() {
        // Required empty public constructor
    }


    public static home_fragment newInstance() {
        home_fragment fragment = new home_fragment();
        //put arguments
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments here
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

        //mydisasters_btn.setVisibility(View.INVISIBLE);
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

                //FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                //FragmentTransaction transaction = fragmentmanager.beginTransaction();

                //transaction.replace(R.id.mainactivity_framelayout, new mydisasters_fragment());
                //transaction.addToBackStack(null);
                //transaction.commit();
                //mainActivity.disasterService.sendRequest(getContext());
            }
        });

        ongoing_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.mainactivity_framelayout, new ongoing_fragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        mainActivity = (MainActivity) context;

    }
}
