package com.csci448.npohl.npohl_a3;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is a helper class used to call the OpenWeatherMap API and get the data back from it
 */
public class WeatherFetch {

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

    /**
     * This function is called from the fragment to help get the weather.
     *
     * This does not return any parameters, but sets up the url to query the api using the given location.
     * The fragment will have to get the values of temperature and weather condition after calling this function
     *
     * @param location the location that we want to get the weather for
     */
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

    /**
     * This function helps get the relevant data from the JSON OBject we recieve back from the API call
     *
     * @param jsonBody the object to be parsed
     * @throws IOException may not be able to fetch any items
     * @throws JSONException may not be able to parse the JSON file given to it
     */
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

}
