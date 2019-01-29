package com.example.katie.hrubiec_katheirne_getmethere.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.katie.hrubiec_katheirne_getmethere.activities.GoingOffActivity;

public class AlarmReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //open frag
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            Alarm alarm = (Alarm) bundle.getSerializable("alarm");
            if (alarm != null) {
                Intent intent1 = new Intent();
                String packageName = context.getPackageName();
                String activityName = GoingOffActivity.class.getName();
                intent1.setClassName(packageName, activityName);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("alarm", alarm);


                if (bundle.getSerializable("time") != null) {
                    //check time in maps
                    Log.v("CHECKALARM", "alarm found for before time");
                    //run google API traffic check
                    //context.startActivity(intent1);
                } else {
                    Log.v("CHECKALARM", alarm.toString());
                    context.startActivity(intent1);
                }
            } else {
                Log.v("CHECKALARM", "alarm is null");
            }


        }
    }
}
