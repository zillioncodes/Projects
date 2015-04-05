package edu.buffalo.cse.cse486586.simpledht;

/**
 * Created by zappykid on 3/27/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zappykid on 2/26/15.
 */
public class MyDbHandler extends SQLiteOpenHelper {
    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "keyValDB.db";
    private static final String TABLE_KEYVALUE = "kvPair";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";
    SQLiteDatabase db;

    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //System.out.println("In the constructor of myHandler");
        //this.onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS kvPair;");
            String CREATE_KEYVALUE_TABLE = "CREATE TABLE " +
                    TABLE_KEYVALUE + " ( "
                    + COLUMN_KEY + " STRING PRIMARY KEY," + COLUMN_VALUE
                    + " STRING, UNIQUE(key) ON CONFLICT REPLACE )"; //UNIQUE(key) ON CONFLICT REPLACE
            //System.out.println("Before executing the create table sql script");
            db.execSQL(CREATE_KEYVALUE_TABLE);
            //System.out.println("After executing the create table sql script");
        }catch(Exception e){
            System.out.println("table not created");
        }
    }

    /*@Override
    public void onCreate() {

    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addValue(){

    }
}
