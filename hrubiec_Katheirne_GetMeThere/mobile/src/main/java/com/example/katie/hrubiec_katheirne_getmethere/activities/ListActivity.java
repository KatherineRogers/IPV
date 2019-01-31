package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.ListFrag;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.SignInFragment;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListActivity extends AppCompatActivity implements ListFrag.AddPlaceListener {

    public static ArrayList<Alarm> alarms = new ArrayList<>();
    public static final int MAINREQUEST = 1;
    public static final String READWRITEOBJ = "READWRITEOBJ";
    Alarm alarm;
    protected Handler myHandler;
    private FirebaseAuth mAuth;
    static DatabaseReference databaseAlarms;
    static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                return true;
            }
        });

        databaseAlarms = FirebaseDatabase.getInstance().getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        loadFromFirebase();

        setList();
    }

    public void setList() {

        if (1==1/*readObjectFromCache(getApplicationContext(), READWRITEOBJ) != null*/) {
            //alarms = (ArrayList<Alarm>) readObjectFromCache(getApplicationContext(), READWRITEOBJ);
            new NewThread("/my_path", alarms).start();
        }
        new NewThread("/my_path", alarms).start();
        getFragmentManager().beginTransaction().replace(R.id.frame, ListFrag.newInstance(alarms)).commit();
        //getFragmentManager().beginTransaction().replace(R.id.frame, SignInFragment.newInstance()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public void addNew() {
        //add new locaiton and alarm
        Intent addIntent = new Intent(this, AddActivity.class);
        //Intent addIntent = new Intent(this, AlarmSettingsActivity.class);
        startActivityForResult(addIntent, AddActivity.ADDREQUEST);
    }

    @Override
    public void deleteAlarm(final int position) {
        alarms.remove(position);
        writeObjectInCache(ListActivity.this, READWRITEOBJ, alarms);
        new NewThread("/my_path", alarms).start();
        setList();
    }

    @Override
    public void viewAlarm(int position) {
        Intent addIntent = new Intent(this, DetailsActivity.class);
        addIntent.putExtra("alarm",alarms.get(position));
        startActivity(addIntent);    }

    @Override
    public void signOut() {
        mAuth.signOut();
        Log.v("CLICK","signed out");
        alarms.clear();
        finish();
    }

    public static void saveToFirebase(Alarm newAlarm){
        String id = user.getUid();
        DatabaseReference userRef = databaseAlarms.child(id);
        String alarmId = databaseAlarms.push().getKey();
        userRef.child("alarms").child(alarmId).setValue(newAlarm);
    }

    public void loadFromFirebase(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String userID = user.getUid();

        DatabaseReference testRef = rootRef.child("users").child(userID);
        DatabaseReference alarmsRef = testRef.child("alarms");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dh: dataSnapshot.getChildren()){
                    //Log.v("CLICK","" + dh.child("arrivalTime"));
                    if(alarms.isEmpty()) {
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
                        Log.v("CLICK", ""+ alarms.size());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        alarmsRef.addListenerForSingleValueEvent(eventListener);
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

    public static Object readObjectFromCache(Context context, String key) {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //writeObjectInCache(getApplicationContext(), READWRITEOBJ, alarms);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //writeObjectInCache(getApplicationContext(), READWRITEOBJ, alarms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new NewThread("/my_path", alarms).start();
        setList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //do something when add is finished or it is deleted
        new NewThread("/my_path", alarms).start();
        setList();
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //do something with the recieved infro from watch aka update ui
            new NewThread("/my_path", alarms).start();
            setList();
        }

    }

    public void sendmessage(ArrayList<Alarm> messageText) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("messageText", messageText);
        Message msg = myHandler.obtainMessage();
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }




    class NewThread extends Thread {
        String path;
        ArrayList<Alarm> message;

        NewThread(String p, ArrayList<Alarm> m) {
            path = p;
            message = m;
        }
        public void run() {
            Task<List<Node>> wearableList =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {

                List<com.google.android.gms.wearable.Node> nodes = Tasks.await(wearableList);
                for (com.google.android.gms.wearable.Node node : nodes) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(ListActivity.alarms);
                    byte[] bytes = bos.toByteArray();
                    Task<Integer> sendMessageTask = Wearable.getMessageClient(ListActivity.this).sendMessage(node.getId(), path, bytes);
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                        sendmessage(ListActivity.alarms);
                    } catch (ExecutionException exception) {
                    } catch (InterruptedException exception) {
                    }
                }
            } catch (ExecutionException exception) {
            } catch (InterruptedException exception) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
