package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AlarmSettingsFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.objects.AlarmReceiver;

import java.util.Objects;

public class AlarmSettingsActivity extends AppCompatActivity implements AlarmSettingsFrag.AddDetailItemListener {

    private final int SELECT_PICTURE = 32;
    private Alarm alarm;

    private void requestPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
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
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 5);
    }

    @Override
    public void addImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void saveAlarm(Alarm newAlarm) {

        //save alarm to list, firebase database, set alarms
        ListActivity.saveToFirebase(newAlarm);
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
        Objects.requireNonNull(mgr).setAlarmClock(alarmClockInfo, pendingIntent1);

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

            Objects.requireNonNull(mgr1).setAlarmClock(acBefore15, piTrafficCheck15);

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

            String chosenRingtone;
            if (uri != null) {
                chosenRingtone = uri.toString();
                alarm.setSounduri(chosenRingtone);
                loadFrag();
            } else {
                chosenRingtone = null;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri imageUri = intent.getData();
            String chosenImage;
            if (imageUri != null) {

                chosenImage = imageUri.toString();
                alarm.setImageuri(chosenImage);
                loadFrag();
            } else {
                chosenImage = null;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionsGranted = true;

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Permissions Required").setMessage(R.string.permission_denied_storage)
                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //send again
                                    requestPermission();
                                }
                            }).setCancelable(false)
                            .create()
                            .show();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.v("PERMISSIONS", "allowed" + permission);
                    } else {
                        //set to never ask again
                        Log.v("PERMISSIONS", "never ask again" + permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                System.exit(0);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
            }
        }
    }

}
