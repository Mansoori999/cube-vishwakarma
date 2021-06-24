package com.vinners.cube_vishwakarma.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "outlet_dbs";
    private static final String TABLE_NAME = "outlets_tb";
    private static final String TABLE_NAME_MY_APP = "outlets_tb_myApp";
    private static final String KEY_ID = "id";
    private  static final String KEY_ListItem_COLUMN = "outletsitem";
    private static final String TITLE = "title";
    private static final String IMAGE = "img";
    private static final String URL = "url";
    private static final String RO_ID = "roid";
    private static final String SAID_ID = "said";

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+ "(" +KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +KEY_ListItem_COLUMN+ " TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME_MY_APP+ "(" +KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +KEY_ListItem_COLUMN+ " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MY_APP);
        onCreate(sqLiteDatabase);
    }

    public boolean insertOutlet(List<OutletsList> listItem){
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {
            Log.e("vlaue inserting==", "" + listItem.get(i));
            contentValues.put(KEY_ListItem_COLUMN, listItem.get(i).toString());
        result = db.insert(TABLE_NAME, null, contentValues);
        }
        //        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Integer DeleteData(String url){
        SQLiteDatabase sample_db = this.getWritableDatabase();
        return sample_db.delete(TABLE_NAME, URL +" = ?", new String[] {url});
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return cursor;
    }
    public Cursor getDataUnique(String roid,String said){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" where "+KEY_ListItem_COLUMN+"='"+roid+"' AND "+KEY_ListItem_COLUMN+"='"+said+"'", null);
        return cursor;
    }



    public boolean insert_myapps(List<OutletsList> listItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {
            Log.e("vlaue inserting==", "" + listItem.get(i));
            contentValues.put(KEY_ListItem_COLUMN, listItem.get(i).toString());
//            db.insert(TABLE_LIST, null, values);

        }
//        con9-tentValues.put(TITLE, title);
//        contentValues.put(URL, url);
        long result = db.insert(TABLE_NAME_MY_APP, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getDataPersonal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_MY_APP, null);
        return cursor;
    }
}