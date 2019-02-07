package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;

import java.text.SimpleDateFormat;

public class GoingOffActivity extends WearableActivity {

    private Alarm alarmGoingOFf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_goinng_off);

        Button dismiss = findViewById(R.id.dismiss);
        Button openMaps = findViewById(R.id.openMaps);

        if (getIntent() != null) {
            alarmGoingOFf = (Alarm) getIntent().getSerializableExtra("alarm");
            TextView tv = findViewById(R.id.nameofalarm);
            tv.setText(alarmGoingOFf.toString());
            tv = findViewById(R.id.timeofalarm);
            tv.setText(new SimpleDateFormat("h:mm a").format(alarmGoingOFf.getWakeUpBefore()));

            for(int i=0; i<MainActivity.alarms.size(); ++i){
                if(MainActivity.alarms.get(i).getIdentifier() == alarmGoingOFf.getIdentifier()){
                    MainActivity.alarms.remove(i);
                    MainActivity.writeObjectInCache(this, MainActivity.READWRITEOBJ, MainActivity.alarms);
                }
            }

            openMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + alarmGoingOFf.getStartingLoc() + "&daddr=" + alarmGoingOFf.getEndingLoc();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            Log.e("ERROR","alarm going off is null");
        }

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(GoingOffActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }//end on create
}
