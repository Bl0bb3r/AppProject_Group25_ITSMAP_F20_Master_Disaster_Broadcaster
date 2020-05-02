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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MainActivity mainActivity;

    Intent serviceIntent;
    ServiceConnection disasterServiceConnection;
    DisasterService disasterService;
    private boolean isBound;

    Button ongoing_btn;
    Button mydisasters_btn;
    Button rankings_btn;
    Button profile_btn;
    Button logout_btn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragments.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        serviceIntent = new Intent(getActivity(), DisasterService.class);
        //bind service
        DisasterServiceConnection();
        getActivity().bindService(serviceIntent, disasterServiceConnection, Context.BIND_AUTO_CREATE);
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

                //FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                //FragmentTransaction transaction = fragmentmanager.beginTransaction();

                //transaction.replace(R.id.mainactivity_framelayout, new mydisasters_fragment());
                //transaction.addToBackStack(null);
                //transaction.commit();
                String id = disasterService.UsersDisasters.get(0).getId();
                disasterService.GetDisaster(id);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        mainActivity = (MainActivity) context;

    }
    private void DisasterServiceConnection()
    {
        disasterServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DisasterService.DisasterServiceBinder binder = (DisasterService.DisasterServiceBinder) service;
                disasterService = binder.getService();
                isBound = true;
                Log.wtf("Binder", "HomeFragment bound to service");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
                Log.wtf("Binder", "HomeFragment unbound to service");
            }
        };
    }
}
