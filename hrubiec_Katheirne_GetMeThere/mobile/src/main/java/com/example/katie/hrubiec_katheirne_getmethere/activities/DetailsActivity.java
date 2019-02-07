package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.DetailsFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;

public class DetailsActivity extends AppCompatActivity{

    public static Alarm alarm;
    // --Commented out by Inspection (2/6/19, 11:13 PM):protected Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* new NewThread("/my_path", MainActivity.alarms).start();

        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                //messageText(stuff.getString("messageText"));
                return true;
            }
        });*/


        /*IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);


*/
        alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        getFragmentManager().beginTransaction().add(R.id.frame, DetailsFrag.newInstance(alarm)).commit();

    }




}


