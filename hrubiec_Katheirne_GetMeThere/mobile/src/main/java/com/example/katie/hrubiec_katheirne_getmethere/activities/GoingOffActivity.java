package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AfterAlarmFragment;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.GoingOffFragment;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.ListFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class GoingOffActivity extends AppCompatActivity implements GoingOffFragment.SnoozeListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.frame, GoingOffFragment.newInstance()).commit();


    }//end on create

    @Override
    public void snooze(final Alarm snoozeAlarm) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                String packageName = getPackageName();
                String activityName = GoingOffActivity.class.getName();
                intent.setClassName(packageName, activityName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("alarm", snoozeAlarm);

                // Do something after 5m
                startActivity(intent);
            }
        }, 300000);
        finish();
    }
}
