package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;

import java.util.ArrayList;
import java.util.Objects;


///wear

public class ListFrag extends ListFragment {

    private static final String ARG_ALARMS = "ARG_ALARMS";
    private ViewAlarmListener mListener;

    public static ListFrag newInstance(ArrayList<Alarm> alarms) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARMS, alarms);
        ListFrag fragment = new ListFrag();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ViewAlarmListener{
        void viewAlarm(int position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //set custom list adapter

        ArrayList<Alarm> alarms = (ArrayList<Alarm>) getArguments().getSerializable(ARG_ALARMS);

        ArrayAdapter<Alarm> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, Objects.requireNonNull(alarms)
        );
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewAlarmListener) {
            mListener = (ViewAlarmListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind AddPlaceListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_frag_wear, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.viewAlarm(position);
    }
}
