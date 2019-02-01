package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class DetailsFrag extends Fragment implements View.OnClickListener {

    private static final String ARG_ALARM = "ARG_ALARM";

    public static DetailsFrag newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARM, alarm);
        DetailsFrag fragment = new DetailsFrag();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_frag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Alarm alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);

        TextView tv = getView().findViewById(R.id.startingDet);
        tv.setText(alarm.getStartingLoc());
        tv = getView().findViewById(R.id.endingDet);
        tv.setText(alarm.getEndingLoc());
        tv = getView().findViewById(R.id.arrivalTImeDet);
        tv.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(alarm.getArrivalTime()));
        tv = getView().findViewById(R.id.departureTimeDet);
        tv.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(alarm.getDepartureTime()));
        tv = getView().findViewById(R.id.durationInTrafficDet);
        String hms = String.format("%02d hours %02d minutes", TimeUnit.MILLISECONDS.toHours(+alarm.getDurationInTraffic()),
                TimeUnit.MILLISECONDS.toMinutes(+alarm.getDurationInTraffic()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(+alarm.getDurationInTraffic())));
        tv.setText(hms);
        tv = getView().findViewById(R.id.wakeUpTimeDet);
        tv.setText(new SimpleDateFormat("h:mm a").format(alarm.getWakeUpBefore()));
        Button btn = getView().findViewById(R.id.gmapsButton);
        btn.setOnClickListener(this);
        ImageView iv = getView().findViewById(R.id.image);

        Uri imageURI = Uri.parse(alarm.getImageuri());
        iv.setImageURI(imageURI);
    }


    @Override
    public void onClick(View v) {
        Alarm alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + alarm.getStartingLoc() + "&daddr=" + alarm.getEndingLoc();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

}
