package edu.buffalo.cse.cse486586.groupmessenger2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    private MyDbHandler myHandler;
    private static final String dbname = "mydb";

    private SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */

        Set<String> keys = values.keySet();
        Iterator iterator = keys.iterator();
        String key = (String) iterator.next();
        System.out.println("The content value key is:"+key);
        String query,whereClause;
        query = "Select * FROM kvPair WHERE key =  \"" + key + "\"";
//        whereClause = "UPDATE Cars SET value=\'"+values.get(key)+"\' WHERE Id="+key;
        try {
            db = myHandler.getWritableDatabase();
            //db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            db.insert("kvPair", null, values);
            //System.out.println("inserted values to dbase:" + values.get(key) + ":key is" + key);


            ///////
            Log.i("insert", values.toString());
        }catch(Exception e){
            Log.e("Insert Error while inserting", (String) values.get(key));
        }

        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
       // System.out.println("On Create has been called");
        //Log.v("On create has been called"," of group messenger provider");
        myHandler = new MyDbHandler(
                getContext(),
                dbname,
                null,
                1
        );
        myHandler.getWritableDatabase();
        myHandler.onCreate(db);
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {


        final String TAG  = GroupMessengerActivity.class.getSimpleName();


        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */

        String query;
        query = "Select * FROM kvPair WHERE key =  \"" + selection + "\"";

        try {
            db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Log.i("cursor",cursor.toString());
            return cursor;
        }catch(Exception e){
            Log.e(TAG, "database read");
            return null;
        }
    }
}
