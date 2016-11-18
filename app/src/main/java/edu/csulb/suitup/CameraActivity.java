package edu.csulb.suitup;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.CalendarContract.CalendarCache.URI;

public class CameraActivity extends AppCompatActivity{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    private ImageView mCameraResult;
    private static String mImagePath;
    private static String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_camera);
        mCameraResult = (ImageView) findViewById(R.id.camera_result);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
        getCategory();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK ) {
                    // Get a bitmap of the photo taken and the mask image
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap original = BitmapFactory.decodeFile(mImagePath,bmOptions);
                    Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.tshirt);

                    // This will rotate the original image 90degrees bc it comes out landscape before
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    original = Bitmap.createBitmap(original , 0, 0, original.getWidth(),
                            original.getHeight(), matrix, true);

                    // Use PorterDuff to mask original image with the mask image
                    Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    canvas.drawBitmap(original, 0, 0, null);
                    canvas.drawBitmap(mask, 0, 0, paint);
                    paint.setXfermode(null);
                    mCameraResult.setImageBitmap(result);

                    try {
                        // Overwrite the original file with the masked photo.
                        File pictureFile = new File(mImagePath);
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        result.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.close();

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("success", "Success");
                }
            }
        }
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Create the File where the photo should go
                photoFile = getFile(mCategory);
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "File Creation Failed", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                // Attach a File URI to the intent to link camera output with the file created
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File getFile(String category) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/"+category);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mImagePath = image.getAbsolutePath();
        return image;
    }


    private void getCategory(){
        final String[] items = {"Shirt", "Pants", "Shoes"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Category");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                mCategory = items[item];
                dialog.dismiss();
                // Opens up Camera to take a picture
                takePictureIntent();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
