package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zappykid on 2/15/15.
 */
public class MyDbHandler extends SQLiteOpenHelper {

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "keyValDB.db";
    private static final String TABLE_KEYVALUE = "kvPair";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";

    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KEYVALUE_TABLE = "CREATE TABLE " +
                TABLE_KEYVALUE + " ( "
                + COLUMN_KEY + "STRING PRIMARY KEY," + COLUMN_VALUE
                        + " STRING, UNIQUE(key) ON CONFLICT REPLACE "+")";
        try {
            db.execSQL(CREATE_KEYVALUE_TABLE);
        }catch(Exception e){
                System.out.println("database not created");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addValue(){

    }
}


