package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;


public class RankingsFragment extends Fragment {

    RecyclerView Rankings;

    Button btn_back;

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
        return inflater.inflate(R.layout.fragment_rankings, container, false);
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
