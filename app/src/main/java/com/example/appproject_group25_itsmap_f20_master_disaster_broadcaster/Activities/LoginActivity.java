package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.CreateNewUserFragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.Login;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.home_fragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {


    //Firebase authentication variable
    private FirebaseAuth mAuth;


    //TODO: Insert NavController

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //check Fragment
        if (findViewById(R.id.loginactivity_framelayout) != null)
        {
            if (savedInstance != null)
            {
                return;
            }

            if (currentUser == null) {
                Login loginFragment = new Login();
                getSupportFragmentManager().beginTransaction().add(R.id.loginactivity_framelayout, loginFragment).commit();
            }
            else
            {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"User: "+currentUser.getUid(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void Authenticate(String Email, String Password, final View view) {
        try {
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener((Activity) LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Authenticate
                        Toast.makeText(LoginActivity.this, "Authentication successfull.", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //TODO: Look at Nav Controller
                        //Init Navigation
                        //final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

                        //Hide Keyboard
                        InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        //Navigate
                        //navController.navigate(R.id.action_loginFragment_to_settingsOverviewFragment);

                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                    e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    public void RegisterUser(String newEmail, String newPassword)
    {
        mAuth.createUserWithEmailAndPassword(newEmail, newPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:success" + task.isSuccessful());
                Toast.makeText(LoginActivity.this,""+ R.string.registrationSuccess, Toast.LENGTH_SHORT).show();

                if (!task.isSuccessful()) {
                    // Sign up failed
                    Toast.makeText(LoginActivity.this,""+ R.string.registrationFailed, Toast.LENGTH_SHORT).show();
                    task.getException();

                }
                else {

                    FragmentManager fragmentmanager = getSupportFragmentManager();
                    fragmentmanager.popBackStack();
                }
            }
        });
    }



}