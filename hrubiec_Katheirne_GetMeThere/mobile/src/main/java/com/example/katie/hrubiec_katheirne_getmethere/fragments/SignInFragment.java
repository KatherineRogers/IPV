package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.activities.CreateAccountActivity;
import com.example.katie.hrubiec_katheirne_getmethere.activities.ListActivity;
import com.example.katie.hrubiec_katheirne_getmethere.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment implements OnClickListener {

    private final String TAG = "FB_SIGNIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etPass;
    private EditText etEmail;

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_frag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button signInB = getView().findViewById(R.id.signInButton);
        TextView newAccount = getView().findViewById(R.id.createAccount);
        signInB.setOnClickListener(this);
        newAccount.setOnClickListener(this);
        etEmail = getView().findViewById(R.id.email);
        etPass = getView().findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /*if (user != null) {
                    Log.v("CLICK", "user is signed in " + user.getUid());
                } else {
                    Log.v("CLICK", "currently signed out");
                }*/
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()) {
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton) {
            signUserIn();
        } else if (v.getId() == R.id.createAccount) {
            //Intent createAccount = new Intent(getActivity(),)
            //getFragmentManager().beginTransaction().add(R.id.frame, CreateAccountFrag.newInstance()).commit();
            Intent startList = new Intent(getActivity(), CreateAccountActivity.class);
            startActivity(startList);
        }
    }


    private void signUserIn() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // TODO: sign the user in with email and password credentials


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {
                    Log.e(TAG, "sign in failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    etPass.setError("Invalid password");
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    etEmail.setError("No account with this email");
                } else {
                    //updateStatus(e.getLocalizedMessage());
                }
            }
        });

    }

}
