package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

public class MyProfileFragment extends Fragment {

    private static final String NAME_FIELD ="UserName" ;
    private static final String COUNTRY_FIELD = "UserCountry";
    private static final String RANK_FIELD = "UserRank";
    private static final String POINT_FIELD ="UserPoints";

    ImageView ProfilePicture;
    EditText NicknameField;
    EditText CountryField;
    TextView RankField;
    TextView PointsField;
    Button btn_back;
    Button btn_apply;

    private View rootView;

    User user;
    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();

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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ProfilePicture = rootView.findViewById(R.id.editProfilePicture);
        NicknameField = rootView.findViewById(R.id.MyProfileNicknameField);
        CountryField = rootView.findViewById(R.id.MyProfileCountryField);
        RankField = rootView.findViewById(R.id.MyProfileRankField);
        PointsField = rootView.findViewById(R.id.MyProfilePointsField);

        if(savedInstanceState == null) {
            GetUser();
        }

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
                if (NicknameField.getText().toString().length() > 16) {
                    Toast.makeText(getActivity(), "" + R.string.nicknameTooLong, Toast.LENGTH_LONG).show();
                }
                else {
                    setProfileDetails();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //TODO Check på user details - lav if country/name osv er sat - return values og refresh layout variabler

    private void getProfileDetails(User user) {
        //https://stackoverflow.com/questions/4590957/how-to-set-text-in-an-edittext
        //if (profile picture) {

        //}
            if (user.getName() != null) {
                NicknameField.setText(user.getName(), TextView.BufferType.EDITABLE);
            }
            if (user.getCountry() != null) {
                CountryField.setText(user.getCountry(), TextView.BufferType.EDITABLE);
            }
            if (user.getRank() != 0) {
                RankField.setText(""+user.getRank());
            }
            else{
                RankField.setText("No rank");
            }
            PointsField.setText(""+user.getTotalPoints());
    }

    private void setProfileDetails() {

        //TODO: Make profile picture interactable
        User tempUser = new User();
        tempUser.setName(NicknameField.getText().toString());

        tempUser.setCountry(CountryField.getText().toString());
        if (RankField.getText() != "No rank") {
            tempUser.setRank(Integer.parseInt(RankField.getText().toString()));
        }
        else
        {
            tempUser.setRank(0);
        }
        tempUser.setTotalPoints(Integer.parseInt(PointsField.getText().toString()));

        UpdateUser(tempUser);
    }

    public void GetUser()
    {
        final User[] user = new User[1];
        ((MainActivity)getActivity()).db.collection("users").document(((MainActivity)getActivity()).currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.wtf("FIREBASE", document.getId() + " => " + document.getData());
                            user[0] = document.toObject(User.class);
                            Log.wtf("FIREBASE", "ID: "+document.getId()+" Name: "+user[0].getName());
                            getProfileDetails(user[0]);

                        } else {
                            Log.wtf("FIREBASE", "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    public void UpdateUser(User user)
    {
        DocumentReference userRef = ((MainActivity)getActivity()).db.collection("users").document(((MainActivity)getActivity()).currentUser.getUid());
        // set/merge the user object

            userRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                }
            });



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            NicknameField.setText(savedInstanceState.getString(NAME_FIELD));
            CountryField.setText(savedInstanceState.getString(COUNTRY_FIELD));
            RankField.setText(savedInstanceState.getString(RANK_FIELD));
            PointsField.setText(savedInstanceState.getString(POINT_FIELD));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME_FIELD, NicknameField.getText().toString());
        outState.putString(COUNTRY_FIELD, CountryField.getText().toString());
        outState.putString(RANK_FIELD, RankField.getText().toString());
        outState.putString(POINT_FIELD, PointsField.getText().toString());
    }
}
