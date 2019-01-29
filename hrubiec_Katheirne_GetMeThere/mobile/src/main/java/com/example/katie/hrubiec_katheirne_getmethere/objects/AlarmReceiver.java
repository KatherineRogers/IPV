package com.example.katie.hrubiec_katheirne_getmethere.objects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.katie.hrubiec_katheirne_getmethere.activities.GoingOffActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //open frag
        Bundle bundle = intent.getBundleExtra("bundle");
/*        Bundle bundle1 = intent.getBundleExtra("bundle1");
        Bundle bundle2 = intent.getBundleExtra("bundle2");
        Bundle bundle3 = intent.getBundleExtra("bundle3");
        Bundle bundle4 = intent.getBundleExtra("bundle4");*/

        if (bundle != null) {
            Alarm alarm = (Alarm) bundle.getSerializable("alarm");
            Alarm alarm1 = (Alarm) bundle.getSerializable("time1");
            Alarm alarm2 = (Alarm) bundle.getSerializable("time2");
            Alarm alarm3 = (Alarm) bundle.getSerializable("time3");
            Alarm alarm4 = (Alarm) bundle.getSerializable("time4");

            if (alarm != null) {
                Intent intent1 = new Intent();
                String packageName = context.getPackageName();
                String activityName = GoingOffActivity.class.getName();
                intent1.setClassName(packageName, activityName);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("alarm", alarm);


                if (bundle.getSerializable("time1") != null) {
                    //check time in maps
                    Log.v("CHECKALARM", "alarm found for before time1");
                    //run google API traffic check

                }else {
                    Log.v("CHECKALARM", alarm.toString());
                    context.startActivity(intent1);
                }
            } else if(alarm1!= null) {
                Log.v("CHECKALARM", "alarm1 is not null");
            }else if(alarm2!= null) {
                Log.v("CHECKALARM", "alarm2 is not null");
            }else if(alarm3!= null) {
                Log.v("CHECKALARM", "alarm3 is not null");
            } else if(alarm4!= null) {
                Log.v("CHECKALARM", "alarm4 is not null");
            }


        }

    }

}
