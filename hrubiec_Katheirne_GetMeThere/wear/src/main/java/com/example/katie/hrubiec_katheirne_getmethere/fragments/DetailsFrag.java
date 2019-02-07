package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;

import java.util.Objects;

//wear

public class DetailsFrag extends Fragment implements View.OnClickListener{

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
        TextView tv = Objects.requireNonNull(getView()).findViewById(R.id.alarmName);
        tv.setText(Objects.requireNonNull(alarm).toString());
        Button btn = getView().findViewById(R.id.toMaps);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //maybe send data to phone and then to maps?

        if(v.getId() == R.id.toMaps){
            boolean isAppDisabled = isGoogleMapsInstalled();
            if(!isAppDisabled){
                AlertDialog.Builder warning = new AlertDialog.Builder(getActivity());
                warning.setMessage("You must download Google Maps to continue");
                warning.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToPlayStore();
                    }
                });
                AlertDialog alert = warning.create();
                alert.show();
            }else{
                Alarm alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + Objects.requireNonNull(alarm).getStartingLoc() + "&daddr=" + alarm.getEndingLoc();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        }
    }


    private void sendToPlayStore(){
        final String appPackageName = "com.google.android.apps.maps";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            Log.v("e","playstore is not installed on wear device");
        }
    }

    private boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }




}
