package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments.CreateNewUserFragment;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button ExitBtn;
    Button SignInBtn;
    TextInputEditText EmailField;
    TextInputEditText PasswordField;
    TextView RegisterNewAccount;

    //Firebase authentication variable
    private FirebaseAuth mAuth;


    //TODO: Insert NavController

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_login);

        // Views
        EmailField = findViewById(R.id.EmailEditText);
        PasswordField = findViewById(R.id.PasswordEditText);
        RegisterNewAccount = findViewById(R.id.TV_register);

        // Buttons
        ExitBtn = findViewById(R.id.Btn_Exit);
        SignInBtn = findViewById(R.id.Btn_SignIn);

        mAuth = FirebaseAuth.getInstance();


        // Textview listener - Opens CreateNewUserFragment
        RegisterNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewUserFragment createNewUserFragment = new CreateNewUserFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.loginactivity_framelayout,createNewUserFragment).commit();
            }
        });

        // Exit Button listener - Closes and exits application
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });


        // Sign in Button listener
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmailField.getText() != null && PasswordField != null){
                    Authenticate(EmailField.getText().toString(), PasswordField.getText().toString(), findViewById(android.R.id.content).getRootView());
                }

            }
        });


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


}