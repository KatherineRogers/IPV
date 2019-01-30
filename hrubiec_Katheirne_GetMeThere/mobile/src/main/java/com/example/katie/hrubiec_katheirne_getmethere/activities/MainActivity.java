package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.katie.hrubiec_katheirne_getmethere.fragments.SignInFragment;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//mobile

public class MainActivity extends AppCompatActivity {

    private final String TAG = "FB_SIGNIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etPass;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.v("CLICK", "user is signed in " + user.getUid());
                    Intent startList = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(startList);
                } else {
                    Log.v("CLICK", "currently signed out");
                    getFragmentManager().beginTransaction().replace(R.id.frame, SignInFragment.newInstance()).commitAllowingStateLoss();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }




}


