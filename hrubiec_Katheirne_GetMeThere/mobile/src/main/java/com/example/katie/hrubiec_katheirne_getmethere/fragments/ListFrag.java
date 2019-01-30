package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.katie.hrubiec_katheirne_getmethere.activities.MainActivity;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.example.katie.hrubiec_katheirne_getmethere.R;

import java.util.ArrayList;

//mobile


public class ListFrag extends ListFragment {

    private AddPlaceListener mListener;
    private static final String ARG_ALARMS = "ARG_ALARMS";

    public static ListFrag newInstance(ArrayList<Alarm> alarms) {

        Bundle args = new Bundle();

        ListFrag fragment = new ListFrag();
        fragment.setArguments(args);
        args.putSerializable(ARG_ALARMS, alarms);
        return fragment;
    }

    public interface AddPlaceListener {
        void addNew();

        void deleteAlarm(int position);

        void viewAlarm(int position);
        void signOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddPlaceListener) {
            mListener = (AddPlaceListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind AddPlaceListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //maybe custom?
        if (getArguments() != null) {
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) getArguments().getSerializable(ARG_ALARMS);
            getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Delete Alarm").setMessage("Are you sure you want to delete this alarm?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete
                                    mListener.deleteAlarm(position);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                            .create()
                            .show();
                    return true;
                }
            });
            ArrayAdapter<Alarm> adapter = new ArrayAdapter<Alarm>(
                    getActivity(), android.R.layout.simple_list_item_1, alarms
            );
            setListAdapter(adapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_frag, container, false);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            mListener.addNew();
        }else if(item.getItemId() == R.id.signOut){
            mListener.signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.viewAlarm(position);
    }

}
