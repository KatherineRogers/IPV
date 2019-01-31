package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class GoingOffActivity extends AppCompatActivity {

    Alarm alarmGoingOFf;


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




            /*for(int i=0; i<ListActivity.alarms.size(); ++i){

                //delet from database instead


                if(ListActivity.alarms.get(i).getIdentifier() == alarmGoingOFf.getIdentifier()){
                    ListActivity.alarms.remove(i);
                    Log.v("REMOVE","removed alarm");
                    ListActivity.writeObjectInCache(this, ListActivity.READWRITEOBJ, ListActivity.alarms);
                }
            }*/

            openMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + alarmGoingOFf.getStartingLoc() + "&daddr=" + alarmGoingOFf.getEndingLoc();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Log.e("ERROR", "alarm going off is null");
        }

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userID = user.getUid();

                DatabaseReference testRef = rootRef.child("users").child(userID);
                DatabaseReference alarmsRef = testRef.child("alarms");
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {







                        String alarmName = dataSnapshot.getKey();

                        Log.v("CLICK", alarmName);

                        for(DataSnapshot dh: dataSnapshot.getChildren()){

                            Long ident = (Long) dh.child("identifier").getValue();

                            if(ident == alarmGoingOFf.getIdentifier()){
                                dh.getRef().removeValue();
                                break;
                            }
                        }


                    /*for (DataSnapshot dh : dataSnapshot.getChildren()) {
                        //Log.v("CLICK","" + dh.child("arrivalTime"));
                        if (alarms.isEmpty()) {
                            Long arrivalTime = (Long) dh.child("arrivalTime").getValue();
                            Long departTime = (Long) dh.child("departureTime").getValue();
                            Long dit = (Long) dh.child("durationInTraffic").getValue();
                            String startLoc = (String) dh.child("startingLoc").getValue();
                            String endLoc = (String) dh.child("endingLoc").getValue();
                            Long ident = (Long) dh.child("identifier").getValue();
                            String img = (String) dh.child("imageurl").getValue();
                            Long wub = (Long) dh.child("wakeUpBefore").getValue();
                            String sound = (String) dh.child("soundurl").getValue();

                            alarms.add(new Alarm(departTime, arrivalTime, dit, startLoc, endLoc, wub, ident, img, sound, userID));
                            setList();
                            Log.v("CLICK", "" + alarms.size());
                        }
                    }*/




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                alarmsRef.addListenerForSingleValueEvent(eventListener);





                finish();
                Intent intent = new Intent(GoingOffActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }//end on create

}
