package com.example.katie.hrubiec_katheirne_getmethere.activities;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.fragments.DetailsFrag;

public class DetailsActivity extends WearableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        getFragmentManager().beginTransaction().add(R.id.framewear, DetailsFrag.newInstance(alarm)).commit();

    }
}
