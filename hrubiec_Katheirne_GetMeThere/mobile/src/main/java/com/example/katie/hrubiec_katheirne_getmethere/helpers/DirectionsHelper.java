package com.example.katie.hrubiec_katheirne_getmethere.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class DirectionsHelper {

    public String requestDirection(String reqUrl){
        String responseString = "";
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();
            inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Objects.requireNonNull(httpsURLConnection).disconnect();
        }
        return responseString;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    public String getRequestUrl(String start, String end) {
        String str_org = "origin=" + start;
        String str_dest = "destination=" + end;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String trafficModel = "traffic_model=pessimistic";
        String apiKey = "key=AIzaSyACz6AjczEW5-jt3EW6lYsmjfO84U2W3jI";
        String departureTime = "departure_time=now"; // currently my bday at 630 am - change to user input time(seconds of day and tiem) - duration i ntraffic valure(value is in seconds) on that day
        String param = str_org+"&"+str_dest+"&"+sensor+"&"+mode+"&"+trafficModel+"&"+departureTime+"&"+apiKey;
        String output = "json";
        Log.v("URL ","https://maps.googleapis.com/maps/api/directions/" + output+"?"+ param);
        return "https://maps.googleapis.com/maps/api/directions/" + output+"?"+ param;
    }


}
