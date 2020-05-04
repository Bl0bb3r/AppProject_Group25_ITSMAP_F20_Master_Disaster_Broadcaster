package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.DisasterAdapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.emblem_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mydisasters_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mydisasters_fragment extends Fragment implements DisasterAdapter.OnDisasterListener {

    //Service
    private Intent serviceIntent;
    private ServiceConnection disasterServiceConnection;
    private DisasterService disasterService;
    private boolean isBound;
    //
    //ListView listViewMyDisasters;
    RecyclerView recyclerViewDisasters;
    GridView gridViewEmblems;
    Button btn_back;
    TextView textView_totalPoints;
    TextView textView_rank;
    private DisasterAdapter disasterAdapter;
    private emblem_adapter emblemAdapter;
    //list with the submitted disasters
    ArrayList<Disaster> disasters;
    //totalpoints
    double Totalpoints = 0;

    //Gson
    Gson gson;

    //fragments
    DisasterDetails disasterDetails;
    View rootView;

    public mydisasters_fragment() {
        // Required empty public constructor
    }


    public static mydisasters_fragment newInstance() {
        mydisasters_fragment fragment = new mydisasters_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //if any parameters
        }
        gson = new Gson();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        disasters = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_mydisasters_fragment, container, false);
        recyclerViewDisasters = (RecyclerView) rootView.findViewById(R.id.mydisasters_recyclerview);


        textView_totalPoints = rootView.findViewById(R.id.textview_totalPoints);
        textView_rank = rootView.findViewById(R.id.textview_worldRank);

        btn_back = (Button) rootView.findViewById(R.id.back_btn);
        gridViewEmblems = (GridView) rootView.findViewById(R.id.mydisasters_gridview);

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
        });


        gridViewEmblems.setNumColumns(4);



        gridViewEmblems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: emblem click?

            }
        });



        recyclerViewDisasters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: disaster click show disaster
            }
        });



        // Inflate the layout for this fragment
        return rootView;


    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }
    @Override
    public void onStart() {

        super.onStart();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if (disasters != null)
        {
            disasters.clear();
        }

        if (((MainActivity)getActivity()).isBound)
        {
            disasters = (ArrayList<Disaster>) ((MainActivity) getActivity()).disasterService.UsersDisasters;
            Log.wtf("MyDisasters", "UserDisaster size: "+disasters.size());
            for (Disaster disaster : disasters)
            {
                Totalpoints += disaster.getPoints();
            }
            recyclerViewDisasters = (RecyclerView) rootView.findViewById(R.id.mydisasters_recyclerview);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL);
            dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_space));
            recyclerViewDisasters.addItemDecoration(dividerItemDecoration);
            disasterAdapter = new DisasterAdapter(disasters, this);
            disasterAdapter.notifyDataSetChanged();

            recyclerViewDisasters.setAdapter(disasterAdapter);
            recyclerViewDisasters.setLayoutManager(new LinearLayoutManager(getActivity()));

            emblemAdapter = new emblem_adapter(getContext(),disasters);
            emblemAdapter.notifyDataSetChanged();
            gridViewEmblems.setAdapter(emblemAdapter);


            textView_totalPoints.setText(""+Totalpoints);
            textView_rank.setText("1");
        }
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDisasterClick(int position) {
        Log.wtf("MyDisasters", "Recyclerview clicked position: "+position);
        String disaster = gson.toJson(disasters.get(position));
        disasterDetails = DisasterDetails.newInstance(disaster);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, disasterDetails).addToBackStack(null).commit();
    }
}
