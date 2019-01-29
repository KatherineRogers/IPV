package com.example.katie.hrubiec_katheirne_getmethere.helpers;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.katie.hrubiec_katheirne_getmethere.activities.DetailsActivity;
import com.example.katie.hrubiec_katheirne_getmethere.activities.MainActivity;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MessageService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

//If the message’s path equals "/my_path"...//

        if (messageEvent.getPath().equals("/my_path")) {

//...retrieve the message//
            ByteArrayInputStream in = new ByteArrayInputStream(messageEvent.getData());
            try {
                ObjectInputStream is = new ObjectInputStream(in);
                DetailsActivity.alarm = (Alarm) is.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", DetailsActivity.alarm);


//Broadcast the received Data Layer messages locally//

            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}
