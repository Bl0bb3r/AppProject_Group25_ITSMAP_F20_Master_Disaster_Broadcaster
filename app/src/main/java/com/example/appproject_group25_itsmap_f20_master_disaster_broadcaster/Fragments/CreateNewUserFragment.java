package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.LoginActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class CreateNewUserFragment extends Fragment {

    private LoginActivity loginActivity;

    Button BackBtn;
    Button SignUpBtn;
    TextInputEditText Email;
    TextInputEditText Password;

    private FirebaseAuth mAuth;

    //TODO: Firebase database med users?
    //FirebaseDatabase FirebaseDB;

    private View rootView;


    public CreateNewUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        loginActivity = (LoginActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_new_user, container, false);
        return rootView;
    }

    // Firebase setup assistant
    @Override
    public void onStart() {
        super.onStart();

    }

    private void registerNewUser(String newEmail, String newPassword) {
        // reads email and password fields filled by new user.
        // trims for space after entered email - happens if autofilled sometimes
        newEmail = Email.getText().toString().trim();
        newPassword = Password.getText().toString().trim();
        
        if(TextUtils.isEmpty(newEmail)) {
            Toast.makeText(loginActivity, ""+ R.string.emailEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(loginActivity, ""+ R.string.passEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.length() < 8) {
            Toast.makeText(loginActivity, ""+ R.string.passShort, Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(newEmail, newPassword).addOnCompleteListener((Activity)getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:success" + task.isSuccessful());
                        Toast.makeText(getContext(),""+ R.string.registrationSuccess, Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            // Sign up failed
                            Toast.makeText(getContext(),""+ R.string.registrationFailed, Toast.LENGTH_SHORT).show();
                            task.getException();

                        }
                        else {
                            // TODO send til MainActivity her - hør Mike om intent eller hvordan vi passer
                        }
                    }
                });


    }

}


