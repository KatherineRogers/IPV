package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountFrag extends Fragment implements View.OnClickListener {

    private final String TAG = "FB_SIGNIN";

    // TODO: Add Auth members
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private EditText etPass;
    private EditText etEmail;
    private EditText etName;

    public static CreateAccountFrag newInstance() {

        Bundle args = new Bundle();

        CreateAccountFrag fragment = new CreateAccountFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_account_frag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etEmail = getView().findViewById(R.id.email);
        etPass = getView().findViewById(R.id.password);
        etName = getView().findViewById(R.id.name);
        Button create = getView().findViewById(R.id.createAccount);
        create.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.v(TAG, "user is signed in " + user.getUid());
                }else{
                    Log.v(TAG, "currently signed out");
                }
            }
        };
    }

    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        String name = etName.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.v("CLICK", "user was created");
                    mAuth.getCurrentUser().getUid();
                    

                }else{
                    Log.v("CLICK", "account creation failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    etEmail.setError("Email is already in use");
                }else if (e instanceof FirebaseAuthWeakPasswordException){
                    //updateStatus(e.getLocalizedMessage());
                    etPass.setError("Password needs to be at least 6 characters in length");
                }
            }
        });


    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        createUserAccount();
    }
}
