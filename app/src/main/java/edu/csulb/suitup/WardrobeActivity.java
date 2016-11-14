package edu.csulb.suitup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Nhut on 11/13/2016.
 */

public class WardrobeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String len = "something";
    Button checkBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrb_mgmt);
        File dir = new File("/storage/emulated/0/DCIM/Camera");
        File[] files = dir.listFiles();
        if (files != null)
            len = "----" + files.length;
        else
            len = "Null";

        checkBtn = (Button)findViewById(R.id.tag_button);
        if (ContextCompat.checkSelfPermission(WardrobeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            //whatever
            if (ActivityCompat.shouldShowRequestPermissionRationale(WardrobeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //whatever
            }
            else
            {
                ActivityCompat.requestPermissions(WardrobeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }
    public void onClick(View view)
    {
        if (view.getId() == R.id.tag_button)
            Toast.makeText(WardrobeActivity.this, "There is some files: " + len, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    File dir = new File(Environment.DIRECTORY_DOWNLOADS);
                    File[] files = dir.listFiles();
                    if (files != null)
                        len = "----" + files.length;
                    else
                        len = "Null";

                }
                else
                {
                    Toast.makeText(WardrobeActivity.this,"Boo! Not Granted",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
