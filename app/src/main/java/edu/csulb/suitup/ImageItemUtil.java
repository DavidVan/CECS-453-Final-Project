package edu.csulb.suitup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nhut on 12/4/2016.
 */

public class ImageItemUtil {
    private WardrobeDbHelper db;

    public ImageItemUtil(Context context)
    {
        db = new WardrobeDbHelper(context);
    }

    /*
    This method is used to read image file from disk
    using information from database. And then put them
    into the ImageItem
     */
    public ArrayList<ImageItem> getData(String type) {
        List<Wardrobe> wardrobe_item = new ArrayList<>();
        HashMap<Integer, List<String>> tagsmap = new HashMap<Integer, List<String>>(db.getTags());
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        switch (type){
            case ("Top"):
                wardrobe_item = db.getTop();
                break;
            case ("Bottom"):
                wardrobe_item = db.getBottom();
                break;
            case ("Shoes"):
                wardrobe_item = db.getShoes();
                break;
            case ("All"):
                wardrobe_item = db.getAll();
                break;
        }

        try
        {
            for (int i = 0; i < wardrobe_item.size(); i++)
            {
                Wardrobe wardrobe = wardrobe_item.get(i);
                String filepath = wardrobe.getFilepath();
                List<String> tags = new ArrayList<String>();
                tags.add("fucking awesome");
                File f = new File(filepath);
                Bitmap b = decodeSampledBitmapFromResource(f,100,100);
                System.out.println(tags.get(0));
                ImageItem imageItem = new ImageItem(b, wardrobe.getDescription(), tags);
                imageItems.add(imageItem);
            }
            return imageItems;
        }
        catch (NullPointerException e)
        {
            System.err.println("Caught a NullPointerException: " + e.getMessage());
            return imageItems;
        }
    }

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
