package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.AddFrag;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;

public class AddActivity extends AppCompatActivity implements AddFrag.AddAlarmListener {

    public static final int ADDREQUEST = 0;
    public static Menu menu;
    // --Commented out by Inspection (2/6/19, 11:13 PM):public static Alarm newAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setList();
    }

    private void setList() {
        getFragmentManager().beginTransaction().add(R.id.frame, AddFrag.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AddActivity.menu = menu;
        getMenuInflater().inflate(R.menu.addresses_menu, menu);
        AddActivity.menu.findItem(R.id.next).setEnabled(false);
        AddActivity.menu.findItem(R.id.edit).setVisible(false);
        return true;
    }

    private void startAlarmFrag(String url) {
        Intent addIntent = new Intent(this, AlarmActivity.class);
        //need to change to startforresult
        addIntent.putExtra("url", url);
        startActivityForResult(addIntent, AlarmActivity.ADDALARMREQUEST);
    }


    @Override
    public void addAlarm(String url) {
        startAlarmFrag(url);
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
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    alertDialogBuilder.setTitle("Permissions Required").setMessage(R.string.permission_denied_loc)
                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //send again
                                    setList();
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
