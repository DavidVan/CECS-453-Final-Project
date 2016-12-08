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
    private int id;
    private Bitmap image;
    private String description;
    private ArrayList<String> tags;
    private String category;
    private String filepath;

    public ImageItem(int _id, Bitmap image, String desc, List<String> Tags, String cat, String filep) {
        super();
        this.id = _id;
        this.image = image;
        this.description = desc;
        this.tags = new ArrayList<String>(Tags);
        this.category = cat;
        this.filepath = filep;
    }

    public int getId(){
        return id;
    }

    public void setId(int _id){
        this.id = _id;
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

    public String getCategory(){
        return category;
    }
    public void setCategory(String cat){
        category = cat;
    }
    public String getFilepath() {return filepath;}
    public void setFilepath(String path) {filepath = path;}
}
