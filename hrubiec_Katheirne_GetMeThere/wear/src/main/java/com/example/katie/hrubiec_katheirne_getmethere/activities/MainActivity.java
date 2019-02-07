package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;

import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.ListFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.AlarmReceiver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

///wear

public class MainActivity extends WearableActivity implements ListFrag.ViewAlarmListener{

    public static ArrayList<Alarm> alarms = new ArrayList<>();
    public static final String READWRITEOBJ = "READWRITEOBJ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//Register to receive local broadcasts, which we'll be creating in the next step//

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        setAmbientEnabled();
        setList();
    }

    private void setList() {
        if (readObjectFromCache(getApplicationContext(), READWRITEOBJ) != null) {
            alarms = (ArrayList<Alarm>) readObjectFromCache(getApplicationContext(), READWRITEOBJ);
            for (Alarm a: Objects.requireNonNull(alarms)){
                if(a.getWakeUpBefore() > System.currentTimeMillis()){
                    setAlarms(a);
                }
            }
        }
        getFragmentManager().beginTransaction().replace(R.id.framewear, ListFrag.newInstance(alarms)).commit();
    }


    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            writeObjectInCache(context,READWRITEOBJ, alarms);
            setList();
        }
    }

    private void setAlarms(Alarm newAlarm){
        AlarmManager mgr1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm1 = new Intent(this, AlarmReceiver.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("alarm", newAlarm);
        intentAlarm1.putExtra("bundle", bundle1);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intentAlarm1, 0);
        Intent intent2 = new Intent(AlarmClock.ACTION_SET_ALARM);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 1, intent2, 0);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(newAlarm.getWakeUpBefore(), pendingIntent2);
        Objects.requireNonNull(mgr1).setAlarmClock(alarmClockInfo, pendingIntent1);
    }

    public static void writeObjectInCache(Context context, String key, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Object readObjectFromCache(Context context, String key) {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void viewAlarm(int position) {
        Intent addIntent = new Intent(this, DetailsActivity.class);
        addIntent.putExtra("alarm",alarms.get(position));
        startActivity(addIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeObjectInCache(getApplicationContext(), READWRITEOBJ, alarms);
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeObjectInCache(getApplicationContext(), READWRITEOBJ, alarms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

}
