package com.csci448.npohl.npohl_a3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    // TODO: 4/11/17 add coordinator layout (according to slides) which will allow us to move fab 
    // TODO: 4/10/17 make floating action button
    //private FloatingActionButton mActionButton;


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
                updateUI();
            }
        });
/*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO: 4/10/17 make snackbar appear on clicking marker 
                //Snackbar sBar = Snackbar.make(getActivity(), mData.getSnackBarString(), SnackBar.LENGTH_SHORT).show();
                return false;
            }
        });
*/
        checkPermission();
        setHasOptionsMenu(true);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                //must check permission before accessing location
                checkPermission();
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
                mData = new PinData();
                mData.setLat(mCurrentLocation.getLatitude());
                mData.setLon(mCurrentLocation.getLongitude());
                mData.setDate(new Date());
                new FetchWeatherTask().execute();
                updateUI();
                return true;
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

    private void updateUI() {
        if (mMap == null || mCurrentLocation == null) {
            return;
        }

        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        mMap.clear();

        String markerTitle = getResources().getString(R.string.marker_title)+ ":(" + String.valueOf(mCurrentLocation.getLatitude()) + "," + String.valueOf(mCurrentLocation.getLongitude()) + ")";

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myPoint)
                .title(markerTitle));
        marker.setTag(mData);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .build();

    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, Void> {
        //used to store data on weather
        private String mCondition;
        private double mTemp;

        @Override
        protected Void doInBackground(Void... params) {
            //will probably need to start new fetcher, call getWeather() using location, then set vars
            WeatherFetch fetcher = new WeatherFetch();
            fetcher.getWeather(mCurrentLocation);
            //call getWeather(location);
            mCondition = fetcher.getCondition();
            mTemp = fetcher.getTemp();
            /*
            try {
                String call = new WeatherFetch().buildUrl(mCurrentLocation);
                String result = new WeatherFetch().getUrlString(call);
                Log.i(TAG, "Fetched contents of URL: " + result);
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            }
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mData.setCondition(mCondition);
            mData.setTemp(mTemp);
            Log.i(TAG, "Weather Condition: " + mCondition);
            Log.i(TAG, "Temperature: " + String.valueOf(mTemp));
            //save to database
        }
    }

}
