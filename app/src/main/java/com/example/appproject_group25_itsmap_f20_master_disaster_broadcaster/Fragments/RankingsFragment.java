package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.RankingsRecyclerAdapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

import java.util.List;


public class RankingsFragment extends Fragment {

    private List<User> mUserList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rankings, container, false);

        rankingsRecyclerView = rootView.findViewById(R.id.recyclerView_rankings);

        rankingsRecyclerAdapter = new RankingsRecyclerAdapter(getActivity(),mUserList);

        Users = rootView.findViewById(R.id.textview_totalUsers);
        Submits = rootView.findViewById(R.id.textview_totalSubmits);
        btn_ShowMe = rootView.findViewById(R.id.scopeMe_btn);
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

    @Override
    public void onStart() {
        super.onStart();
        getRankingsInfo();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getRankingsInfo() {
        Users.setText(((MainActivity)getActivity()).disasterService.GetAllUsers().size()-1);
        mUserList = ((MainActivity)getActivity()).disasterService.GetAllUsers();
        //Submits field will remain empty - it will call database too many times, -
        // and we're currently not tracking total submits, so would be rather large method.
        // (Would involve comparison between two lists currently (between total disasters available -
        // and already submited disasters.

    }

}
