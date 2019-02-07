package com.example.katie.hrubiec_katheirne_getmethere.services;

import android.content.Intent;

import com.example.katie.hrubiec_katheirne_getmethere.activities.MainActivity;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.android.gms.wearable.MessageEvent;

import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {
            ByteArrayInputStream in = new ByteArrayInputStream(messageEvent.getData());
            try {
                ObjectInputStream is = new ObjectInputStream(in);
                MainActivity.alarms = (ArrayList<Alarm>) is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", MainActivity.alarms);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }




}
