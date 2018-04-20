package com.hasanozanal.sunshine.Data;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ozanal on 17.04.2018.
 */

public class Api {
    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static Retrofit retrofit;
    public static final String API_KEY_URL = "5b13dba51fd0bc9b13f8abf71735df4d";

    public static Retrofit getApiClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        }
        return retrofit;
    }
}
