package edu.csulb.suitup;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;

import static edu.csulb.suitup.WardrobeMgmtActivity.decodeSampledBitmapFromResource;

/**
 * Created by Nhut on 12/3/2016.
 */

public class FileUtility {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final String directory = "/storage/emulated/0/DCIM/Camera";

    public ArrayList<File> getFiles()
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

    public ArrayList<ImageItem> getData() {
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
            System.out.println(imageItems.size());
            return imageItems;
        }
        catch (NullPointerException e)
        {
            System.err.println("NullPointerException: " + e.getMessage());
            return null;
        }

    }
}
