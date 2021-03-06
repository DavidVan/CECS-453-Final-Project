package edu.csulb.suitup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WardrobeMgmtActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Spinner viewSelectionSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private WardrobeFragment wardrobeFragment;
    private String type = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe_mgmt);

        viewSelectionSpinner = (Spinner)findViewById(R.id.viewSpinner);
        adapter = ArrayAdapter.createFromResource(WardrobeMgmtActivity.this, R.array.view_selection, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSelectionSpinner.setAdapter(adapter);
        viewSelectionSpinner.setSelection(3);

        wardrobeFragment = (WardrobeFragment)getFragmentManager().findFragmentById(R.id.wardrobe_fragment);

        viewSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (viewSelectionSpinner.getSelectedItem().toString()){
                    case "Top":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setType("Top");
                        type = "Top";
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "Bottom":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setType("Bottom");
                        type = "Bottom";
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "Shoes":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setType("Shoes");
                        type = "Shoes";
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                    case "All":
                        Toast.makeText(WardrobeMgmtActivity.this,viewSelectionSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        wardrobeFragment.setType("All");
                        type = "All";
                        getFragmentManager().beginTransaction().detach(wardrobeFragment).attach(wardrobeFragment).commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        wardrobeFragment.setType(type);
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
}
