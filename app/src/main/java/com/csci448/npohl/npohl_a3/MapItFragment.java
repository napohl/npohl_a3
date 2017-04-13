package com.csci448.npohl.npohl_a3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tagmanager.Container;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static android.support.design.R.styleable.FloatingActionButton;

public class MapItFragment extends SupportMapFragment {
    private static final String TAG = "MapItFragment";

    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private Location mCurrentLocation;
    private Marker mMarker;
    private PinData mData;
    private android.support.design.widget.FloatingActionButton mAddLocation;

    public static MapItFragment newInstance() {
        return new MapItFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // TODO: 4/10/17 make snackbar appear on clicking marker
                        PinData data = (PinData) marker.getTag();
                        String message = data.getSnackBarString();
                        Snackbar sBar = Snackbar.make(getActivity().findViewById(R.id.fragment_container), message, Snackbar.LENGTH_INDEFINITE);
                        sBar.show();
                        return false;
                    }
                });
                updateUI();
            }
        });

        mAddLocation = (FloatingActionButton) getActivity().findViewById(R.id.add_location_button);
        mAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocation();
            }
        });
        checkPermission();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mapit, menu);

        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
        MenuItem deleteDatabase = menu.findItem(R.id.action_delete);
        deleteDatabase.setEnabled(AllData.get(getActivity()).getAllPinData().size() != 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                addLocation();
                return true;
            case R.id.action_delete:
                mMap.clear();
                AllData.get(getActivity()).deleteDatabase();
                updateUI();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkPermission() {
        Log.d(TAG, "checkPermission()");
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "Requesting Permission");
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
    }

    private void addLocation() {
        //must check permission before accessing location
        checkPermission();
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        mData = new PinData();
        mData.setLat(mCurrentLocation.getLatitude());
        mData.setLon(mCurrentLocation.getLongitude());
        mData.setDate(new Date());
        new FetchWeatherTask().execute();
    }

    // TODO: 4/12/2017 add javadoc comments
    private void updateUI() {
        //if we don't have a map or location, skip updating the UI altogether
        if (mMap == null) {
            return;
        }
        
        AllData allData = AllData.get(getActivity());
        List<PinData> pinDatas = allData.getAllPinData();

        for (PinData pinData : pinDatas) {
            LatLng point = new LatLng(pinData.getLat(), pinData.getLon());
            String markerTitle = getResources().getString(R.string.marker_title)+ ":(" + String.valueOf(pinData.getLat()) + "," + String.valueOf(pinData.getLon()) + ")";

            Marker marker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(markerTitle));
            marker.setTag(pinData);
        }
/*
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        String markerTitle = getResources().getString(R.string.marker_title)+ ":(" + String.valueOf(mCurrentLocation.getLatitude()) + "," + String.valueOf(mCurrentLocation.getLongitude()) + ")";

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myPoint)
                .title(markerTitle));
        marker.setTag(mData);
        */

    }

    /**
     * This function assumes the input is in kelvin, and will convert to Fahrenheit
     * This is because the weather api we are using returns the temperature in Kelvin
     *
     * @param temp temperature in Kelvin
     */
    private int convertTemp(double temp) {
        double convert = temp * 9/5;
        return (int)convert - 460;
    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, Void> {
        //used to store data on weather
        private String mCondition;
        private int mTemp;

        @Override
        protected Void doInBackground(Void... params) {
            //will probably need to start new fetcher, call getWeather() using location, then set vars
            WeatherFetch fetcher = new WeatherFetch();
            fetcher.getWeather(mCurrentLocation);
            mCondition = fetcher.getCondition();
            //must convert temperature when we receive the data
            mTemp = convertTemp(fetcher.getTemp());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mData.setCondition(mCondition);
            mData.setTemp(mTemp);
            Log.i(TAG, "Weather Condition: " + mCondition);
            Log.i(TAG, "Temperature: " + String.valueOf(mTemp));
            //save to database
            AllData.get(getActivity()).addData(mData);
            //update with marker after we have all data
            updateUI();
        }
    }

}
