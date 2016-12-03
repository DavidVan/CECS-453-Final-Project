package edu.csulb.suitup;

import android.graphics.Path;

/**
 * Created by Mark on 12/3/2016.
 */

public class Wardrobe {
    private String description;
    private String filepath;
    private String category;
    private int db_id;

    public Wardrobe(String desc, String path, String c){
        description = desc;
        filepath = path;
        category = c;
    }
    public Wardrobe(int id, String desc, String path, String c){
        int db_id = id;
        description = desc;
        filepath = path;
        category = c;
    }

    public int getId(){
        return db_id;
    }

    public String getDescription(){
        return description;
    }

    public String getFilepath(){
        return filepath;
    }

    public String getFileName(){
        return filepath.substring(filepath.lastIndexOf("/")+1);
    }

    public String getCategory(){
        return category;
    }
}
