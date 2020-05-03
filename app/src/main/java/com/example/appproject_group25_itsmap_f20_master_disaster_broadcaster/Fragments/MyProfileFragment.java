package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

public class MyProfileFragment extends Fragment {

    private MainActivity mainActivity;

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

    // Fragment Lifecycle stuff

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        mainActivity = (MainActivity) context;
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
                setProfileDetails();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getProfileDetails();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //TODO Check på user details - lav if country/name osv er sat - return values og refresh layout variabler

    private void getProfileDetails() {
        User tempUser = mainActivity.disasterService.GetUser();

        //https://stackoverflow.com/questions/4590957/how-to-set-text-in-an-edittext
        //if (profile picture) {

        //}
        if (tempUser.getName() != null) {
            NicknameField.setText(tempUser.getName(),TextView.BufferType.EDITABLE);
        }
        if (tempUser.getCountry() != null) {
            CountryField.setText(tempUser.getCountry(),TextView.BufferType.EDITABLE);
        }
        if (tempUser.getRank() != 0) {
            RankField.setText(tempUser.getRank());
        }
        if (tempUser.getTotalPoints() != 0) {
            PointsField.setText(tempUser.getTotalPoints());
        }

        return;
    }

    private void setProfileDetails() {
        User currUser = mainActivity.disasterService.GetUser();

        //TODO: Make profile picture interactable

        // if variable in edit text is different from value from getUser - change - and set in DB
        if (NicknameField.toString().length() > 16) {
            Toast.makeText(mainActivity, "" + R.string.nicknameTooLong, Toast.LENGTH_LONG).show();
        }
        else {
            if (NicknameField.toString() != currUser.getName()) {
                // set new value NicknameField.toString() in db
                currUser.setName(NicknameField.getText().toString());
            }
        }
        if (CountryField.toString() != currUser.getCountry()) {
            // set new value CountryField.toString() in db
            currUser.setCountry(CountryField.getText().toString());
        }

        mainActivity.disasterService.UpdateUser(currUser);
    }
}
