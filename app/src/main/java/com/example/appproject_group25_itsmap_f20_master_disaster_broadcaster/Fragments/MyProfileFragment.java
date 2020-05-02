package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

public class MyProfileFragment extends Fragment {

    ImageView ProfilePicture;
    EditText NicknameField;
    EditText CountryField;
    TextView RankField;
    TextView PointsField;
    Button btn_back;
    Button btn_apply;

    private View rootView;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ProfilePicture = rootView.findViewById(R.id.editProfilePicture);
        NicknameField = rootView.findViewById(R.id.MyProfileNicknameField);
        CountryField = rootView.findViewById(R.id.MyProfileCountryField);
        RankField = rootView.findViewById(R.id.MyProfileRankField);
        PointsField = rootView.findViewById(R.id.MyProfilePointsField);



        btn_back = rootView.findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
        });

        btn_apply = rootView.findViewById(R.id.apply_btn);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        return rootView;
    }

    private void getUserDetails() {
        String Nickname;
        String Country;
        String Rank;
        String Points;




    }
}
