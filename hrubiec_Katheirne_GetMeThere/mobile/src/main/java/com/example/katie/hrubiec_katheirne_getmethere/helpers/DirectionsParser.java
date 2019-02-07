package com.example.katie.hrubiec_katheirne_getmethere.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DirectionsParser {

    public JSONArray parseGoogleJson(JSONObject googleJSON){

        JSONObject jobj = new JSONObject();
        JSONArray legs = new JSONArray();
        try {
            JSONArray routes = googleJSON.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++){
                legs = ((JSONObject) routes.get(i)).getJSONArray("legs");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //make json to return
        return legs;
    }

}