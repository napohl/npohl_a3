package com.csci448.npohl.npohl_a3;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nate on 4/11/2017.
 */

public class WeatherFetch {

    //final call should probably look like api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}

    private static final String TAG = "WeatherFetch";
    private static final String API_KEY = "141e2d4db8929655ec16033ce00c08f0";
    private static final String API_TAG = "APPID";
    private static final Uri API_CALL = Uri
            .parse("http://api.openweathermap.org/data/2.5/weather")
            .buildUpon()
            .build();

    private String mCondition;
    private double mTemp;

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        Log.i(TAG, "getUrlBytes URL = " + url.toString());

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    private String buildUrl(Location location) {
        return API_CALL.buildUpon()
                .appendQueryParameter("lat", "" + (int)location.getLatitude())
                .appendQueryParameter("lon", "" + (int)location.getLongitude())
                .appendQueryParameter(API_TAG, API_KEY)
                .build().toString();
    }

    // TODO: 4/12/17 add javadoc comment to this function
    public void getWeather(Location location) {
        String url = buildUrl(location);
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Recieved JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseData(jsonBody);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items: ", ioe);
        }
    }

    // TODO: 4/12/17 add javadoc comment to this function
    private void parseData(JSONObject jsonBody) throws IOException, JSONException {
        //only one object in the weather array we care about, the first one
        JSONObject weatherJsonObject = jsonBody.getJSONArray("weather").getJSONObject(0);
        JSONObject mainJsonObject = jsonBody.getJSONObject("main");

        mCondition = weatherJsonObject.getString("description");
        //we want
        mTemp = Double.valueOf(mainJsonObject.getString("temp"));
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String getCondition() {
        return mCondition;
    }

    public double getTemp() {
        return mTemp;
    }

    /*
    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items: ", ioe);
        }

        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            item.setLat(photoJsonObject.getDouble("latitude"));
            item.setLon(photoJsonObject.getDouble("longitude"));
            items.add(item);
        }
    }*/
}
