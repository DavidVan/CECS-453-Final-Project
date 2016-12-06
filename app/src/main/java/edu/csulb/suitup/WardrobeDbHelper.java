package edu.csulb.suitup;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nhut on 11/18/2016.
 */

public class WardrobeDbHelper extends SQLiteOpenHelper {
    // List of column for all tables
    // _id is global to all 3 tables
    public static final String ID_COLUMN = "_id";

    // wardrobe table columns
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String FILEPATH_COLUMN = "filepath";
    public static final String CATEGORY_COLUMN = "category";

    // tags table cloumns
    public static final String TAGS_COLUMN = "tags";
    public static final String CLOTHES_ID_COLUMN = "clothesid";

    // exclusion table columns
    public static final String TOP_ID_COLUMN = "topid";
    public static final String BOTTOM_ID_COLUMN = "bottomid";
    public static final String SHOES_ID_COLUMN = "shoesid";

    // DB name, version and table names
    public static final String DATABASE_NAME = "WardrobeManager";
    public static final String WARDROBE_TABLE_NAME = "Wardrobe";
    public static final String TAG_TABLE_NAME = "Tags";
    public static final String EXCLUSION_TABLE_NAME = "Exclusion";
    public static final int DATABASE_VERSION = 1;

    // Create statement
    private static final String WARDROBE_TABLE_CREATE = String.format(
            "CREATE TABLE %s(" +                                //table name
                    "%s integer primary key autoincrement, " +   // _id
                    "%s text, " +                                // description
                    "%s text, " +                                 // filepath
                    "%s text)",                                 // category
            WARDROBE_TABLE_NAME, ID_COLUMN, DESCRIPTION_COLUMN, FILEPATH_COLUMN, CATEGORY_COLUMN);

    private static final String TAG_TABLE_CREATE = String.format(
            "CREATE TABLE %s(" +                                // TABLE NAME
                    "%s integer primary key autoincrement, " +   // _id
                    "%s integer not null check(%s > 0), " +      // clothes id
                    "%s text, " +                                // tags
                    "foreign key(%s) references %s(%s))",       // clothes id foreign key to wardrobe table, _id
            TAG_TABLE_NAME,
            ID_COLUMN,
            CLOTHES_ID_COLUMN, CLOTHES_ID_COLUMN,
            TAGS_COLUMN,
            CLOTHES_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN);

    private static final String EXCLUSION_TABLE_CREATE = String.format(
            "CREATE TABLE %s(" +                                    //table name
                    "%s integer primary key autoincrement, " +       // _id
                    "%s integer check(%s > 0), " +                   // top_id
                    "%s integer check(%s > 0), " +                   // bottom id
                    "%s integer check(%s > 0), " +                   // shoes id
                    "foreign key(%s) references %s(%s), " +          // top id foreign key to wardrobe table, _id
                    "foreign key(%s) references %s(%s), " +          // bottom id foreign key to wardrobe table, _id
                    "foreign key(%s) references %s(%s))",           // bottom id foreign key to wardrobe table, _id
            EXCLUSION_TABLE_NAME, ID_COLUMN,
            TOP_ID_COLUMN, TOP_ID_COLUMN,
            BOTTOM_ID_COLUMN, BOTTOM_ID_COLUMN,
            SHOES_ID_COLUMN, SHOES_ID_COLUMN,
            TOP_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN,
            BOTTOM_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN,
            SHOES_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN
    );
    // Constructor
    public WardrobeDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method: create 3 tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(WARDROBE_TABLE_CREATE);
        db.execSQL(TAG_TABLE_CREATE);
        db.execSQL(EXCLUSION_TABLE_CREATE);
    }

    // onUpgrade: drop table and re-create
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + WARDROBE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCLUSION_TABLE_CREATE);
        // Create again
        onCreate(db);
    }

    // add a new exclusion passing in shirt/pants/shoes ids
    public void addExclusion(int top, int bottom, int shoes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOP_ID_COLUMN, top);
        values.put(BOTTOM_ID_COLUMN, bottom);
        values.put(SHOES_ID_COLUMN, shoes);

        // Inserting Row
        db.insert(EXCLUSION_TABLE_NAME, null, values);
        db.close();
    }

    // Adds a new wardrobe into the database
    public void addWardrobe(String desc, String filepath, String category){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COLUMN, desc);
        values.put(FILEPATH_COLUMN, filepath);
        values.put(CATEGORY_COLUMN, category);

        // Inserting Row
        db.insert(WARDROBE_TABLE_NAME, null, values);
        db.close();
    }

    // Adds a new tag into the database
    public void addTag(int clothes_id, String tag){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLOTHES_ID_COLUMN, clothes_id);
        values.put(TAGS_COLUMN, tag);

        // Inserting Row
        db.insert(TAG_TABLE_NAME, null, values);
        db.close();
    }

    // Retrieve all
    public List<Wardrobe> getAll()
    {
        List<Wardrobe> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + WARDROBE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String filepath = cursor.getString(2);
                String category = cursor.getString(3);

                all.add(new Wardrobe(id, description, filepath, category));
            }while(cursor.moveToNext());
        }
        // closing connections
        cursor.close();
        db.close();

        return all;
    }


    // Retrieve a list of Top Wardrobe Objects
    public List<Wardrobe> getTop(){
        List<Wardrobe> tops = new ArrayList<>();
        String selectQuery =
                "SELECT * FROM " + WARDROBE_TABLE_NAME +
                " WHERE " + CATEGORY_COLUMN + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {"Top"});

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String filepath = cursor.getString(2);
                String category = cursor.getString(3);
                tops.add(new Wardrobe(id, description, filepath, category));
            }while(cursor.moveToNext());
        }
        // closing connections
        cursor.close();
        db.close();

        return tops;
    }

    // Retrieve a list of Bottom Wardrobe Objects
    public List<Wardrobe> getBottom(){
        List<Wardrobe> bottoms = new ArrayList<>();
        String selectQuery =
                "SELECT * FROM " + WARDROBE_TABLE_NAME +
                        " WHERE " + CATEGORY_COLUMN + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {"Bottom"});

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String filepath = cursor.getString(2);
                String category = cursor.getString(3);

                bottoms.add(new Wardrobe(id, description, filepath, category));
            }while(cursor.moveToNext());
        }
        // closing connections
        cursor.close();
        db.close();

        return bottoms;
    }

    // Retrieve a list of Shoe Wardrobe Objects
    public List<Wardrobe> getShoes(){
        List<Wardrobe> shoes = new ArrayList<>();
        String selectQuery =
                "SELECT * FROM " + WARDROBE_TABLE_NAME +
                        " WHERE " + CATEGORY_COLUMN + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {"Shoes"});

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String filepath = cursor.getString(2);
                String category = cursor.getString(3);

                shoes.add(new Wardrobe(id, description, filepath, category));
            }while(cursor.moveToNext());
        }
        // closing connections
        cursor.close();
        db.close();

        return shoes;
    }

    // Retrieve a HashMap for a wardrobe's tags
    // The Ids as the key and a list of tags as the value
    // Not sure if we need this hashmap ... but might come in handy for the future.
    public HashMap<Integer, List<String>> getTags(){
        HashMap<Integer, List<String>> tagsmap = new HashMap<>();
        String selectQuery = "SELECT * FROM " + EXCLUSION_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looper through all rows and adding to hashmap
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(1);
                String tag = cursor.getString(2);

                // if clothes id already exists in hashmap, add the tag to its list
                if(tagsmap.containsKey(id)){
                    tagsmap.get(id).add(tag);
                }
                else{
                    // add a new k/v pair into the map
                    List<String> taglist = new ArrayList<>();
                    taglist.add(tag);
                    tagsmap.put(id, taglist);
                }
            }while(cursor.moveToNext());
        }

        // closing connections
        cursor.close();
        db.close();

        return tagsmap;
    }

    public void getTag(int clothes_id){

    }

    // Get a list of wardrobe combinations that were excluded
    public List<WardrobeCombination> getExclusions(){
        List<WardrobeCombination> combo = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EXCLUSION_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looper through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                combo.add(new WardrobeCombination(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
            }while(cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return combo;
    }


    // TODO: Fill these in
    public void removeExclusion(int exclusion_id){

    }

    public void removeWardrobe(int clothes_id){

    }

    public void removeTag(int clothes_id){
        String deleteQuery = "DELETE FROM " + TAG_TABLE_NAME + "WHERE " + CLOTHES_ID_COLUMN + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(deleteQuery, new String[] {Integer.toString(clothes_id)});

    }

    public void removeTag(int clothes_id, String tag){


    }
}
