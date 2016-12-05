package edu.csulb.suitup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Nhut on 12/2/2016.
 */

public class WardrobeMgmtActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final String directory = "/storage/emulated/0/DCIM/Test";
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Spinner viewSelectionSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardobe_mgmt);


        /*if (ContextCompat.checkSelfPermission(WardrobeMgmtActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(WardrobeMgmtActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        else
        {*/
        TextView tv = (TextView)findViewById(R.id.viewTxtView);

        //
        //Set up gridview
        /*gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.wardrobe_item, getData());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(WardrobeMgmtActivity.this, ItemDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });*/
        // Set up spinner
        viewSelectionSpinner = (Spinner)findViewById(R.id.viewSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(WardrobeMgmtActivity.this, R.array.view_selection, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSelectionSpinner.setAdapter(adapter);
        viewSelectionSpinner.setSelection(3);

        viewSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WardrobeFragment wardrobeFragment = (WardrobeFragment)getFragmentManager().findFragmentById(R.id.wardrobe_fragment);
                switch (viewSelectionSpinner.getSelectedItem().toString()){
                    case "Shirts":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setSize(15);
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "Pants":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setSize(25);
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "Shoes":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setSize(45);
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "All":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();

                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    //default:
                        //wardrobeFragment.setSize(100);
                        //getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment);
                        //break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    //}
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
                    Toast.makeText(WardrobeMgmtActivity.this,"We need this permission to+" +
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
    /*public ArrayList<File> getFiles()
    {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        ArrayList<File> inFiles = new ArrayList<File>();

        if (files != null)
        {
            for (File f: files)
            {
                if (f.getName().endsWith(".jpg")) {
                    inFiles.add(f);
                }
            }
        }
        else
            throw new NullPointerException("There is no files in the directory");
        return inFiles;
    }

    private ArrayList<ImageItem> getData() {
        ArrayList<File> nFiles;
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        try
        {
            nFiles = getFiles();
            for (int i = 0; i < nFiles.size(); i++)
            {
                File f = new File(directory, nFiles.get(i).getName());
                Bitmap b = decodeSampledBitmapFromResource(f,100,100);
                imageItems.add(new ImageItem(b, "Image#" + i));
            }
            return imageItems;
        }
        catch (NullPointerException e)
        {
            System.err.println("NullPointerException: " + e.getMessage());
            return null;
        }

    }*/

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(File f,
                                                         int reqWidth, int reqHeight) {

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), new Rect(1,1,1,1), options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(f), new Rect(1,1,1,1), options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
            return null;
        }

    }

}

