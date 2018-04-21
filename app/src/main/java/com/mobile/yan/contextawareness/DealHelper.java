package com.mobile.yan.contextawareness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DealHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "deal_db";
    public static final int DB_VERSION = 1;

    //

    //public static final String CRERATE_QUERY = "create table" +
    //public static final String DROP_QUERY =



    public DealHelper(Context context){

        super(context, DB_NAME, null, DB_VERSION);
        Log.d("Database operations", "Database created...");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL( CRERATE_QUERY );
        Log.d("Database operations", "Table created...");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //db.execSQL( DROP_QUERY );
        Log.d("Database operations", "Database updated...");

    }

    public void putInformation(String name, int calories, double fat, SQLiteDatabase db){
       // contentValues contentValues = new ContentValues(  );





    }

    public Cursor getInformation(SQLiteDatabase db){

       // String [] projection = sdf;
        //Cursor cursor = db.query(  );
        //return cursor;
        return null;

    }

}
