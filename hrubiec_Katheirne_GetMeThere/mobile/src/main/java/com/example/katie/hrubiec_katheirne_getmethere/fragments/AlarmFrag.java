package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsHelper;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsParser;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.PolylineParser;
import com.example.katie.hrubiec_katheirne_getmethere.objects.Alarm;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AlarmFrag extends DialogFragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String ARG_URL = "ARG_URL";
    private String averageTimeText = "";
    private FinsihAddListener mListener;

    private long durationInTraffic = 0;
    private long arrivalTimeChosen = 0;
    private long departureTime = 0;
    private String startingLoc;
    private String endingLoc;
    private boolean wakeUpCalled = false;
    private MapView mapView;
    private GoogleMap map;

    private String url = "";
    private Calendar calendar = Calendar.getInstance();
    private Boolean first = true;
    private long wakeUpTime = 0;

    private final ArrayList<Marker> markers = new ArrayList<>();

    //need map and to set polyline from link url var

    public static AlarmFrag newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        AlarmFrag fragment = new AlarmFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FinsihAddListener) {
            mListener = (FinsihAddListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind FinsihAddListener");
        }
    }

    public interface FinsihAddListener {
        void finishAdd(Alarm newAlarm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.alarm_add_frag, container, false);
        mapView = v.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().isZoomGesturesEnabled();
        map.getUiSettings().setAllGesturesEnabled(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            url = (String) getArguments().getSerializable(ARG_URL);
            AlarmFrag.TaskRequestDirections taskRequestDirections = new AlarmFrag.TaskRequestDirections();
            taskRequestDirections.execute(url);
        }
        EditText arrivalPicker = Objects.requireNonNull(getView()).findViewById(R.id.arrivalTime);
        EditText wakeUpPicker = getView().findViewById(R.id.wakeUp);
        Button setAlarmButton = getView().findViewById(R.id.addAlarm);
        arrivalPicker.setOnClickListener(this);
        wakeUpPicker.setOnClickListener(this);
        setAlarmButton.setOnClickListener(this);
        getMinDepart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrivalTime:
                //arrival method
                dateTimePicker();
                break;
            case R.id.wakeUp:
                //waky up dialog
                wakeUpPicker();
                break;
            case R.id.addAlarm:
                //needs check to see if any are null
                if (departureTime + durationInTraffic < System.currentTimeMillis() + durationInTraffic) {
                    showWarning();
                } else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    if(!wakeUpCalled){
                        mListener.finishAdd(new Alarm(departureTime, arrivalTimeChosen, durationInTraffic,startingLoc, endingLoc, departureTime, System.currentTimeMillis(), "","", userID));
                    }else{
                        mListener.finishAdd(new Alarm(departureTime, arrivalTimeChosen, durationInTraffic,startingLoc, endingLoc, wakeUpTime,System.currentTimeMillis(),"","",userID));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showWarning() {
        AlertDialog.Builder warning = new AlertDialog.Builder(getActivity());
        warning.setMessage("Minimum departure time cannot be in the past. Please readjust arrival time or wake up time.");
        warning.setPositiveButton("Okay", null);
        AlertDialog alert = warning.create();
        alert.show();
    }

    private void wakeUpPicker() {
        wakeUpCalled = true;
        final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inflater = this.getLayoutInflater();
        }
        View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.wake_up_picker, null);
        d.setTitle("Time to Get Ready");
        d.setMessage(R.string.pick_wakeup);
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText wakeUp = Objects.requireNonNull(getView()).findViewById(R.id.wakeUp);
                long minutes = numberPicker.getValue() * 60000;
                if (departureTime + durationInTraffic < System.currentTimeMillis() + durationInTraffic) {
                    showWarning();
                } else {
                    wakeUpTime = departureTime - minutes;
                    wakeUp.setText(numberPicker.getValue() + " minutes before departure");
                }
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }

    private void dateTimePicker() {
        Calendar c = Calendar.getInstance();
        int syear = c.get(Calendar.YEAR);
        int smonth = c.get(Calendar.MONTH);
        int sday = c.get(Calendar.DAY_OF_MONTH);

        final int shour = c.get(Calendar.HOUR);
        final int sminute = c.get(Calendar.MINUTE);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.HOUR, hourOfDay);
                        if (hourOfDay > 12) {
                            calendar.set(Calendar.AM_PM, Calendar.AM);
                        } else {
                            calendar.set(Calendar.AM_PM, Calendar.AM);
                        }
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.YEAR, year);

                        String key = "&key=AIzaSyACz6AjczEW5-jt3EW6lYsmjfO84U2W3jI";
                        String[] seperated = url.split("departure_time=");
                        url = seperated[0] + "departure_time=" + arrivalTimeChosen + key;
                        AlarmFrag.TaskRequestDirections taskRequestDirections = new AlarmFrag.TaskRequestDirections();
                        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                            showWarning();
                        }else{
                            taskRequestDirections.execute(url);
                        }
                    }
                }, shour, sminute, false);
                timePickerDialog.setTitle("Select time to Arrive by");
                timePickerDialog.show();
            }
        }, syear, smonth, sday);
        datePickerDialog.setTitle("Pick date");
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getMinDepart() {
        //add method to update ever minute clock tick
        TextView minDepart = Objects.requireNonNull(getView()).findViewById(R.id.depart);
        EditText arrivalTime = getView().findViewById(R.id.arrivalTime);

        departureTime = calendar.getTimeInMillis() + 120000;
        arrivalTimeChosen = departureTime + durationInTraffic;

        minDepart.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(departureTime));
        arrivalTime.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(arrivalTimeChosen));

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
            try {
                JSONObject jobj = new JSONObject(s);
                DirectionsParser dp = new DirectionsParser();
                JSONArray legs = dp.parseGoogleJson(jobj);

                for (int h = 0; h < legs.length(); h++) {
                    startingLoc = ((JSONObject) legs.get(h)).getString("start_address");
                    endingLoc = ((JSONObject) legs.get(h)).getString("end_address");
                    JSONObject durationInTraffic = ((JSONObject) legs.get(h)).getJSONObject("duration_in_traffic");
                    averageTimeText = durationInTraffic.getString("text");
                    AlarmFrag.this.durationInTraffic = Long.parseLong(durationInTraffic.getString("value")) * 1000;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView eta = Objects.requireNonNull(getView()).findViewById(R.id.travelTime);
            eta.setText(averageTimeText);

            if (first) {
                getMinDepart();
                first = false;
            } else {
                arrivalTimeChosen = calendar.getTimeInMillis();
                departureTime = arrivalTimeChosen - durationInTraffic;

                TextView depart = getView().findViewById(R.id.depart);
                depart.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(departureTime));

                EditText arrivalTime = getView().findViewById(R.id.arrivalTime);
                arrivalTime.setText(new SimpleDateFormat("MMM d, yyyy, h:mm a").format(arrivalTimeChosen));
            }


            AlarmFrag.TaskParser taskParser = new AlarmFrag.TaskParser();
            taskParser.execute(s);
        }
    }

    class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser dp = new DirectionsParser();
                JSONArray legs = dp.parseGoogleJson(jsonObject);
                for (int h = 0; h < legs.length(); h++) {
                    startingLoc = ((JSONObject) legs.get(h)).getString("start_address");
                    endingLoc = ((JSONObject) legs.get(h)).getString("end_address");
                }

                PolylineParser polylineParser = new PolylineParser();
                routes = polylineParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            PolylineParser polylineParser = new PolylineParser();
            PolylineOptions polylineOptions = polylineParser.getPolyline(lists);

            if (polylineOptions != null) {
                //add to map

                DirectionsHelper dh = new DirectionsHelper();

                LatLng start = dh.getLocationFromAddress(getActivity(), startingLoc);
                LatLng end = dh.getLocationFromAddress(getActivity(), endingLoc);

                Marker s = map.addMarker(new MarkerOptions().position(start).title("Start"));
                markers.add(s);
                Marker e = map.addMarker(new MarkerOptions().position(end).title("End"));
                markers.add(e);

                LatLngBounds.Builder b = new LatLngBounds.Builder();
                for (Marker m : markers) {
                    b.include(m.getPosition());
                }
                LatLngBounds bounds = b.build();
                //Change the padding as per needed
                //getting View size is too small after padding is applied. for 10 10 5
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 5);
                //need to fix zoom level
                map.animateCamera(cu);
                //create url to get request from 1st to second
                map.addPolyline(polylineOptions);
            }
        }
    }


}