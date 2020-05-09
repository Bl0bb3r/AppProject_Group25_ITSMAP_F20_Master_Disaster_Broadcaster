package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.LoginActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.material.textfield.TextInputEditText;

public class CreateNewUserFragment extends Fragment {

    private static final String EMAIL_FIELD ="Email";
    private static final String PW_FIELD = "Password";
    private static final String PW_VERIFY_FIELD = "VerifyPW";
    Button btn_back;
    Button btn_signup;
    TextInputEditText editText_email;
    TextInputEditText editText_password1;
    TextInputEditText editText_password2;

    private View rootView;


    public CreateNewUserFragment() {
        // Required empty public constructor
    }

    public static CreateNewUserFragment newInstance() {
        CreateNewUserFragment fragment = new CreateNewUserFragment();

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
        rootView = inflater.inflate(R.layout.fragment_create_new_user, container, false);

        editText_email = rootView.findViewById(R.id.F_CNU_EmailEditText);
        editText_password1 = rootView.findViewById(R.id.F_CNU_PasswordEditText);
        editText_password2 = rootView.findViewById(R.id.F_CNU_verifyPasswordEditText);


        btn_back = rootView.findViewById(R.id.Btn_Cancel);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();

            }
        });

        btn_signup = rootView.findViewById(R.id.Btn_SignUp);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                registerNewUser(editText_email.getText().toString(), editText_password1.getText().toString());

            }
        });
        return rootView;
    }

    // Firebase setup assistant
    @Override
    public void onStart() {
        super.onStart();

    }

    private void registerNewUser(String email, String password) {
        // reads email and password fields filled by new user.
        // trims for space after entered email - happens if autofilled sometimes
        String _email = email.trim();
        String _password = password.trim();
        
        if(TextUtils.isEmpty(_email)) {
            Toast.makeText(getActivity(), ""+ R.string.emailEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(_password)) {
            Toast.makeText(getActivity(), ""+ R.string.passEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (_password.length() < 8) {
            Toast.makeText(getActivity(), ""+ R.string.passShort, Toast.LENGTH_SHORT).show();
        }

        //here
        ((LoginActivity)getActivity()).RegisterUser(_email, _password);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            editText_email.setText(savedInstanceState.getString(EMAIL_FIELD));
            editText_password1.setText(savedInstanceState.getString(PW_FIELD));
            editText_password2.setText(savedInstanceState.getString(PW_VERIFY_FIELD));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EMAIL_FIELD, editText_email.getText().toString());
        outState.putString(PW_FIELD, editText_password1.getText().toString());
        outState.putString(PW_VERIFY_FIELD, editText_password2.getText().toString());
    }
}


