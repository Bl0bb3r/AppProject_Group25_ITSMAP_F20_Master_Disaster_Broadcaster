package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    Button ExitBtn;
    Button SignInBtn;
    TextInputEditText EmailField;
    TextInputEditText PasswordField;

    //Firebase authentication variable
    public static FirebaseAuth mAuth;


    //TODO: Insert NavController

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_login);

        // Views
        EmailField = findViewById(R.id.EmailEditText);
        PasswordField = findViewById(R.id.PasswordEditText);

        // Buttons
        ExitBtn = findViewById(R.id.Btn_Exit);
        SignInBtn = findViewById(R.id.Btn_SignIn);

        mAuth = FirebaseAuth.getInstance();

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
                        getCurrentUser();
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

    public static FirebaseUser getCurrentUser() {
        FirebaseUser CurrentUser = mAuth.getCurrentUser();
        return CurrentUser;
    }


}