package edu.csulb.suitup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by David on 11/17/2016.
 */

public class LocationActivity extends AppCompatActivity implements LocationListener, JsonResponse {

    private LocationManager locationManager;
    private LocationProvider gpsProvider;
    private final int MY_PERMISSION_GET_LOCATION_PERMISSIONS = 0;
    private Double latitude;
    private Double longitude;
    private String latLongLocation;
    private LocationActivity instance = this;

    private String weatherCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        latitude = 0.0;
        longitude = 0.0;
        latLongLocation = "";
        weatherCondition = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_GET_LOCATION_PERMISSIONS);
        }
        else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        final Button getLocation = (Button) findViewById(R.id.get_location_button);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });

        final Button getWeather = (Button) findViewById(R.id.get_weather_button);
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = getLocation();

                String yql = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + location + "\")";

                String jsonURL = "http://query.yahooapis.com/v1/public/yql?q=" + yql + "&format=json&env=store://datatables.org/alltableswithkeys";

                WeatherTask task = new WeatherTask(instance, location);
                task.execute(jsonURL);
            }
        });
    }

    public void setLocation() {
        TextView locationText = (TextView) findViewById(R.id.location_text);
        String location = getLocation();
        locationText.setText(location);
    }

    public String getLocation() {
        String cityStateZip = "";
        Geocoder geocoder = new Geocoder(this);
        List<Address> locations;
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        try {
            if (lastLocation == null) {
                return "";
            }
            locations = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 10);
        }
        catch (IOException e) {
            locations = null;
        }
        if (locations != null) {
            if (!locations.isEmpty()) {
                Address address = locations.get(0);
                cityStateZip = address.getAddressLine(1);
            }
            else {
                return latLongLocation;
            }
        }
        if (cityStateZip != null) {
            /* Can use this if we need only city and state.
            String[] locationInformation = cityStateZip.split("[, ]+");
            String city = locationInformation[0];
            String state = locationInformation[1];
            return city + ", " + state;
            */
            return cityStateZip;
        }
        else {
            return "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_GET_LOCATION_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
                else {
                    locationManager = null;
                    gpsProvider = null;
                }
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (latLongLocation.equals("") || latLongLocation.equals("Waiting for GPS...")) {
            latLongLocation = "Waiting for GPS...";
        }
        else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latLongLocation = "Lat: " + latitude + " Long: " + longitude;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void processJSON(String result) {
        try {
            JSONObject json = new JSONObject(result);
            JSONObject queryObject = json.getJSONObject("query");
            JSONObject resultsObject = queryObject.getJSONObject("results");
            JSONObject channelObject = resultsObject.getJSONObject("channel");
            JSONObject itemObject = channelObject.getJSONObject("item");
            JSONArray forecastArray = itemObject.getJSONArray("forecast");
            JSONObject forecastForToday = forecastArray.getJSONObject(0);
            weatherCondition = forecastForToday.getString("text");
            TextView weatherText = (TextView) findViewById(R.id.weather_text);
            weatherText.setText(weatherCondition);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
