package com.vinners.cube_vishwakarma.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList;

import java.util.List;

public class DatabaseHandlers extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyDatabase";

    // Database table name
    private static final String TABLE_LIST = "MyListItem";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ListItem = "listitem";

    public DatabaseHandlers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST + "(" + KEY_ID
                + " INTEGER," + KEY_ListItem + " TEXT" + ")";

        db.execSQL(CREATE_LIST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

        // Create tables again
        onCreate(db);
    }



    public void addListItem(List<OutletsList> listItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {

            Log.e("vlaue inserting==", "" + listItem.get(i));
            values.put(KEY_ListItem, String.valueOf(listItem.get(i)));
            db.insert(TABLE_LIST, null, values);

        }

        db.close(); // Closing database connection
    }

    Cursor getListItem(Integer roId, Integer said) {
        String selectQuery = "SELECT  * FROM " + TABLE_LIST + "WHERE";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

}
