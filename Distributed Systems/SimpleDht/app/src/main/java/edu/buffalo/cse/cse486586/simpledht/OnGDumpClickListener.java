package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zappykid on 4/2/15.
 */
public class OnGDumpClickListener implements View.OnClickListener {

    private static final String TAG = OnTestClickListener.class.getName();
    private static final int TEST_CNT = 10;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";

    private final TextView mTextView;
    private final ContentResolver mContentResolver;
    private final Uri mUri;
    private final ContentValues[] mContentValues;

    public OnGDumpClickListener(TextView _tv, ContentResolver _cr) {
        mTextView = _tv;
        mContentResolver = _cr;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
        mContentValues = initTestValues();
    }

    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    private ContentValues[] initTestValues() {
        ContentValues[] cv = new ContentValues[TEST_CNT];
        for (int i = 0; i < TEST_CNT; i++) {
            cv[i] = new ContentValues();
            cv[i].put(KEY_FIELD, "key" + Integer.toString(i));
            cv[i].put(VALUE_FIELD, "val" + Integer.toString(i));
        }

        return cv;
    }

    @Override
    public void onClick(View v) {
        new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Task extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
//            if (testInsert()) {
//                publishProgress("Insert success\n");
//            } else {
//                publishProgress("Insert fail\n");
//                return null;
//            }
            System.out.println("About to test GDUMP");
            if (testQuery()) {
                publishProgress("Query success\n");
            } else {
                publishProgress("Query fail\n");
            }

            return null;
        }

        protected void onProgressUpdate(String...strings) {
            mTextView.append(strings[0]);

            return;
        }

        private boolean testInsert() {
            try {
                for (int i = 0; i < TEST_CNT; i++) {
                    mContentResolver.insert(mUri, mContentValues[i]);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return false;
            }

            return true;
        }

        private boolean testQuery() {
            try {
                  String query = "\"*\"";
//                System.out.println("calling testQuery");
//                    Cursor resultCursor = mContentResolver.query(mUri, null,
//                            query, null, null);
//                    if (resultCursor == null) {
//                        Log.e(TAG, "Result null");
//                        throw new Exception();
//                    }
//
//                    int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
//                    int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
//                    if (keyIndex == -1 || valueIndex == -1) {
//                        Log.e(TAG, "Wrong columns");
//                        resultCursor.close();
//                        throw new Exception();
//                    }
//
//                    while(resultCursor.moveToNext()) {
//                    String key = resultCursor.getString(0);
//                    String value = resultCursor.getString(1);
//                    mTextView.append(key);
//                    mTextView.append(value);
//                    System.out.println("The value of key in cursor is "+key);
//                    System.out.println("The value of value in cursor is "+value);
//                    }
                    mContentResolver.delete(mUri,query,null);
                    mTextView.append("delete called");

                    //resultCursor.close();
            } catch (Exception e) {
                return false;
            }

            return true;
        }
    }
}
