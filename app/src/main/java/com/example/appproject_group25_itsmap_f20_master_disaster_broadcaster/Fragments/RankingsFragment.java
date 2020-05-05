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


public class RankingsFragment extends Fragment {
    private String TOTAL_USERS = "TotalUsers";
    private ArrayList<User>  mUserList = new ArrayList<>();

    FirestoreRecyclerAdapter adapter;

    TextView Users;
    TextView Submits;
    Button btn_ShowMe;
    Button btn_Top;
    Button btn_back;

    RecyclerView rankingsRecyclerView;
    RecyclerView.Adapter rankingsRecyclerAdapter;


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

                mUserList = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);

            }
        });

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapter = new FirestoreRecyclerAdapter<User, UserHolder>(options) {

            @Override
            public void onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...

            }
            @NonNull
            @Override
            public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
                View view = mInflater.inflate(R.layout.rankings_customlayout, parent,false);
                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                //Rank Placement
                //holder.textView_placement.setText(mUserList.get(position).getRank());
                Users.setText(""+(mUserList.size()));
                Log.wtf("Rankings", "totalUsers TextView: "+ Users.getText());

                Log.wtf("RankingsFragment","Current model -- name: "+model.getName()+" rank: "+model.getRank()+" totalPoints: "+model.getTotalPoints());

                //Rank placement - ordinal end letters
                // https://www.java67.com/2018/05/java-string-chartat-example-how-to-get-first-last-character.html

                if(model != null) {
                    int Sample = model.getRank();
                    String rank = String.valueOf(Sample);
                    char lastChar = rank.charAt(rank.length() - 1);

                    if (lastChar == 1) {
                        holder.textView_placementEnd.setText(R.string.first);
                    } else if (lastChar == 2) {
                        holder.textView_placementEnd.setText(R.string.second);
                    } else if (lastChar == 3) {
                        holder.textView_placementEnd.setText(R.string.third);
                    } else {
                        holder.textView_placementEnd.setText(R.string.fourthAndRest);
                    }

                    holder.textView_placement.setText(""+model.getRank());
                    //Nickname
                    if (model.getName() != null) {
                        holder.textView_Nickname.setText(model.getName());
                    }

                    //Points
                    holder.textView_totalPoints.setText("" + model.getTotalPoints());
                }
            }

        };
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

    private void getRankingsInfo() {

        //((MainActivity) getActivity()).repository.GetAllUsers(this);
            //Submits field will remain empty - it will call database too many times, -
            // and we're currently not tracking total submits, so would be rather large method.
            // (Would involve comparison between two lists currently (between total disasters available -

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

    private class UserHolder extends RecyclerView.ViewHolder {
        TextView textView_placement;
        TextView textView_placementEnd;
        TextView textView_Nickname;
        TextView textView_totalPoints;


        public UserHolder(View itemView)
        {
            super(itemView);
            textView_placement = (TextView) itemView.findViewById(R.id.textView_rankings_position_rank);
            textView_placementEnd = (TextView) itemView.findViewById(R.id.textView_rankings_position_end);
            textView_Nickname = (TextView) itemView.findViewById(R.id.textView_rankings_name);
            textView_totalPoints = (TextView) itemView.findViewById(R.id.textView_rankings_points);


        }

    }


}
