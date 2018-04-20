package com.hasanozanal.sunshine.Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ozanal on 27/03/2018.
 */

public class DataProvider {

    private static final String BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    private static final String BASE_URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String ICON_URL = "http://openweathermap.org/img/w/";
    private static final String COORD_URL = "?lat=";
    private static final String API_KEY_URL = "&APPID=5b13dba51fd0bc9b13f8abf71735df4d";

    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        JSONObject jObj = jsonObject.getJSONObject(tagName);
        return jObj;
    }

    public static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    public static double getDouble(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    public static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }
    public static String createImageUrl(String code){
        String fullUrl = ICON_URL + code + ".png";
        return fullUrl;
    }

}

