package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AlarmSettingsFrag;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.SignInFragment;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.objects.AlarmReceiver;

public class AlarmSettingsActivity extends AppCompatActivity implements AlarmSettingsFrag.AddDetailItemListener {

    String chosenRingtone;
    String chosenImage;
    private final int SELECT_PICTURE = 32;
    Alarm alarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        if(alarm != null){
            loadFrag();
        }
    }

    private void loadFrag(){
        getFragmentManager().beginTransaction().replace(R.id.frame, AlarmSettingsFrag.newInstance(alarm)).commitAllowingStateLoss();

    }

    @Override
    public void addSound() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 5);
    }

    @Override
    public void addImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void saveAlarm(Alarm newAlarm) {

        //save alarm to list, firebase database, set alarms

        Log.v("CLICK","asa - " + alarm.getImageuri());
        Log.v("CLICK","asa - " + alarm.getSounduri());
        //ListActivity.alarms.add(newAlarm);
        ListActivity.saveToFirebase(newAlarm);
        //ListActivity.writeObjectInCache(getApplicationContext(), ListActivity.READWRITEOBJ, ListActivity.alarms);
        Intent mainActIntent = new Intent(this, ListActivity.class);
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



        if(newAlarm.getWakeUpBefore()-(15*60000) > System.currentTimeMillis()){
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

            long beforeWakeUp15 = newAlarm.getWakeUpBefore()-(15*60000);

            AlarmManager.AlarmClockInfo acBefore15 = new AlarmManager.AlarmClockInfo(beforeWakeUp15, pi2TrafficCheck15);

            mgr1.setAlarmClock(acBefore15, piTrafficCheck15);

            Log.v("CLICK","alarm set for 15 before");
        }else{
            //return
            Log.v("CLICK","alarm not set for 15 before");
        }

        finish();
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone rt = RingtoneManager.getRingtone(this, uri);
            String title = rt.getTitle(this);

            if (uri != null) {
                this.chosenRingtone = uri.toString();
                Log.v("CLICK", "name for sound: " + title);
                Log.v("CLICK", "uri for sound: " + chosenRingtone);

                alarm.setSounduri(chosenRingtone);
                loadFrag();
            } else {
                this.chosenRingtone = null;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri imageUri = intent.getData();
            if (imageUri != null) {

                this.chosenImage = imageUri.toString();
                Log.v("CLICK", "uri for image: " + imageUri);
                alarm.setImageuri(chosenImage);
                Log.v("CLICK", "uri for image: " + alarm.getImageuri());

                loadFrag();
            } else {
                this.chosenImage = null;
            }
        }
    }


}
