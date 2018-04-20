package com.hasanozanal.sunshine.Adapters;


import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasanozanal.sunshine.Model.Weather;
import com.hasanozanal.sunshine.R;

import java.util.ArrayList;
import java.util.Locale;

import com.hasanozanal.sunshine.Data.DataService;

/**
 * Created by ozanal on 29/03/2018.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> implements Filterable {
    private Activity activity;
    private int layoutResource;
    private ArrayList<Weather> wData = new ArrayList<>();
    private ArrayList<Weather> weatherList = new ArrayList<>() ;
    private static String DEGREE_ICON = "\u00b0";
    private DataService dataService;

    public WeatherAdapter(@NonNull Activity act, int resource, ArrayList<Weather> weatherData) {
        super(act, resource, weatherData);
        activity = act;
        layoutResource = resource;
        wData = weatherData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return wData.size();
    }

    @Nullable
    @Override
    public Weather getItem(int position) {
        return wData.get(position);
    }

    @Override
    public int getPosition(@Nullable Weather item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null || (row.getTag()) == null) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);
            holder = new ViewHolder();

            holder.city_nameHolder = (TextView) row.findViewById(R.id.list_row_cityNameId);
            holder.max_tempHolder = (TextView) row.findViewById(R.id.list_row_tempId);
            holder.min_tempHolder = (TextView) row.findViewById(R.id.list_row_minTempId);
            holder.imageView = (ImageView) row.findViewById(R.id.list_row_weatherImgId);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.weather = getItem(position);
        holder.city_nameHolder.setText(holder.weather.getCity_nameTxt());
        int maxTemp = Integer.valueOf(holder.weather.getMax_tempTxt());
        int minTemp = Integer.valueOf(holder.weather.getMin_tempTxt());
        holder.max_tempHolder.setText(Integer.toString(maxTemp)+DEGREE_ICON);
        holder.min_tempHolder.setText(Integer.toString(minTemp)+DEGREE_ICON);

        dataService = new DataService();
        Bitmap bm = dataService.getImageData(holder.weather.getWeatherIcon());
        if (bm != null) {
            holder.imageView.setImageBitmap(bm);
        }
        return row;
    }
    public class ViewHolder {

        Weather weather;
        TextView city_nameHolder;
        TextView max_tempHolder;
        TextView min_tempHolder;
        ImageView imageView;
    }

    public void filter(String text){
        text = text.toLowerCase(Locale.getDefault());
        weatherList.clear();
        if (text.length() == 0) {
            weatherList.addAll(wData);
        }else {
            for (Weather weather : wData) {
                if (weather.getCity_nameTxt().toLowerCase(Locale.getDefault()).contains(text)) {
                    weatherList.add(weather);
                    wData = weatherList;
                }
            }
        }
        notifyDataSetChanged();
    }

}
