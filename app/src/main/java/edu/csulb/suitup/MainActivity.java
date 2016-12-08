package edu.csulb.suitup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera_button = (Button) findViewById(R.id.camera_button);
        camera_button.setOnClickListener(this);

        Button location_button = (Button) findViewById(R.id.location_button);
        location_button.setOnClickListener(this);

        Button random_button = (Button) findViewById(R.id.wardrobe_button);
        random_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.camera_button){
            startActivity(new Intent(this, CameraActivity.class));
        }
        if(v.getId()== R.id.location_button){
            startActivity(new Intent(this, LocationActivity.class));
        }
        if(v.getId() == R.id.wardrobe_button){
            startActivity(new  Intent(this, WardrobeMgmtActivity.class));
        }
    }

}
