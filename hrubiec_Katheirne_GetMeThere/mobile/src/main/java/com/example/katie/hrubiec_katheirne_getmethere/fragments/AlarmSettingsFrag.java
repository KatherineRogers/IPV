package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;

public class AlarmSettingsFrag extends Fragment implements View.OnClickListener{

    AddDetailItemListener mListener;
    private static final String ARG_ALARM = "ARG_ALARM";
    Alarm alarm;

    public static AlarmSettingsFrag newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARM, alarm);
        AlarmSettingsFrag fragment = new AlarmSettingsFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_settings_frag, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddDetailItemListener) {
            mListener = (AddDetailItemListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind FinsihAddListener");
        }
    }

    public interface AddDetailItemListener {
        void addSound();
        void addImage();
        void saveAlarm(Alarm newAlarm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);
        }

        TextView setFor = getView().findViewById(R.id.setForText);
        setFor.setText(alarm.toString());

        EditText sound = getView().findViewById(R.id.sound);
        sound.setOnClickListener(this);

        ImageView image = getView().findViewById(R.id.image);
        image.setOnClickListener(this);

        Button saveButton = getView().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        if(!alarm.getImageuri().isEmpty()){
            Uri imageuri = Uri.parse(alarm.getImageuri());
            image.setImageURI(imageuri);
        }


        if(!alarm.getSounduri().isEmpty()){
            Uri uri = Uri.parse(alarm.getSounduri());
            Ringtone rt = RingtoneManager.getRingtone(getActivity(), uri);
            String title = rt.getTitle(getActivity());
            sound.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.image){
            Log.v("CLICK", "image clicked");
            mListener.addImage();
        }else if(v.getId() == R.id.sound){
            Log.v("CLICK", "sound clicked");
            mListener.addSound();
        }else if(v.getId() == R.id.saveButton){

            mListener.saveAlarm(alarm);
        }
    }






}
