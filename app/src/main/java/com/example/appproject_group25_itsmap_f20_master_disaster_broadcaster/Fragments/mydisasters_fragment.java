package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.emblem_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.mydisasters_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mydisasters_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mydisasters_fragment extends Fragment {

    //mainActivity
    MainActivity mainActivity;
    //Service
    private Intent serviceIntent;
    private ServiceConnection disasterServiceConnection;
    private DisasterService disasterService;
    private boolean isBound;
    //
    ListView listViewMyDisasters;
    GridView gridViewEmblems;
    Button btn_back;
    TextView textView_totalPoints;
    TextView textView_rank;
    private mydisasters_adapter mydisastersAdapter;
    private emblem_adapter emblemAdapter;
    //list with the submitted disasters
    ArrayList<Disaster> disasters;
    //totalpoints
    double Totalpoints;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mydisasters_fragment, container, false);

      if (disasters != null)
      {
          disasters.clear();
      }
        disasters = mainActivity.disasterService.UsersDisasters;


        textView_totalPoints = rootView.findViewById(R.id.textview_totalPoints);
        textView_rank = rootView.findViewById(R.id.textview_worldRank);

        btn_back = (Button) rootView.findViewById(R.id.back_btn);
        gridViewEmblems = (GridView) rootView.findViewById(R.id.mydisasters_recyclerview);

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        gridViewEmblems.setNumColumns(4);
        emblemAdapter = new emblem_adapter(getContext(),disasters);
        emblemAdapter.notifyDataSetChanged();
        gridViewEmblems.setAdapter(emblemAdapter);

        ////////////////////////////////////////////////////


        for (Disaster disaster : disasters)
        {
            Totalpoints += disaster.getPoints();
        }
        textView_totalPoints.setText(""+Totalpoints);
        textView_rank.setText("1");
        //////////////////////////////////////////////

        gridViewEmblems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: emblem click?

            }
        });

        listViewMyDisasters = (ListView) rootView.findViewById(R.id.mydisasters_listview);
        mydisastersAdapter = new mydisasters_adapter(getContext(), disasters);
        mydisastersAdapter.notifyDataSetChanged();

        listViewMyDisasters.setAdapter(mydisastersAdapter);

        listViewMyDisasters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: go to SubmitDisasterFragment

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
    @Override
    public void onStart() {

        super.onStart();

    }
    @Override
    public void onStop() {

        super.onStop();
    }

}
