package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.emblem_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.mydisasters_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mydisasters_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mydisasters_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listViewMyDisasters;
    GridView gridViewEmblems;

    private mydisasters_adapter mydisastersAdapter;
    private emblem_adapter emblemAdapter;

    ArrayList<Disaster> disasters;

    public mydisasters_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mydisasters_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mydisasters_fragment newInstance(String param1, String param2) {
        mydisasters_fragment fragment = new mydisasters_fragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //For testing purpose
        Disaster disaster1 = new Disaster();
        disaster1.setId("1");
        disaster1.setDisaster_latitude(123);
        disaster1.setDisaster_longitude(123);
        disaster1.setDisasterImage(String.valueOf(R.drawable.fire));
        disaster1.setDisasterType(DisasterType.Wildfire);
        disaster1.setUserImage(String.valueOf(R.drawable.disasterdude));
        disaster1.setPoints(12);

        Disaster disaster2 = new Disaster();
        disaster2.setId("2");
        disaster2.setDisaster_latitude(123);
        disaster2.setDisaster_longitude(123);
        disaster2.setDisasterImage(String.valueOf(R.drawable.cloud));
        disaster2.setDisasterType(DisasterType.Landslide);
        disaster2.setUserImage(String.valueOf(R.drawable.disasterdude));
        disaster2.setPoints(190903);

        Disaster disaster3 = new Disaster();
        disaster3.setId("3");
        disaster3.setDisaster_latitude(123);
        disaster3.setDisaster_longitude(123);
        disaster3.setDisasterImage(String.valueOf(R.drawable.bolt));
        disaster3.setDisasterType(DisasterType.Blizzard);
        disaster3.setUserImage(String.valueOf(R.drawable.disasterdude));
        disaster3.setPoints(232);

        disasters = new ArrayList<>();

        disasters.add(disaster1);
        disasters.add(disaster2);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);
        disasters.add(disaster3);

        View rootView = inflater.inflate(R.layout.fragment_mydisasters_fragment, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        gridViewEmblems = (GridView) rootView.findViewById(R.id.mydisasters_recyclerview);
        gridViewEmblems.setNumColumns(4);
        emblemAdapter = new emblem_adapter(getContext(),disasters);
        emblemAdapter.notifyDataSetChanged();
        gridViewEmblems.setAdapter(emblemAdapter);


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
}
