package com.example.katie.hrubiec_katheirne_getmethere.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsHelper;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.DirectionsParser;
import com.example.katie.hrubiec_katheirne_getmethere.helpers.PolylineParser;
import com.example.katie.hrubiec_katheirne_getmethere.R;
import com.example.katie.hrubiec_katheirne_getmethere.activities.AddActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFrag extends Fragment implements OnMapReadyCallback, View.OnClickListener{

    MapView mapView;
    public GoogleMap map;
    AddAlarmListener mListener;
    JSONObject jobj;
    ArrayList<Marker> markers = new ArrayList<>();
    String startingLoc;
    String endingLoc;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;

    public static AddFrag newInstance() {

        Bundle args = new Bundle();

        AddFrag fragment = new AddFrag();
        fragment.setArguments(args);
        return fragment;
    }

    public interface AddAlarmListener{
        void addAlarm(String url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddAlarmListener) {
            mListener = (AddAlarmListener) context;
        } else {
            throw new IllegalArgumentException("Context is not of kind AddPlaceListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_frag, container, false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btn = getView().findViewById(R.id.search);
        btn.setOnClickListener(this);

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("MAPZ","on map ready called");
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().isZoomGesturesEnabled();
        map.getUiSettings().setAllGesturesEnabled(true);
        requestPermission();
    }

    public void requestPermission(){
        locationManager  = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //map.setMyLocationEnabled(true);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //centreMapOnLocation(lastKnownLocation,"Your Location");
        } else if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }


    public void centreMapOnLocation(Location location, String title){

        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        map.clear();
        map.addMarker(new MarkerOptions().position(userLocation).title(title));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,12));

    }

    @Override
    public void onClick(View v) {
        EditText starting = getView().findViewById(R.id.startingAdd);
        EditText ending = getView().findViewById(R.id.endingAdd);
        if(starting.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), R.string.starting_empty, Toast.LENGTH_SHORT).show();
        }else{
            //go to next page
            if (ending.getText().toString().trim().equals("")){
                Toast.makeText(getActivity(), R.string.ending_empty, Toast.LENGTH_SHORT).show();
            }else{
                //add pins to map

                map.clear();
                DirectionsHelper dh = new DirectionsHelper();

                LatLng start = dh.getLocationFromAddress(getActivity(), starting.getText().toString().trim());
                String enterdStarting = starting.getText().toString().trim();
                String startRemoveSpaces = enterdStarting.replace( " ", "");
                String startFinal = startRemoveSpaces.replace(",","");


                LatLng end = dh.getLocationFromAddress(getActivity(),ending.getText().toString().trim());
                String enterdEnding = ending.getText().toString().trim();
                String endRemoveSpaces = enterdEnding.replace( " ", "");
                String endFinal = endRemoveSpaces.replace(",","");

                if (start != null && end != null) {

                    EditText startinget = getView().findViewById(R.id.startingAdd);
                    EditText endinget = getView().findViewById(R.id.endingAdd);
                    startinget.setFocusable(false);
                    endinget.setFocusable(false);

                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        imm = (InputMethodManager) getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                    }
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    requesturl(startFinal,endFinal);

                }else{
                    Toast.makeText(getActivity(), R.string.enter_valid, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EditText starting = getView().findViewById(R.id.startingAdd);
        EditText ending = getView().findViewById(R.id.endingAdd);

        if(item.getTitle().toString().equals("Edit")){
            starting.setFocusableInTouchMode(true);
            ending.setFocusableInTouchMode(true);
            AddActivity.menu.findItem(R.id.edit).setVisible(false);
            AddActivity.menu.findItem(R.id.next).setEnabled(false);
        }else if (item.getTitle().toString().equals("Next")){
            DirectionsHelper dh = new DirectionsHelper();

            String enterdStarting = starting.getText().toString().trim();
            String startRemoveSpaces = enterdStarting.replace( " ", "");
            String startFinal = startRemoveSpaces.replace(",","");

            String enterdEnding = ending.getText().toString().trim();
            String endRemoveSpaces = enterdEnding.replace( " ", "");
            String endFinal = endRemoveSpaces.replace(",","");
            String url = dh.getRequestUrl(startFinal, endFinal);

            mListener.addAlarm(url);
        }
        return super.onOptionsItemSelected(item);
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

    public void requesturl(String start, String end) {
        DirectionsHelper dh = new DirectionsHelper();
        String url = dh.getRequestUrl(start, end);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
    }

    public class TaskRequestDirections extends AsyncTask<String, Void,String> {

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
            AddFrag.TaskParser taskParser = new AddFrag.TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String,Void,List<List<HashMap<String, String>>>>{

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

            if(polylineOptions!=null){
                //add to map
                EditText starting = getView().findViewById(R.id.startingAdd);
                EditText ending = getView().findViewById(R.id.endingAdd);
                DirectionsHelper dh = new DirectionsHelper();
                LatLng start = dh.getLocationFromAddress(getActivity(), starting.getText().toString().trim());
                LatLng end = dh.getLocationFromAddress(getActivity(),ending.getText().toString().trim());

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

                starting.setFocusable(false);
                starting.setText(startingLoc);
                ending.setFocusable(false);
                ending.setText(endingLoc);

                AddActivity.menu.findItem(R.id.edit).setVisible(true);
                AddActivity.menu.findItem(R.id.next).setEnabled(true);

                Toast.makeText(getActivity(), R.string.clickNext, Toast.LENGTH_LONG).show();
            }
        }
    }
}
