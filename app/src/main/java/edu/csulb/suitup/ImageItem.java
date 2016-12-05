package edu.csulb.suitup;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhut on 12/4/2016.
 */

/*
This class is used to display the Image when user clicks on gridView
 */

public class ImageItem {
    private Bitmap image;
    private String description;
    private ArrayList<String> tags;

    public ImageItem(Bitmap image, String desc, List<String> Tags) {
        super();
        this.image = image;
        this.description = desc;
        this.tags = new ArrayList<String>();
        this.tags.addAll(Tags);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> Tags) {
        tags.clear();
        tags.addAll(Tags);
    }
}
