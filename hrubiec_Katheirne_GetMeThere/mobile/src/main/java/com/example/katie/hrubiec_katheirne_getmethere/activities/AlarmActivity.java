package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AddFrag;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AlarmFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.objects.AlarmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity implements AlarmFrag.FinsihAddListener {

    public static final int ADDALARMREQUEST = 1;
    String url;
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = getIntent().getStringExtra("url");
        setList();
    }

    private void setList() {
        getFragmentManager().beginTransaction().add(R.id.frame, AlarmFrag.newInstance(url)).addToBackStack(null).commit();
    }

    @Override
    public void finishAdd(Alarm newAlarm) {

        Intent startNext = new Intent(this, AlarmSettingsActivity.class);
        startNext.putExtra("alarm", newAlarm);
        startActivityForResult(startNext, 88);



        /*ListActivity.alarms.add(newAlarm);
        ListActivity.writeObjectInCache(getApplicationContext(), ListActivity.READWRITEOBJ, ListActivity.alarms);
        Intent mainActIntent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, mainActIntent);

        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm1 = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", newAlarm);
        intentAlarm1.putExtra("bundle", bundle);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intentAlarm1, 0);
        Intent intent2 = new Intent(AlarmClock.ACTION_SET_ALARM);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 1, intent2, 0);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(newAlarm.getWakeUpBefore(), pendingIntent2);
        mgr.setAlarmClock(alarmClockInfo, pendingIntent1);

        //setTrafficChecks(newAlarm);



        if(newAlarm.getWakeUpBefore()-(30*60000) > System.currentTimeMillis()){
            //checks if 15 before is less than now
            //set intent to fire -
            AlarmManager mgr1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            //Bundle bundle1 = new Bundle();
            bundle.putSerializable("time1", newAlarm);

            Intent trafficCheck15 = new Intent(this, AlarmReceiver.class);
            trafficCheck15.putExtra("bundle", bundle);

            PendingIntent piTrafficCheck15 = PendingIntent.getBroadcast(this,15,trafficCheck15,0);

            Intent trafficCheck152 = new Intent(AlarmClock.ACTION_SET_ALARM);

            PendingIntent pi2TrafficCheck15 = PendingIntent.getActivity(this,0,trafficCheck152, 0);

            long beforeWakeUp15 = newAlarm.getWakeUpBefore()-(30*60000);

            AlarmManager.AlarmClockInfo acBefore15 = new AlarmManager.AlarmClockInfo(beforeWakeUp15, pi2TrafficCheck15);

            mgr1.setAlarmClock(acBefore15, piTrafficCheck15);

            Log.v("CHECKALARM","alarm set for 30 before");
        }else{
            //return
            Log.v("CHECKALARM","alarm not set for 15 before");
        }
        finish();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //do something when add is finished or it is deleted
        if(resultCode != RESULT_CANCELED){
            Intent intent = new Intent(this, ListActivity.class);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AddActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
