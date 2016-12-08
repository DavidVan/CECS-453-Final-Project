package edu.csulb.suitup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, JsonResponse, SensorEventListener {

    private LocationManager locationManager;
    private LocationProvider gpsProvider;
    private final int MY_PERMISSION_GET_LOCATION_PERMISSIONS = 0;
    private Double latitude;
    private Double longitude;
    private String latLongLocation;
    private MainActivity instance = this;
    private SensorManager sensorManager;
    private long lastUpdate;

    private String weatherCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera_button = (Button) findViewById(R.id.camera_button);
        camera_button.setOnClickListener(this);

        Button random_button = (Button) findViewById(R.id.random_button);
        random_button.setOnClickListener(this);

        Button wardrobe_button = (Button) findViewById(R.id.wardrobe_button);
        wardrobe_button.setOnClickListener(this);

        latitude = 0.0;
        longitude = 0.0;
        latLongLocation = "";
        weatherCondition = "";

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        lastUpdate = System.currentTimeMillis();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_GET_LOCATION_PERMISSIONS);
        }
        else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            setLocation();
            String location = getLocation();

            String yql = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + location + "\")";

            String jsonURL = "http://query.yahooapis.com/v1/public/yql?q=" + yql + "&format=json&env=store://datatables.org/alltableswithkeys";

            WeatherTask task = new WeatherTask(instance, location);
            task.execute(jsonURL);
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.camera_button){
            startActivity(new Intent(this, CameraActivity.class));
        }
        if(v.getId() == R.id.wardrobe_button){
            startActivity(new  Intent(this, WardrobeMgmtActivity.class));
        }
        if(v.getId() == R.id.random_button){
            startActivity(new  Intent(this, RandomWardrobeActivity.class));
        }
    }

    public void setLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_GET_LOCATION_PERMISSIONS);
        }
        TextView locationText = (TextView) findViewById(R.id.location_information);
        String location = getLocation();
        locationText.setText(location);
    }

    public String getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_GET_LOCATION_PERMISSIONS);
        }
        String city = "";
        String state = "";
        String zip = "";
        Geocoder geocoder = new Geocoder(this);
        List<Address> locations;
        Location lastLocation;
        if (locationManager != null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            return "";
        }

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
                city = address.getLocality();
                state = address.getAdminArea().toUpperCase().substring(0, 2);
                zip = address.getPostalCode();
            }
            else {
                return latLongLocation;
            }
        }
        if (city != null && state != null && zip != null) {
            // Can use this if we need only city and state.
            return city + ", " + state;
            // Below returns City, State, and ZIP.
            // return city + ", " + state + " " + zip;
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
                    setLocation();
                    String location = getLocation();

                    String yql = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + location + "\")";

                    String jsonURL = "http://query.yahooapis.com/v1/public/yql?q=" + yql + "&format=json&env=store://datatables.org/alltableswithkeys";

                    WeatherTask task = new WeatherTask(instance, location);
                    task.execute(jsonURL);
                }
                else {
                    locationManager = null;
                    gpsProvider = null;
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_GET_LOCATION_PERMISSIONS);
                    }
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
            TextView weatherText = (TextView) findViewById(R.id.weather_information);
            weatherText.setText(weatherCondition);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_camera) {
            Intent information = new Intent(this, CameraActivity.class);
            startActivity(information);
            return true;
        }
        else if (id == R.id.uninstall) {
            Intent uninstall = new Intent(Intent.ACTION_DELETE, Uri.parse("package:edu.csulb.suitup"));
            startActivity(uninstall);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            long actualTime = System.currentTimeMillis();

            if (accelerationSquareRoot >= 10) {
                if (actualTime - lastUpdate < 500) {
                    return;
                }
                lastUpdate = actualTime;
                // Detected a shake. Show random outfit.
                Intent intent = new Intent(this, RandomWardrobeActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing needed here...
    }

    public String getWeather() {
        String weather = ((TextView) findViewById(R.id.weather_information)).getText().toString().toLowerCase();
        Log.d("weather", weather);
        String realWeather = "";
        if (weather.contains("sunny") || weather.contains("clear")) {
            realWeather = "sunny";
        }
        else if (weather.contains("cloud") || weather.contains("dark")) {
            realWeather = "cloudy";
        }
        else if (weather.contains("rain") || weather.contains("shower") || weather.contains("storm") || weather.contains("thunder")) {
            realWeather = "rain";
        }
        return realWeather;
    }

}
