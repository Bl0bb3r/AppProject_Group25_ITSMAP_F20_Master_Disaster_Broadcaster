package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.LoginActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Fragment {

    private static final String EMAIL_FIELD = "UserEmail";
    private static final String PWD_FIELD = "UserPassword";
    Button ExitBtn;
    Button SignInBtn;
    TextInputEditText EmailField;
    TextInputEditText PasswordField;
    TextView RegisterNewAccount;
    LoginActivity loginActivity;

    public Login() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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

        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        // Views
        EmailField = rootView.findViewById(R.id.EmailEditText);
        PasswordField = rootView.findViewById(R.id.PasswordEditText);
        RegisterNewAccount = rootView.findViewById(R.id.TV_register);

        // Buttons
        ExitBtn = rootView.findViewById(R.id.Btn_Exit);
        SignInBtn = rootView.findViewById(R.id.Btn_SignIn);



        // Textview listener - Opens CreateNewUserFragment
        RegisterNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.loginactivity_framelayout, new CreateNewUserFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Exit Button listener - Closes and exits application
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity.finish();
                System.exit(0);
            }
        });


        // Sign in Button listener
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmailField.getText() != null && PasswordField != null){
                    loginActivity.Authenticate(EmailField.getText().toString(), PasswordField.getText().toString(), rootView.getRootView());
                }

            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        loginActivity = (LoginActivity) context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            if (savedInstanceState.getString(EMAIL_FIELD) != null) {
                EmailField.setText(savedInstanceState.getString(EMAIL_FIELD));
            }
            if (savedInstanceState.getString(PWD_FIELD) != null) {
                PasswordField.setText(savedInstanceState.getString(PWD_FIELD));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (EmailField != null)
        {
            outState.putString(EMAIL_FIELD,EmailField.getText().toString());
        }
        if (PasswordField != null) {
            outState.putString(PWD_FIELD,PasswordField.getText().toString());
        }
        
     
    }
}

