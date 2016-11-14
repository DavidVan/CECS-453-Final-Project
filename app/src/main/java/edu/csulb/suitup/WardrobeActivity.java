package edu.csulb.suitup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Created by Nhut on 11/13/2016.
 */

public class WardrobeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrb_mgmt);


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
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.wrb_item_gridview_layout, getData());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(WardrobeActivity.this, ItemDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(WardrobeActivity.this,"We need this permission to+" +
                            "list your clothes. Please reinstall the app+" +
                            "and allow us to access the storage",Toast.LENGTH_LONG).show();
                    Uri packageURI = Uri.parse("package:edu.csulb.suitup");
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public File[] getFiles()
    {
        String directory = "/storage/emulated/0/DCIM/Test";
        File dir = new File(directory);
        File[] files = dir.listFiles();

        if (files != null)
            return files;
        else
            throw new NullPointerException("There is no files in the directory");
    }

    private ArrayList<ImageItem> getData() {
        File[] nFiles;
        String imgPath = "/storage/emulated/0/DCIM/Test";
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        try
        {
            nFiles = getFiles();
        }
        catch (NullPointerException e)
        {
            System.err.println("NullPointerException: " + e.getMessage());
        }
        finally {
            nFiles = new File(imgPath).listFiles();
        }


        for (int i = 0; i < nFiles.length; i++)
        {
            try {
                File f=new File(imgPath, nFiles[i].getName());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageItems.add(new ImageItem(b, "Image#" + i));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return imageItems;
    }

}

