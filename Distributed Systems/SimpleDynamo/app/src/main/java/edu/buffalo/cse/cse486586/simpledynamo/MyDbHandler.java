package edu.buffalo.cse.cse486586.simpledynamo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zappykid on 4/14/15.
 * Maintains the database in every AVD emulator
 */
 public class MyDbHandler extends SQLiteOpenHelper {
    
        /*Global variable declaration*/
        private static final String TABLE_KEYVALUE = "kvPair";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_VALUE = "value";
        public SQLiteDatabase db;

        public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        /*Creates the databse tables by running the SQL scripts on creation of the emulator */
        public void onCreate(SQLiteDatabase db) {
            try{
                db = getWritableDatabase();    // Retrieves the database to write.
                String CREATE_KEYVALUE_TABLE = "CREATE TABLE " +
                        TABLE_KEYVALUE + " ( "
                        + COLUMN_KEY + " STRING PRIMARY KEY," + COLUMN_VALUE
                        + " STRING, UNIQUE(key) ON CONFLICT REPLACE )"; 
                //System.out.println("Before executing the create table sql script");
                db.execSQL(CREATE_KEYVALUE_TABLE);       // To run the sql script
            }catch(Exception e){
                //System.out.println("table not created"); // Displays exception on error
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
 }
