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
        db_id = id;
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

    @Override
    public boolean equals(Object object) {
        Wardrobe w = (Wardrobe) object;
        if (this.description.equals(w.description) && this.filepath.equals(w.filepath) && this.category.equals(w.category)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }
}
