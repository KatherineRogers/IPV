package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.katie.hrubiec_katheirne_getmethere.fragments.SignInFragment;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//mobile

public class MainActivity extends AppCompatActivity {

    // --Commented out by Inspection (2/6/19, 11:13 PM):private final String TAG = "FB_SIGNIN";
    // --Commented out by Inspection (2/6/19, 11:13 PM):private EditText etPass;
    // --Commented out by Inspection (2/6/19, 11:13 PM):private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent startList = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(startList);
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.frame, SignInFragment.newInstance()).commitAllowingStateLoss();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }
}


