package com.hasanozanal.sunshine.Model;


import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ozanal on 27/03/2018.
 */

public class Weather {

    @SerializedName("temp_max")
    private int max_tempTxt;
    @SerializedName("temp_min")
    private int min_tempTxt;
    @SerializedName("temp")
    private int current_temp;
    @SerializedName("name")
    private String city_nameTxt;
    @SerializedName("country")
    private String country_nameTxt;
    @SerializedName("main")
    private String descriptionTxt;
    @SerializedName("dt")
    private String date;
    @SerializedName("temp_max")
    private String forecastDate;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lon")
    private Double lon;
    @SerializedName("speed")
    private String windSpeedTxt;
    @SerializedName("deg")
    private String windDegTxt;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("id")
    private int ItemId;
    @SerializedName("icon")
    private String weatherIcon;

    public Weather() {
    }

    public Weather(int max_tempTxt, int min_tempTxt, String city_nameTxt, String country_nameTxt, String descriptionTxt, Double lat, Double lon, String windSpeedTxt, String windDegTxt, String humidity,String date,int current_temp,int itemId,String weatherIcon,String forecastDate) {
        this.max_tempTxt = max_tempTxt;
        this.min_tempTxt = min_tempTxt;
        this.city_nameTxt = city_nameTxt;
        this.country_nameTxt = country_nameTxt;
        this.descriptionTxt = descriptionTxt;
        this.lat = lat;
        this.lon = lon;
        this.windSpeedTxt = windSpeedTxt;
        this.windDegTxt = windDegTxt;
        this.humidity = humidity;
        this.date = date;
        this.current_temp = current_temp;
        this.ItemId = itemId;
        this.weatherIcon = weatherIcon;
        this.forecastDate = forecastDate;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getMax_tempTxt() {
        return max_tempTxt;
    }

    public void setMax_tempTxt(int max_tempTxt) {
        this.max_tempTxt = max_tempTxt;
    }

    public int getMin_tempTxt() {
        return min_tempTxt;
    }

    public void setMin_tempTxt(int min_tempTxt) {
        this.min_tempTxt = min_tempTxt;
    }

    public String getCity_nameTxt() {
        return city_nameTxt;
    }

    public void setCity_nameTxt(String city_nameTxt) {
        this.city_nameTxt = city_nameTxt;
    }

    public String getCountry_nameTxt() {
        return country_nameTxt;
    }

    public void setCountry_nameTxt(String country_nameTxt) {
        this.country_nameTxt = country_nameTxt;
    }

    public String getDescriptionTxt() {
        return descriptionTxt;
    }

    public void setDescriptionTxt(String descriptionTxt) {
        this.descriptionTxt = descriptionTxt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getWindSpeedTxt() {
        return windSpeedTxt;
    }

    public void setWindSpeedTxt(String windSpeedTxt) {
        this.windSpeedTxt = windSpeedTxt;
    }

    public String getWindDegTxt() {
        return windDegTxt;
    }

    public void setWindDegTxt(String windDegTxt) {
        this.windDegTxt = windDegTxt;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd MM");
        String currentDateandTime = sdf.format(new Date());
        return  currentDateandTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCurrent_temp() {
        return current_temp;
    }

    public void setCurrent_temp(int current_temp) {
        this.current_temp = current_temp;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getForecastDate() {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date dates = null;
        try {
            dates = curFormater.parse(this.date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);
            SimpleDateFormat curFormater2 = new SimpleDateFormat("EEEE");
            date = curFormater2.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }
}
