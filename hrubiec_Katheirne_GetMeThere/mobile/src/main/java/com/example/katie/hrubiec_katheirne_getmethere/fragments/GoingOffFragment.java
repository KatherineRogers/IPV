package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.activities.ListActivity;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class GoingOffFragment extends Fragment {

    Alarm alarmGoingOFf;
    Ringtone ringtone;
    MediaPlayer mediaPlayer;
    SnoozeListener mListener;

    public static GoingOffFragment newInstance() {

        Bundle args = new Bundle();

        GoingOffFragment fragment = new GoingOffFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_goinng_off, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SnoozeListener) {
            mListener = (SnoozeListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind FinsihAddListener");
        }
    }

    public interface SnoozeListener {
        void snooze(Alarm snoozeAlarm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button dismiss = getView().findViewById(R.id.dismiss);
        Button snooze = getView().findViewById(R.id.snooze);

        if (getActivity().getIntent() != null) {
            alarmGoingOFf = (Alarm) getActivity().getIntent().getSerializableExtra("alarm");
            ImageView iv = getView().findViewById(R.id.image);
            Uri imageURI = Uri.parse(alarmGoingOFf.getImageuri());
            iv.setImageURI(imageURI);
            TextView tv = getView().findViewById(R.id.nameofalarm);
            tv.setText(alarmGoingOFf.toString());
            tv = getView().findViewById(R.id.timeofalarm);
            tv.setText(new SimpleDateFormat("h:mm a").format(alarmGoingOFf.getWakeUpBefore()));

            Uri alarmUriSound = Uri.parse(alarmGoingOFf.getSounduri());
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getActivity(), alarmUriSound);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            snooze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    //snooze alarm
                    mListener.snooze(alarmGoingOFf);
                }
            });
        } else {
            Log.e("ERROR", "alarm going off is null");
        }

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userID = user.getUid();

                DatabaseReference testRef = rootRef.child("users").child(userID);
                DatabaseReference alarmsRef = testRef.child("alarms");
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String alarmName = dataSnapshot.getKey();

                        Log.v("CLICK", alarmName);

                        for(DataSnapshot dh: dataSnapshot.getChildren()){

                            Long ident = (Long) dh.child("identifier").getValue();

                            if(ident == alarmGoingOFf.getIdentifier()){
                                mediaPlayer.stop();
                                dh.getRef().removeValue();
                                ListActivity.alarms.clear();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                alarmsRef.addListenerForSingleValueEvent(eventListener);
                //open frag
                getFragmentManager().beginTransaction().replace(R.id.frame, AfterAlarmFragment.newInstance(alarmGoingOFf)).commit();

                //finish();
                //Intent intent = new Intent(GoingOffActivity.this, ListActivity.class);
                //startActivity(intent);
            }
        });
    }
}
