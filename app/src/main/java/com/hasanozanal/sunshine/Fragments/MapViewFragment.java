package com.hasanozanal.sunshine.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hasanozanal.sunshine.R;
import com.hasanozanal.sunshine.Helpers.DBHelper;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private MarkerOptions userMarker;
    private DBHelper dbHelper = new DBHelper(getActivity());

    public MapViewFragment() {
    }
    public static MapViewFragment newInstance(String param1, String param2) {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
    }

    public void setUserMarker(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        if (userMarker == null){
            userMarker = new MarkerOptions().position(userLocation).title("Current Location");
            mMap.addMarker(userMarker);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (mMap != null){
            getLatAndLon(latLng);
        }
    }

    private void getLatAndLon(final LatLng latLng) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to add this location?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Double lat = latLng.latitude;
                Double lon = latLng.longitude;
                dbHelper = new DBHelper(getActivity());
                dbHelper.addWeather(lat,lon);
                dbHelper.close();
                dialog.dismiss();

                MainFragment mainFragment = new MainFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerId,mainFragment);
                transaction.commit();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
