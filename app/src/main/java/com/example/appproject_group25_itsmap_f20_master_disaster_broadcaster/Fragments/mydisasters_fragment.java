package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.EmblemAdapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class mydisasters_fragment extends Fragment implements DisasterAdapter.OnDisasterListener, EmblemAdapter.OnEmblemListener, Repository.UserCallBack {
    private StorageReference storageRef;
    //FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerViewDisasters;
    RecyclerView gridViewEmblems;
    Button btn_back;
    TextView textView_totalPoints;
    TextView textView_rank;
    private DisasterAdapter disasterAdapter;
    private EmblemAdapter emblemAdapter;
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
        gridViewEmblems = (RecyclerView) rootView.findViewById(R.id.mydisasters_gridview);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_space));

        recyclerViewDisasters.addItemDecoration(dividerItemDecoration);
        RecyclerViewSetup();
        recyclerViewDisasters.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDisasters.setAdapter(disasterAdapter);
        disasterAdapter.notifyDataSetChanged();



        gridViewEmblems.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gridViewEmblems.setAdapter(emblemAdapter);
        emblemAdapter.notifyDataSetChanged();


        textView_totalPoints = rootView.findViewById(R.id.textview_totalPoints);
        textView_rank = rootView.findViewById(R.id.textview_worldRank);

        ((MainActivity)getActivity()).repository.GetUser(this);
        btn_back = (Button) rootView.findViewById(R.id.back_btn);


        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
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
        disasterAdapter.startListening();
        emblemAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {

        super.onStop();
        disasterAdapter.stopListening();
        emblemAdapter.stopListening();
    }

    @Override
    public void onDisasterClick(int position, DocumentSnapshot documentSnapshot) {
        Log.wtf("MyDisasters", "Recyclerview clicked position: "+position);
        String disaster = gson.toJson(documentSnapshot.toObject(Disaster.class));
        disasterDetails = DisasterDetails.newInstance(disaster);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, disasterDetails).addToBackStack(null).commit();
    }


    private void RecyclerViewSetup() {
        Query query = ((MainActivity) getActivity()).db.collection("users").document(((MainActivity) getActivity()).currentUser.getUid()).collection("disasters");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

               queryDocumentSnapshots.toObjects(Disaster.class);

            }
        });

        FirestoreRecyclerOptions<Disaster> options = new FirestoreRecyclerOptions.Builder<Disaster>().setQuery(query, Disaster.class).build();
        disasterAdapter = new DisasterAdapter(options, this);
        emblemAdapter = new EmblemAdapter(options, this);

    }

    @Override
    public void onEmblemClick(int position, DocumentSnapshot documentSnapshot) {
        //emblem on click
    }

    @Override
    public void onUserCallback(User user) {
        textView_totalPoints.setText(""+user.getTotalPoints());
        textView_rank.setText(""+user.getRank());
    }
}
