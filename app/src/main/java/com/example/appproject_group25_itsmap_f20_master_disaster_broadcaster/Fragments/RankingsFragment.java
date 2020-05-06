package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.RankingsRecyclerAdapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RankingsFragment extends Fragment implements RankingsRecyclerAdapter.OnRankListener {
    private String TOTAL_USERS = "TotalUsers";
    private ArrayList<User>  mUserList = new ArrayList<>();



    TextView Users;
    TextView Submits;
    Button btn_ShowMe;
    Button btn_Top;
    Button btn_back;

    RecyclerView rankingsRecyclerView;
    RankingsRecyclerAdapter adapter;


    private View rootView;


    public RankingsFragment() {
        // Required empty public constructor
    }

    public static RankingsFragment newInstance() {
        RankingsFragment fragment = new RankingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        if (savedInstanceState == null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rankings, container, false);

        Users = rootView.findViewById(R.id.textview_totalUsers);
        Submits = rootView.findViewById(R.id.textview_totalSubmits);
        btn_ShowMe = rootView.findViewById(R.id.scopeMe_btn);

        rankingsRecyclerView = rootView.findViewById(R.id.recyclerView_rankings);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_space));
        rankingsRecyclerView.addItemDecoration(dividerItemDecoration);



        setupRecyclerView();

        rankingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankingsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Rank Placement
        //holder.textView_placement.setText(mUserList.get(position).getRank());
        Users.setText("0");
        Log.wtf("Rankings", "totalUsers TextView: "+ Users.getText());


        btn_ShowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_Top = rootView.findViewById(R.id.scopeTop_btn);
        btn_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_back = rootView.findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
        });

        return rootView;
    }

    private void setupRecyclerView() {
        Query query = ((MainActivity)getActivity()).db.collection("users").orderBy("rank");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }

                queryDocumentSnapshots.toObjects(User.class);

            }
        });

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapter = new RankingsRecyclerAdapter(options, this);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {

        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onRankClick(int position, DocumentSnapshot documentSnapshot) {
        //Rankings click in the list
    }
}
