package com.hasanozanal.sunshine.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasanozanal.sunshine.Model.Weather;
import com.hasanozanal.sunshine.R;

import java.util.ArrayList;

import com.hasanozanal.sunshine.Data.DataService;

/**
 * Created by ozanal on 4.04.2018.
 */

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ForecastViewHolder> {


    private ArrayList<Weather> mweatherArrayList;
    private DataService dataService = new DataService();
    private Weather weather;

    public WeatherRecyclerAdapter(ArrayList<Weather> weatherArrayList) {
        mweatherArrayList = weatherArrayList;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather,parent,false);
        return new ForecastViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Weather weather = mweatherArrayList.get(position);
        holder.updateUI(weather);
    }

    @Override
    public int getItemCount() {
        return mweatherArrayList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private ImageView weatherIcon;
        private TextView weatherDate;
        private TextView weatherDescription;
        private TextView maxTemp;
        private TextView minTemp;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            weatherIcon = (ImageView) itemView.findViewById(R.id.cardViewImgId);
            weatherDate = (TextView) itemView.findViewById(R.id.weatherDayCardId);
            weatherDescription = (TextView) itemView.findViewById(R.id.descriptionCardId);
            maxTemp = (TextView) itemView.findViewById(R.id.maxTempCardId);
            minTemp = (TextView) itemView.findViewById(R.id.minTempCardId);

        }
        public void updateUI(Weather weather){
            weatherDescription.setText(weather.getDescriptionTxt());
            maxTemp.setText(Double.toString(weather.getMax_tempTxt()));
            minTemp.setText(Double.toString(weather.getMin_tempTxt()));
            weatherDate.setText(weather.getForecastDate());
            Bitmap bm = dataService.getImageData(weather.getWeatherIcon());
            if (bm != null){
                weatherIcon.setImageBitmap(bm);
            }
        }
    }
}
