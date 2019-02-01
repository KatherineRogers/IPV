package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;

public class AfterAlarmFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_ALARM = "ARG_ALARM";
    Alarm alarmGoingOff;

    public static AfterAlarmFragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARM, alarm);
        AfterAlarmFragment fragment = new AfterAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_after_options, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmGoingOff = (Alarm) getArguments().getSerializable(ARG_ALARM);
        Button nav = getView().findViewById(R.id.startNav);
        nav.setOnClickListener(this);
        Button share = getView().findViewById(R.id.share);
        share.setOnClickListener(this);
        ImageView iv = getView().findViewById(R.id.image);
        Uri imageURI = Uri.parse(alarmGoingOff.getImageuri());
        iv.setImageURI(imageURI);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.startNav){
            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + alarmGoingOff.getStartingLoc() + "&daddr=" + alarmGoingOff.getEndingLoc();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
            //finish();
        }else if(v.getId() == R.id.share){
            //
            String shareBody = "Just woke up! Ready for my commute from " + alarmGoingOff.getStartingLoc() + " to " + alarmGoingOff.getEndingLoc();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }
}
