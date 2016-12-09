package edu.csulb.suitup;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomWardrobeActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private Random mRand = new Random();
    private WardrobeCombination mCurrentCombination; 
    private List<Wardrobe> mTopList;
    private List<Wardrobe> mBottomList;
    private List<Wardrobe> mShoesList;
    private List<Wardrobe> mWeatherList;
    private List<Wardrobe> mWeatherListTops;
    private List<Wardrobe> mWeatherListBottoms;
    private List<Wardrobe> mWeatherListShoes;


    private List<WardrobeCombination> mExclusions;

    private ImageView topview;
    private ImageView bottomview;
    private ImageView shoesview;

    private CheckBox useWeather;

    private SensorManager sensorManager;
    private long lastUpdate;

    private String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_wardrobe);

        Intent intent = getIntent();
        weather = intent.getExtras().getString("weather");


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        lastUpdate = System.currentTimeMillis();

        //Initialize Views
        topview = (ImageView) findViewById(R.id.top_view);
        bottomview  = (ImageView) findViewById(R.id.bottom_view);
        shoesview = (ImageView) findViewById(R.id.shoes_view);

        useWeather = (CheckBox) findViewById(R.id.use_weather);

        // Get list of wardrobe from database
        WardrobeDbHelper dbhelper = new WardrobeDbHelper(getApplicationContext());

        mTopList = dbhelper.getTop();
        mBottomList = dbhelper.getBottom();
        mShoesList = dbhelper.getShoes();


        if (weather.equals("sunny")) {
            mWeatherList = dbhelper.getSunny();
        }
        else if (weather.equals("cloudy")) {
            mWeatherList = dbhelper.getCloudy();
        }
        else if (weather.equals("rainy")) {
            mWeatherList = dbhelper.getRainy();
        }
        else {
            mWeatherList = new ArrayList<>();
        }

        mWeatherListTops = new ArrayList<>();
        mWeatherListBottoms = new ArrayList<>();
        mWeatherListShoes = new ArrayList<>();

        for (Wardrobe w : mWeatherList) {
            if (w.getCategory().equals("Top")) {
                mWeatherListTops.add(w);
            }
            if (w.getCategory().equals("Bottom")) {
                mWeatherListBottoms.add(w);
            }
            if (w.getCategory().equals("Shoes")) {
                mWeatherListShoes.add(w);
            }
        }
        Log.d("sizeDavid", "" + mWeatherList.size());
        Log.d("sizeDavid", "" + mWeatherListTops.size());
        Log.d("sizeDavid", "" + mWeatherListBottoms.size());
        Log.d("sizeDavid", "" + mWeatherListShoes.size());

        // get list of exclusions from database
        mExclusions = dbhelper.getExclusions();

        // Ends activity if user does not have anything in one of the categories
        if(mTopList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a top apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(mBottomList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a bottom apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(mShoesList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a pair of shoes", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            generateCombination();
        }

        // Event Listeners for Buttons
        Button generateButton= (Button) findViewById(R.id.generate_button);
        Button excludeButton = (Button) findViewById(R.id.exclude_button);

        generateButton.setOnClickListener(this);
        excludeButton.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.generate_button){
            generateCombination();
        }
        if(v.getId()== R.id.exclude_button){
            excludeCombination();
        }
    }

    // Will return a random wardrobe chosen from a list of wardrobes
    private Wardrobe generateRandom(List<Wardrobe> list){
        int size = list.size();
        return list.get(mRand.nextInt(size));
    }

    private void generateCombination(){
        // Make sure you have enough wardrobe in store, taking in account the exclusions.
        int possibleCombinations = mTopList.size() * mBottomList.size() * mShoesList.size();
        if (possibleCombinations <= mExclusions.size()){
            Toast.makeText(getApplicationContext(), "Not enough Apparel added to make a combination",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        WardrobeCombination wardcombo;
        Wardrobe randtop;
        Wardrobe randbottom;
        Wardrobe randshoes;

        boolean uniquefound = false;

        // keep looking for a combination until you find one that's not excluded
        while(!uniquefound) {
            if (!mWeatherListTops.isEmpty() && useWeather.isChecked()) {
                List<Wardrobe> weatherTops = new ArrayList<>();
                for (Wardrobe w : mTopList) {
                    weatherTops.add(w);
                }
                weatherTops.retainAll(mWeatherListTops);
                randtop = generateRandom(weatherTops);
            }
            else {
                randtop = generateRandom(mTopList);
            }

            if (!mWeatherListBottoms.isEmpty() && useWeather.isChecked()) {
                List<Wardrobe> weatherBottoms = new ArrayList<>();
                for (Wardrobe w : mBottomList) {
                    weatherBottoms.add(w);
                }
                weatherBottoms.retainAll(mWeatherListBottoms);
                randbottom = generateRandom(weatherBottoms);
            }
            else {
                randbottom = generateRandom(mBottomList);
            }

            if (!mWeatherListShoes.isEmpty() && useWeather.isChecked()) {
                List<Wardrobe> weatherShoes = new ArrayList<>();
                for (Wardrobe w : mShoesList) {
                    weatherShoes.add(w);
                }
                weatherShoes.retainAll(mWeatherListShoes);
                randshoes = generateRandom(weatherShoes);
            }
            else {
                randshoes = generateRandom(mShoesList);
            }

            wardcombo = new WardrobeCombination(randtop.getId(), randbottom.getId(), randshoes.getId());
            // If new generated combo is not the same as the current one
            if(mCurrentCombination == null || !mCurrentCombination.equals(wardcombo)) {
                // if the new generated combo is not in the exclude list
                if (!mExclusions.contains(wardcombo)) {
                    uniquefound = true;
                    mCurrentCombination = wardcombo;

                    // Set View's Images
                    topview.setImageBitmap(BitmapFactory.decodeFile(randtop.getFilepath()));
                    bottomview.setImageBitmap(BitmapFactory.decodeFile(randbottom.getFilepath()));
                    shoesview.setImageBitmap(BitmapFactory.decodeFile(randshoes.getFilepath()));

                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
                    topview.startAnimation(myFadeInAnimation); //Set animation to your ImageView
                    bottomview.startAnimation(myFadeInAnimation);
                    shoesview.startAnimation(myFadeInAnimation);
                }
            }
        }
    }

    // Adds the current combination displayed to the database as excluded.
    public void excludeCombination(){
        mExclusions.add(mCurrentCombination);

        WardrobeDbHelper dbHelper = new WardrobeDbHelper(getApplicationContext());
        dbHelper.addExclusion(mCurrentCombination);

        // Make a new combination to replace current one on the GUI
        generateCombination();
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
                // Detected a shake. Show new random outfit.
                generateCombination();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing needed here.
    }
}
