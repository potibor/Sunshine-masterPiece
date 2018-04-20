package com.hasanozanal.sunshine.Data;

import com.hasanozanal.sunshine.Model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ozanal on 17.04.2018.
 */

public interface ApiInterface {


    @GET("data/2.5/weather")
    Call<String> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String key, @Query("units") String units);

    @GET("data/2.5/forecast")
    Call<String> getForecast(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String key, @Query("units") String units);

    @GET("img/w/")
    Call<String> getImageCode(@Query("") int id);
}
