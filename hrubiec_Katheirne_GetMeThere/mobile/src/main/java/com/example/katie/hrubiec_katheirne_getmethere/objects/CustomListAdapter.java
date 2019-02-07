package com.example.katie.hrubiec_katheirne_getmethere.objects;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katie.hrubiec_katheirne_getmethere.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private static final long ID_CONSTANT = 0x010101010L;
    private final Context mContext;
    private final ArrayList<Alarm> mAlarms;

    public CustomListAdapter(Context _context, ArrayList<Alarm> _alarms) {
        mContext = _context;
        mAlarms = _alarms;
    }

    // Returning the number of objects in our collection.
    @Override
    public int getCount() {
        return mAlarms.size();
    }

    // Returning Book objects from our collection.
    @Override
    public Alarm getItem(int _position) {
        return mAlarms.get(_position);
    }

    // Adding our constant and position to create unique ID values.
    @Override
    public long getItemId(int _position) {
        return ID_CONSTANT + _position;
    }

    //inflate baseLayout xml
    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (_convertView == null) {
            _convertView = LayoutInflater.from(mContext).inflate(R.layout.base_adapter, _parent, false);
        }
        Alarm alarm = getItem(_position);
        TextView tv = _convertView.findViewById(R.id.description);
        ImageView iv = _convertView.findViewById(R.id.image);
        TextView departTV = _convertView.findViewById(R.id.depart);
        departTV.setText(alarm.getStringDepart());
        tv.setText(alarm.toString());
        Uri imageURI = Uri.parse(alarm.getImageuri());
        iv.setImageURI(imageURI);
        return _convertView;
    }

}
