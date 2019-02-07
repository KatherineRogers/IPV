package com.example.katie.hrubiec_katheirne_getmethere.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.katie.hrubiec_katheirne_getmethere.activities.GoingOffActivity;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsHelper;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {

    private Alarm alarm;
    private Intent intent1;
    private Context cntxt;
    private Bundle bndl;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("CLICK", "recieved");
        //open frag
        bndl = intent.getBundleExtra("bundle");
/*        Bundle bundle1 = intent.getBundleExtra("bundle1");
        Bundle bundle2 = intent.getBundleExtra("bundle2");
        Bundle bundle3 = intent.getBundleExtra("bundle3");
        Bundle bundle4 = intent.getBundleExtra("bundle4");*/

        cntxt = context;

        if (bndl != null) {
            Log.v("CLICK", "bundle not null");
            alarm = (Alarm) bndl.getSerializable("alarm");
            Alarm alarm1 = (Alarm) bndl.getSerializable("time1");
            Alarm alarm2 = (Alarm) bndl.getSerializable("time2");
            Alarm alarm3 = (Alarm) bndl.getSerializable("time3");
            Alarm alarm4 = (Alarm) bndl.getSerializable("time4");

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseAuth.AuthStateListener authListener;

            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null && user.getUid().equals(alarm.userID)) {

                        if (alarm != null) {
                            //need to check if database  still contains the alarm

                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            final String userID = user.getUid();

                            DatabaseReference testRef = rootRef.child("users").child(userID);
                            final DatabaseReference alarmsRef = testRef.child("alarms");
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {

                                        Long ident = (Long) dss.child("identifier").getValue();

                                        if (alarm.getIdentifier() == ident) {
                                            intent1 = new Intent();

                                            String packageName = cntxt.getPackageName();
                                            String activityName = GoingOffActivity.class.getName();
                                            intent1.setClassName(packageName, activityName);
                                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent1.putExtra("alarm", alarm);
                                            if (bndl.getSerializable("time1") != null) {
                                                //check time in maps
                                                //run google API traffic check
                                                String start = alarm.getStartingLoc().replace(" ", "");
                                                String end = alarm.getEndingLoc().replace(" ", "");

                                                DirectionsHelper dh = new DirectionsHelper();
                                                String url = dh.getRequestUrl(start, end);
                                                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                                                taskRequestDirections.execute(url);

                                            } else {
                                                cntxt.startActivity(intent1);
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            };
                            alarmsRef.addListenerForSingleValueEvent(eventListener);

                        }/* else if(alarm1!= null) {
                Log.v("CLICK", "alarm1 is not null");
            }else if(alarm2!= null) {
                Log.v("CLICK", "alarm2 is not null");
            }else if(alarm3!= null) {
                Log.v("CLICK", "alarm3 is not null");
            } else if(alarm4!= null) {
                Log.v("CLICK", "alarm4 is not null");
            }*/
                    } else {
                    }
                }
            };
            firebaseAuth.addAuthStateListener(authListener);
        }

    }


    class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            DirectionsHelper dh = new DirectionsHelper();
            responseString = dh.requestDirection(strings[0]);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //parse json
            //set eta and map polyline
            JSONObject jobj = null;
            try {
                jobj = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DirectionsParser dp = new DirectionsParser();
            JSONArray legs = dp.parseGoogleJson(jobj);

            JSONObject durationInTraffic;
            long durationInTrafficLong = 0;

            for (int h = 0; h < legs.length(); h++) {
                try {
                    durationInTraffic = ((JSONObject) legs.get(h)).getJSONObject("duration_in_traffic");
                    durationInTrafficLong = Long.parseLong(durationInTraffic.getString("value")) * 1000;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (alarm.getDurationInTraffic() < durationInTrafficLong || alarm.getDurationInTraffic() == durationInTrafficLong) {
                //wake up now
                //start alarm now
                cntxt.startActivity(intent1);
            } else if (alarm.getDurationInTraffic() > durationInTrafficLong) {
                //check again in 15 more minutes - do nothing basically
            }
        }
    }

}
