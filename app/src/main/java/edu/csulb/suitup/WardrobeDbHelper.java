package edu.csulb.suitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nhut on 11/18/2016.
 */

public class WardrobeDbHelper extends SQLiteOpenHelper {
    // List of column for all tables
    // _id is global to all 3 tables
    public static final String ID_COLUMN = "_id";

    // wardrobe table columns
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DIRECTORY_COLUMN = "directory";
    public static final String FILENAME_COLUMN = "filename";
    public static final String CATEGORY_COLUMN = "category";

    // tags table cloumns
    public static final String TAGS_COLUMN = "tags";
    public static final String CLOTHES_ID_COLUMN = "clothesid";

    // exclusion table columns
    public static final String SHIRT_ID_COLUMN = "shirtid";
    public static final String PANT_ID_COLUMN = "pantid";
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
                    "%s integer primary key autoincrement," +   // _id
                    "%s text," +                                // description
                    "%s text," +                                // directory
                    "%s text" +                                 // filename
                    "%s text)",                                 // category
            WARDROBE_TABLE_NAME, ID_COLUMN, DESCRIPTION_COLUMN, DIRECTORY_COLUMN, FILENAME_COLUMN, CATEGORY_COLUMN);

    private static final String TAG_TABLE_CREATE = String.format(
            "CREATE TABLE %s(" +                                // TABLE NAME
                    "%s integer primary key autoincrement," +   // _id
                    "%s integer not null check(%s > 0)," +      // clothes id
                    "%s text," +                                // tags
                    "foreign key(%s) references %s(%s))",       // clothes id foreign key to wardobe table, _id
            TAG_TABLE_NAME, ID_COLUMN, CLOTHES_ID_COLUMN, CLOTHES_ID_COLUMN, TAGS_COLUMN,
            CLOTHES_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN);

    private static final String EXCLUSION_TABLE_CREATE = String.format(
            "CREATE TABLE %s(" +                                    //table name
                    "%s integer primary key autoincrement," +       // _id
                    "%s integer check(%s > 0)," +                   // shirt_id
                    "%s integer check(%s > 0)," +                   // paint id
                    "%s integer check(%s > 0)," +                   // shoes id
                    "foreign key(%s) references %s(%s)," +          // shirt id foreign key to wardrobe table, _id
                    "foreign key(%s) references %s(%s)," +          // pant id foreign key to wardrobe table, _id
                    "foreign key(%s) references %s(%s))",           // pant id foreign key to wardrobe table, _id
            EXCLUSION_TABLE_NAME, ID_COLUMN,
            SHIRT_ID_COLUMN, SHIRT_ID_COLUMN,
            PANT_ID_COLUMN, PANT_ID_COLUMN,
            SHOES_ID_COLUMN, SHOES_ID_COLUMN,
            SHIRT_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN,
            PANT_ID_COLUMN, WARDROBE_TABLE_NAME, ID_COLUMN,
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
    }
}
