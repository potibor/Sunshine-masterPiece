package com.hasanozanal.sunshine.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hasanozanal.sunshine.Data.Api;
import com.hasanozanal.sunshine.Data.ApiInterface;
import com.hasanozanal.sunshine.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.hasanozanal.sunshine.Data.DataService;
import com.hasanozanal.sunshine.Data.JSONHandler;
import com.hasanozanal.sunshine.Helpers.DBHelper;
import com.hasanozanal.sunshine.Model.Weather;
import com.hasanozanal.sunshine.Adapters.WeatherAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment implements LocationListener,SearchView.OnQueryTextListener {

    //region Static Values

    private static String DEGREE_ICON = "\u00b0";

    //endregion

    private int index = 0;

    private Location mLocation;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallBack;
    private LocationRequest mLocationRequest;

    //region Views

    private TextView currentDateTxt;
    private TextView maxTempTxt;
    private TextView minTempTxt;
    private TextView cityNameTxt;
    private TextView descriptionTxt;
    private ImageView imageView;
    private ImageView list_image_View;
    private ImageButton addLocationBtn;
    private ImageButton webViewBtn;
    private Button settingsBtn;
    private ListView weatherList;
    private SearchView searchView;
    private String unit;
    //endregion
    // region Instances
    private Weather weather;
    private DBHelper dbHelper;
    private JSONHandler jsonHandler;
    private DataService dataService;
    private JSONObject jsonObject;
    private MapViewFragment mapViewFragment;
    private Dialog ratingDialog;
    private ArrayList<Weather> locations;
    private WeatherAdapter weatherAdapter;
    private DetailsFragment detailsFragment = new DetailsFragment();
    private ApiInterface api;
    // endregion

    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    //region Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setFields(view);
        SharedPreferences();

        dbHelper = new DBHelper(getActivity());
        dataService = new DataService();
        api = Api.getApiClient().create(ApiInterface.class);

        setLocationChangeAction();
        getLastKnownLocation();

        locations = dbHelper.getWeather();
        weatherAdapter = new WeatherAdapter(getActivity(),R.layout.location_row,locations);
        weatherList.setAdapter(weatherAdapter);
        weatherAdapter.notifyDataSetChanged();
        index = 0;
        requestForListView();
        getCurrentLocation();

        // region ButtonClicks
        weatherList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                deleteListItem(position);
                weatherAdapter.notifyDataSetChanged();
                return true;
            }
        });
        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weather = locations.get(position);
                updateDetails(weather);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerId,detailsFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapViewFragment mapViewFragment = new MapViewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerId, mapViewFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        webViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewFragment webViewFragment = new WebViewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerId, webViewFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerId,settingsFragment).addToBackStack(null);
                transaction.commit();
            }
        });
        // endregion

        searchView.setOnQueryTextListener(this);
        return view;
    }

    //endregion

    @Override
    public void onLocationChanged(Location location) {
        mapViewFragment.setUserMarker(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                getCurrentLocation();
                break;
            default:
                break;
        }
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
        } else {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(100);
            mLocationRequest.setInterval(1000);
            mLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, null);
        }
    }

    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);

        } else mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void setLocationChangeAction() {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLocation = locationResult.getLastLocation();
                Call<String> call = api.getWeather(mLocation.getLatitude(),mLocation.getLongitude(),Api.API_KEY_URL,unit);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String url = response.body();
                        try {
                            jsonObject = new JSONObject(url);
                            jsonHandler = new JSONHandler();
                            weather = jsonHandler.JSONWeatherHandler(jsonObject);
                            updateUI(weather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("error","" + t.getLocalizedMessage());
                    }
                });
            }
        };
    }

    public void requestForListView() {
        if (locations.size() == 0){
            return;
        }
        Call<String> call = api.getWeather(locations.get(index).getLat(),locations.get(index).getLon(),Api.API_KEY_URL,unit);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String url = response.body();
                try {
                    jsonObject = new JSONObject(url);
                    jsonHandler = new JSONHandler();
                    weather = jsonHandler.JSONWeatherHandler(jsonObject);
                    locations.set(index, weather);
                    index ++;
                    if(index < locations.size())
                        requestForListView();
                    else
                        weatherAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateUI(Weather weather){
        cityNameTxt.setText(weather.getCity_nameTxt() + "," + weather.getCountry_nameTxt());
        maxTempTxt.setText(Integer.toString(weather.getMax_tempTxt()) + DEGREE_ICON);
        minTempTxt.setText(Integer.toString(weather.getMin_tempTxt())+ DEGREE_ICON);
        descriptionTxt.setText(weather.getDescriptionTxt());
        currentDateTxt.setText(weather.getDate());
        Bitmap imageIcon = dataService.getImageData(weather.getWeatherIcon());
        if (imageIcon != null){
            imageView.setImageBitmap(imageIcon);
        }
    }

    public void deleteListItem(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mId = locations.get(position).getItemId();
                        Weather item = locations.get(position);
                        weatherAdapter.remove(item);
                        dbHelper.deleteItem(mId);
                        weatherAdapter.notifyDataSetChanged();
                        dbHelper.close();
                        dialog.dismiss();
                    }
                });
                builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void updateDetails(Weather weather){
        Bundle args = new Bundle();
        args.putDouble("lat", weather.getLat());
        args.putDouble("lon", weather.getLon());
        detailsFragment.setArguments(args);
    }

    public void setFields(View view){
        addLocationBtn = (ImageButton) view.findViewById(R.id.addLocationBtnId);
        webViewBtn = (ImageButton) view.findViewById(R.id.QuestionMarkBtnId);
        settingsBtn = (Button) view.findViewById(R.id.fragment_main_settings_btn);
        currentDateTxt = (TextView) view.findViewById(R.id.currentDateId);
        maxTempTxt = (TextView) view.findViewById(R.id.max_tempTextId);
        minTempTxt = (TextView) view.findViewById(R.id.min_tempTxtId);
        cityNameTxt = (TextView) view.findViewById(R.id.city_nameTxtId);
        descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxtId);
        imageView =(ImageView) view.findViewById(R.id.currentWeatherImgId);
        weatherList = (ListView) view.findViewById(R.id.listviewId);
        searchView = (SearchView) view.findViewById(R.id.searchViewId);
        list_image_View = (ImageView) view.findViewById(R.id.list_row_weatherImgId);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()){
            weatherAdapter = new WeatherAdapter(getActivity(),R.layout.location_row,locations);
            weatherList.setAdapter(weatherAdapter);
            weatherAdapter.notifyDataSetChanged();
        }
        else{
            weatherAdapter.filter(newText);
        }
        return false;
    }

    public void rateDialog(){
        ratingDialog = new Dialog(getActivity());
        ratingDialog.setContentView(R.layout.custom_popup);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingDialog.show();
    }

    public void SharedPreferences(){
        SharedPreferences preferences = getActivity().getSharedPreferences("switch",0);
        if (preferences.getBoolean("Celcius",true)){
            unit = "metric";
        }else{
            unit = "imperial";
        }
    }
}
