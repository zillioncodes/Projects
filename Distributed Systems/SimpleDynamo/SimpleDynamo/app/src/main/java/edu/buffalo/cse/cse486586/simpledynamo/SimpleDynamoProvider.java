package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SimpleDynamoProvider extends ContentProvider {
    String myPort; static final int SERVER_PORT = 10000;static nodeHub myNode = null;private Uri mUri;
    //static boolean lock = true;
    private MyDbHandler myHandler; private SQLiteDatabase db;private Cursor myQueryCursor;
    static final String TAG = SimpleDynamoProvider.class.getSimpleName(); public static int starAvdCount = 4;
    private static final String dbname = "mydb";private static boolean updateFlag = false;
    public HashMap<String, String> portHash = new HashMap<String, String>();
    HashMap<String, String> map = new HashMap<String, String>();
    private String myQueryResult = null;public boolean flag = false;static Object lock = new Object();
    int starReply=0;public static String queryReturnPort; long millis;
    public static HashMap<String,String> kvResult = new HashMap<String,String>();
    Queue<String> writeQueue = new LinkedList<String>();Queue<String> readQueue = new LinkedList<String>();
    public static HashMap<String,ArrayList<String>> onIFailStore =  new HashMap<String,ArrayList<String>>();
    public static HashMap<String,ArrayList<String>> onRFailStore =  new HashMap<String,ArrayList<String>>();
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mUri = buildUri("content", "/edu.buffalo.cse.cse486586.simpledynamo.provider");
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        System.out.println("the port number is the following:" + myPort);
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
            System.out.println("serverTask has been called with the server socket set to 10000");
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
        }
        millis = System.currentTimeMillis();
        myNode = new nodeHub(myPort);

        try {
            //XThread.sleep(1500);
            myNode.initNodes();
            //check();
            //myNode.checkAVDNum();
            //while(myNode.checkFlag){
              //  System.out.println("Waiting while check!");
              //  Thread.sleep(2);
            //}
            //if(myNode.count==4){
                myNode.updateInsFail();
                myNode.updateRepFail();


                Thread.sleep(1400);
//                updateFlag = true;
//                while (updateFlag) {
//                    System.out.println("Waiting while update!!");
//                    Thread.sleep(1);
//                }
//                System.out.println("Updte flag released, value:"+updateFlag);

            //else{
            myHandler = new MyDbHandler(
                    getContext(),
                    dbname,
                    null,
                    1
            );
            myHandler.getWritableDatabase();
            myHandler.onCreate(db);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("have initiated the nodeHub");

        return false;
    }

    private void check() {
        int i = 0;
        do{
            i = 0;
        for(String s:myNode.allport) {
            try {
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(s));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msgToSend = "CHECK#" + myPort;
                pw.println(msgToSend);
                pw.flush();
                String inSocket = in.readLine();
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }while(i!=5);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        String star = "\"*\"";
        String atRate = "@";
        if (selection.equalsIgnoreCase(star)) {
            deleteAll();
            star = star + "#" + myPort;
            System.out.println("Sending to other ports to delete");
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", star);
        } else if (selection.equalsIgnoreCase(atRate)) {
            System.out.println("delete local values");
            deleteAll();
        } else {
            int in = deleteQuery(selection);
            if (in == 1) {
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", selection);
            }
        }

        return 0;
    }

    private int deleteQuery(String selection) {
        Cursor check = searchQuery(selection);
        String key = null;
        String query;
        while (check.moveToNext()) {
            key = check.getString(0);
            String value = check.getString(1);
            ///System.out.println("The value of key in cursor is " + key);
            ///System.out.println("The value of value in cursor is " + value);
        }
        if (key != null) {
            query = "DELETE FROM kvPair where key='" + selection + "'";
            db = myHandler.getWritableDatabase();
            db.execSQL(query);
            return 0;
        } else {
            return 1;
        }
    }

    private void deleteSubAll(String selection) {
        ///System.out.println("Inside sub delete");
        String[] parts = selection.split("#");
        String star = "\"*\"";
        if (parts[0].equalsIgnoreCase(star)) {
            /// System.out.println("Have reached here");
            deleteAll();
            if (!(parts[1].equals(myNode.SucPre.get(0)))) {
                star = star + "#" + parts[1];
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", star);
            }
        } else {
            int in = deleteQuery(selection);
            if (in == 1) {
                if (!(parts[1].equals((myNode.SucPre.get(0))))) {
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", selection);
                }
            }
        }
    }

    private void deleteAll() {
        String query;
        System.out.println("Trying to delete all values in AVD");
        query = "DELETE FROM kvPair";
        System.out.println("In Query Delete");

        try {
//            db = myHandler.getWritableDatabase();
//            db.rawQuery(query,null);
            db = myHandler.getWritableDatabase();
            db.execSQL(query);
            //return cursor;
        } catch (Exception e) {
            Log.e(TAG, "database delete failed");
            // return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        Set<String> keys = values.keySet();
        String key = null;
        String keytemp = "";
        //String[] keyVal = new String[4]; //String keyHash;
        StringBuffer sb = new StringBuffer();
        String keyhashval = "";
        //int flag = 0;
        Iterator iterator = keys.iterator();
        int i = 2;
        int j = 0;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (key.equalsIgnoreCase("key")) {
                keytemp = (String) values.get(key);
                sb.append(keytemp); //sb = key
            } else if (key.equalsIgnoreCase(("value"))) {
                sb.append("#").append((String) values.get(key)); // sb = key#value
            }
        }
        try {
            keyhashval = genHash(keytemp);
            //sb.append("#").append(keyhashval); //sb = key#value#keyHAshVal
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String getPortToInsert = "";
        System.out.println("Sorted nodes:"+myNode.nodes.size());
        while (j < myNode.nodes.size()) {
            int compr = keyhashval.compareTo(myNode.nodes.get(j));
            System.out.println("Key:"+keytemp+" Comparing keyhash "+keyhashval+" with node hash:"+myNode.nodes.get(j));
            System.out.println("Compare value:"+compr);
            if (compr <= 0) {
                getPortToInsert = myNode.nodeHashMap.get(myNode.nodes.get(j));

                //sb.append("#").append(getPortToInsert);
                //flag++;
                break;
            }
            j++;
        }
        System.out.println("The value of j:"+j);
        if (j == myNode.nodes.size()) {
            getPortToInsert = myNode.nodeHashMap.get(myNode.nodes.get(0));
            j=0;
        }
        if (getPortToInsert.equalsIgnoreCase(myPort)) {
            System.out.println("In myAVD:"+myPort+" with final insert:"+sb.toString());
            finalInsert(sb.toString());   // new sb = key#value
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "REPLICATE", sb.toString());
        } else {
            sb.append("#").append(getPortToInsert);// new sb = key#value#getPortToInsert
            System.out.println("Port to insert is:"+myNode.nodes.get(j));
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "insert", sb.toString());
        }
        //lookUpPort(keyVal);
        return null;
    }

    private synchronized void finalInsert(String s) {
        ContentValues newCV = new ContentValues();
        System.out.println(s);
        String[] tmparr = s.split("#");
        newCV.put("key", tmparr[0]);
        newCV.put("value", tmparr[1]);
        System.out.println("The values in CV is Key:"+newCV.get("key")+"and Value is:"+newCV.get("value"));
        try {
            db = myHandler.getWritableDatabase();
            //Cursor cursor = db.rawQuery(query, null);
            db.insert("kvPair", null, newCV);
            //System.out.println("inserted values to dbase:" + values.get(key) + ":key is" + key);
            Log.i("insert", newCV.toString());
        } catch (Exception e) {
            Log.e("Insert Error while inserting", (String) newCV.get(tmparr[1]));
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        String selectionKeyHash = null;
        int j = 0;
        String msgToQuery = "";
        String atRate = "\"@\"";
        String getPortToQuery = "";
        String star = "\"*\"";
        try {
            ///System.out.println("The value queried for selection is : " + selection);
            selectionKeyHash = genHash(selection);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (selection.equals(atRate)) {
            Cursor csr = getAllQuery();
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            while (csr.moveToNext()) {
                String key = csr.getString(0);
                String value = csr.getString(1);
                System.out.println("The value of key in cursor is " + key);
                System.out.println("The value of value in cursor is " + value);
                mc.addRow((new String[]{key, value}));
            }
            System.out.println("the matrixcursor atRate:" + mc);
            return mc;
        } else if (selection.equals(star)) {
            Cursor csr = getAllQuery();
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            while (csr.moveToNext()) {
                String key = csr.getString(0);
                String value = csr.getString(1);
                System.out.println("The value of key in cursor is " + key);
                System.out.println("The value of value in cursor is " + value);
                map.put(key, value);
            }
            if ((myNode.myPortHash.equalsIgnoreCase(myNode.SucPre.get(0)))) {
                for (Map.Entry<String, String> etr : map.entrySet()) {
                    mc.addRow(new String[]{etr.getKey(), etr.getValue()});
                }
                return mc;
            }
            setMyQueryMap(map);
            msgToQuery = star + "#" + myPort;
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLUSTAR", msgToQuery);
            //synchronized(lock){
                try {
                //Thread.sleep(1000);
                flag=true;
                while(flag) {
                    Thread.sleep(2);
                  //  lock.wait();
                }
                  //  lock.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Map.Entry<String, String> etr : map.entrySet()) {
                mc.addRow(new String[]{etr.getKey(), etr.getValue()});
            }
            return mc;
        }//}
        while (j < myNode.nodes.size()) {
            int compr = selectionKeyHash.compareTo(myNode.nodes.get(j));
            if (compr < 0) {
                getPortToQuery = myNode.nodeHashMap.get(myNode.nodes.get(j));
                break;
            }
            j++;
        }
        if (j == myNode.nodes.size()) {
            getPortToQuery = myNode.nodeHashMap.get(myNode.nodes.get(0));
        }
        if (getPortToQuery.equalsIgnoreCase(myPort)) {
            //Cursor csr = searchQuery(selection);
//            while (csr.moveToNext()) {
//                String key1 = csr.getString(0);
//                String value1 = csr.getString(1);
//                System.out.println("The value of key in cursor is " + key1);
//                System.out.println("The value of value in cursor is " + value1);
//            }
            getPortToQuery = myNode.nodeHashMap.get(myNode.SucPre.get(1));
            msgToQuery = selection + "#" + getPortToQuery + "#"+myPort;
            System.out.println("Sending message to query:" + msgToQuery);
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "READ", msgToQuery);
            //return csr;
        } else {
            msgToQuery = selection + "#" + getPortToQuery;
            System.out.println("Sending message to query:" + msgToQuery);
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QUERY", msgToQuery);}
            try {
                //sThread.sleep(1000);
                //flag=true;
                while(!(kvResult.containsKey(selection))) {
                  Thread.sleep(2);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            System.out.println("In my port"+myPort+" got key "+selection+" result "+ kvResult.get(selection));
            mc.addRow((new String[]{selection, kvResult.get(selection)}));
            kvResult.remove(selection);
            return mc;
        //}
        //return null;
    }

    private synchronized Cursor searchQuery(String selection) {
        String query;
        query = "Select * FROM kvPair WHERE key =  \"" + selection + "\"";
        ///System.out.println("In Query"+selection);
        String key = null;
        try {
            db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Log.i("cursor", cursor.toString());
            return cursor;

        } catch (Exception e) {
            Log.e(TAG, "database read");
            return null;
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    public Cursor getAllQuery() {
        String query;
        query = "Select * FROM kvPair";
        ///System.out.println("In Query STAR");

        try {
            db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Log.i("cursor", cursor.toString());
            return cursor;
        } catch (Exception e) {
            Log.e(TAG, "database read");
            return null;
        }
    }

    public void setMyQueryMap(HashMap<String, String> myQueryMap) {
        this.map = myQueryMap;
    }

    public String getMyQueryResult() {
        return myQueryResult;
    }

    public void setMyQueryResult(String myQueryResult) {
        this.myQueryResult = myQueryResult;
    }
    public void setQueryReturnPort(String qRP) {
        this.queryReturnPort = qRP;
    }

    public String getQueryReturnPort() {
        return queryReturnPort;
    }



    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            String inSocket;
            Socket socket;
            while (true) {
                try {
                    System.out.println("Inside ServerTask");
                    socket = serverSocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    inSocket = br.readLine();
                    System.out.println("String Recieved:" + inSocket);
                    String[] msgParts = inSocket.split("#");
                    pw.println("ping");
                    pw.flush();
                    pw.close();
                    if (msgParts[0].equalsIgnoreCase(("IF"))) {
                        System.out.println("After fail, insert:" + inSocket + " the string in port:" + myPort);
                        if (onIFailStore.containsKey(msgParts[4])) {
                            ArrayList<String> temp = onIFailStore.get(msgParts[4]);
                            temp.add(msgParts[1] + "#" + msgParts[2]);
                            onIFailStore.put(msgParts[4], temp);
                            System.out.println("The content of IFailStore:" + onIFailStore + " inserted on port:" + msgParts[4]);
                        } else {
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(msgParts[1] + "#" + msgParts[2]);
                            onIFailStore.put(msgParts[4], temp);
                            System.out.println("The content of IFailStore:" + onIFailStore + " inserted on port:" + msgParts[4]);
                        }
                        String insert = msgParts[1] + "#" + msgParts[2];
                        System.out.println("After fail,final insert msg in port:" + msgParts[4] + " message" + insert);
                        writeQueue.add(insert);
                        gatePass();
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "RONE", insert);
                    } else if (msgParts[0].equalsIgnoreCase("UPDTINS")) {
                        System.out.println("Got insert update request from port:" + msgParts[1] + " map:" + onIFailStore);
                        if (onIFailStore.containsKey(msgParts[1])) {
                            ArrayList<String> temp = onIFailStore.get(msgParts[1]);
                            System.out.println("To Update data:" + onIFailStore.get(msgParts[1]));
                            for (String s : onIFailStore.get(msgParts[1])) {
                                String[] tmparr = s.split("#");
                                String keyHashVal = genHash(tmparr[0]);
                                String msgToSend = tmparr[0] + "#" + tmparr[1] + "#" + msgParts[1];
                                System.out.println("On update,Message sent to insert:" + msgToSend);
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "insert", msgToSend);
                            }
                            onIFailStore.remove(msgParts[1]);
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UC", msgParts[1]);
                        } else {
                            System.out.println("*********Nothing to Update******");
                            //new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UC", msgParts[1]);
                        }
                    } else if (msgParts[0].equalsIgnoreCase("UC")) {
                        System.out.println("Recieved Update Completion");
                        updateFlag = false;
                    } else if (msgParts[0].equalsIgnoreCase("UPDTREP")) {
                        System.out.println("Got replicate update request from port:" + msgParts[1] + " map:" + onRFailStore);
                        getOnRFSValues(msgParts[1]);
                    }else if(msgParts[0].equalsIgnoreCase(("UPDTREPONE"))){
                        System.out.println("Got replicate from master update request from port:" + msgParts[1] + " map:" + onRFailStore);
                        if((onRFailStore.containsKey(msgParts[1]))) {
                            getOnRFSValues(msgParts[1]);
                        }else{
                            System.out.println("*********Nothing to Update******");
                        }
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UC", msgParts[1]);
                    }else if(msgParts[0].equalsIgnoreCase("RF")){
                        System.out.println("After fail, replica:" + inSocket + " the string in port:" + myPort);
                        if(onRFailStore.containsKey(msgParts[3])){
                            ArrayList<String> temp = onRFailStore.get(msgParts[3]);
                            //onRFailStore.get(msgParts[3]).add(msgParts[1]+"#"+msgParts[2]);
                            temp.add(msgParts[1]+"#"+msgParts[2]); System.out.println("Just added to the rfailmap:"+msgParts[1]+"#"+msgParts[2]);
                            System.out.println("fail replica before entering the map:"+onRFailStore);
                            onRFailStore.put(msgParts[3],temp);
                            System.out.println("fail replica after entering the map:"+onRFailStore);
                        }else {
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(msgParts[1]+"#"+msgParts[2]);
                            System.out.println("fail replica before entering the map:"+onRFailStore);
                            onRFailStore.put(msgParts[3],temp);
                            System.out.println("fail replica after entering the map:"+onRFailStore);
                        }
                    }
                    else {
                        this.publishProgress(inSocket);
                    }
                } catch (ConnectException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }

        private void getOnRFSValues(String msgPart) {
            System.out.println("Value of msg part:"+msgPart);
            if(onRFailStore.containsKey(msgPart)) {
                ArrayList<String> temp = onRFailStore.get(msgPart);
                System.out.println("To Update data:" + temp);
                for (String s : temp) {
                    String[] tmparr = s.split("#");
                    //String keyHashVal = genHash(tmparr[0]);
                    String msgToSend = tmparr[0] + "#" + tmparr[1]+"#"+msgPart;
                    System.out.println("On update,Message sent to replicate:" + msgToSend);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "RU", msgToSend);
                }
                onRFailStore.remove(msgPart);
            }
        }

//        private synchronized void IFgatePass() {
//            System.out.println("Inside gatePass");
//            while (!(writeQueue.isEmpty())) {
//                System.out.println("Emptying write queue");
//                String msgPart = writeQueue.remove();
//                finalInsert(msgPart);
//                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "REPLICATE", msgPart);
//            }
//        }

        private synchronized void gatePass() {
            System.out.println("Inside gatePass");
            while (!(writeQueue.isEmpty())) {
                System.out.println("Emptying write queue");
                String msgPart = writeQueue.remove();
                finalInsert(msgPart);
                //new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "REPLICATE", msgPart);
            }
            while (!(readQueue.isEmpty())) {
                String msgPart = readQueue.remove();
                Cursor csr = searchQuery(msgPart);
                String key, value = "";
                while (csr.moveToNext()) {
                    key = csr.getString(0);
                    value = csr.getString(1);
                    System.out.println("The value of key in cursor is " + key);
                    System.out.println("The value of value in cursor is " + value);
                    //resOfQuery = resOfQuery+key+"@"+value+"$";
                    //mc.addRow((new String[] {key,value}));
                }
                value = value + "#" + getQueryReturnPort() + "#" + msgPart;
                System.out.println("The value of query result:" + value);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "RQ", value);
                setQueryReturnPort(null);
            }
        }
        protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String inSocket = strings[0];
            System.out.println("String Recieved in onProgressUpdate:"+inSocket);
            String[] msgParts = inSocket.split("#");

            if (msgParts[0].equalsIgnoreCase("insert")) {
                System.out.println("going to final insert:" + inSocket + " the string in port:" + msgParts[3]);
                String insert = msgParts[1] +"#"+ msgParts[2];
                System.out.println("final insert msg in port:"+msgParts[3]+" message"+insert);
                writeQueue.add(insert);
                gatePass();
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "REPLICATE", insert);
                //changes
            }else if (msgParts[0].equalsIgnoreCase("QLUSTAR")) {
                System.out.println("Inside QLUSTAR:" + inSocket);
                String resOfQuery = "";
                Cursor csr = getAllQuery();
                while (csr.moveToNext()) {
                    String key = csr.getString(0);
                    String value = csr.getString(1);
                    System.out.println("The value of key in cursor is " + key);
                    System.out.println("The value of value in cursor is " + value);
                    resOfQuery = resOfQuery + key + "@" + value + "$";
                    //mc.addRow((new String[] {key,value}));
                }
                resOfQuery = resOfQuery + "#" + msgParts[2];
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "RESSTAR", resOfQuery);
            }else if (msgParts[0].equalsIgnoreCase("RESSTAR")) {
                String[] kvp = msgParts[1].split("\\$");
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{"key", "value"});
                System.out.println("In SERVER WQPALL" + map);
                for (String s : kvp) {
                    String[] kvn = s.split("@");
                    System.out.println("the keyvalue:" + s);
                    String key = kvn[0];
                    String value = kvn[1];
                    map.put(key, value);
                }
                starReply++;
                if(starReply==starAvdCount){
                    flag = false;
                }
            }else if (msgParts[0].equalsIgnoreCase("READ")) {
                setQueryReturnPort(msgParts[2]);
                readQueue.add(msgParts[1]);
                gatePass();
                //changed
                //setMyQueryResult(value);
            }else if(msgParts[0].equalsIgnoreCase("del")){
                ///System.out.println("Delete comes here"+inSocket);
                String[] parts = inSocket.trim().split("#");
                ///System.out.println("Now see this:"+parts[1]+parts[2]);
                deleteSubAll(parts[1] + "#" + parts[2]);
            }else if(msgParts[0].equalsIgnoreCase("REPLICATE")){
                String insert = msgParts[1] +"#"+ msgParts[2];
                System.out.println("Recieved String to insert as replica"+insert);
                finalInsert(insert);
            }else if(msgParts[0].equalsIgnoreCase("RQ")){
                System.out.println("Setting value of myQueryResult"+msgParts[1]);
                kvResult.put(msgParts[2], msgParts[1]);
                System.out.println("Have just put "+msgParts[2]+" and value:"+msgParts[1]+"  a query result");
                //flag = false;
            }else if(msgParts[0].equalsIgnoreCase("QUERY")){
                System.out.println("Inside New Query"+inSocket);//Query#key#portToQuery#fromqueriedport
                String msgToRead = msgParts[1]+"#"+myNode.nodeHashMap.get(myNode.SucPre.get(1))+"#"+msgParts[3];
                System.out.println("Sending message to third node"+msgToRead);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "READ",msgToRead);
            }else if(msgParts[0].equalsIgnoreCase("QONE")){
                System.out.println("Inside Qone"+inSocket);//Query#key#toQueryPort#fromQueryPort
                String msgToRead = msgParts[1]+"#"+myNode.nodeHashMap.get(myNode.SucPre.get(0))+"#"+msgParts[3];
                System.out.println("Sending read msg to next node"+msgToRead);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "READ",msgToRead);
            }


        }

    }

    private class ClientTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... msgs) {
            String op = msgs[0];
            System.out.println("Going to perform this operation:" + op);
            try {
                if (op.equalsIgnoreCase("insert")) {
                    System.out.println("In insert with String:" + msgs[1]);
                    String[] inarr = msgs[1].split("#"); //key#value#getPortToInsert
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[2]));
                    //socket.setSoTimeout(50);
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "insert#" + inarr[0] + "#" + inarr[1] + "#" + inarr[2]; //insert#key#value#tosendport
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    System.out.println("Sending msg to Write:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                    if(inSocket==null) {
                        String msgToSendOnFail = "IF#"+inarr[0]+"#"+inarr[1];
                        //IF#key#value
                        System.out.println("Msg Send to insert on fail");
                        myNode.handleInsert(msgToSendOnFail, inarr[2]);
                    }
                    socket.close();
                } else if (op.equalsIgnoreCase("QLUSTAR")) {
                    for (String s : myNode.allport) {
                        if (!(s.equalsIgnoreCase(myPort))) {
                            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(s));
                            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String msgToSend = "QLUSTAR#" + "STAR#" + myPort;
                            pw.println(msgToSend);
                            pw.flush();
                            String inSocket = in.readLine();
                            socket.close();
                            System.out.println("Sending msg to Write:" + msgToSend);
                            System.out.println("##################################################Recieved ping:"+inSocket);
                            if(inSocket==null){
                                starAvdCount--;
                            }
                        }
                    }
                } else if (op.equalsIgnoreCase("QUERY")) {
                    String[] inarr = msgs[1].split("#");
                    System.out.println("In Case Query:" + msgs[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = op+"#"+inarr[0] + "#" + inarr[1] + "#" + myPort;
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    //socket.close();
                    System.out.println("Sending msg to Write:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                    if(inSocket==null) {
                        String msgToQueryOnFail = inarr[0]+"#"+myPort;
                        //IF#key#value#keyHash
                        System.out.println("Msg Send to query on fail");
                        myNode.handleQuery(msgToQueryOnFail, inarr[1]);
                    }
                    socket.close();

                } else if (op.equalsIgnoreCase("RQ")) {
                    String[] inarr = msgs[1].split("#");
                    System.out.println("Result of Query:" + msgs[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "RQ#" + inarr[0]+"#"+inarr[2];
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    System.out.println("Result of Query msg to Write:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                    socket.close();

                }else if (op.equalsIgnoreCase("RESSTAR")) {
                    String[] inarr = msgs[1].split("#");
                    System.out.println("In Case RESSTAR:" + msgs[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "RESSTAR#"+inarr[0] + "#" + inarr[1];
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    //socket.close();
                    System.out.println("Sending msg to Write:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                    socket.close();
                } else if (op.equalsIgnoreCase("del")) {
                    String msgToSend = "del#" + msgs[1]+"#"+myPort;
                    ///System.out.println("Sending delete to other port"+getSuccessorPort());
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(myNode.nodeHashMap.get(myNode.SucPre.get(0))));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    socket.close();
                    System.out.println("Sending msg to delete:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);

                } else if(op.equalsIgnoreCase("REPLICATE")){
                    System.out.println("Sending Value to replicate:"+msgs[1]); int repnodes=0;
                    while(repnodes<2){
                        String msgToSend = "REPLICATE#"+msgs[1]; // replicate#key#value
                        System.out.println(msgToSend+" in port"+myNode.nodeHashMap.get(myNode.SucPre.get(repnodes)));
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes))));
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        pw.println(msgToSend);
                        pw.flush();
                        repnodes++;
                        String inSocket = in.readLine();
                        //socket.close();
                        System.out.println("Wrote msg to replicate:" + msgToSend);
                        System.out.println("##################################################Recieved ping:"+inSocket);
                        if(inSocket==null && repnodes==1){
                            myNode.handleRepFail(msgs[1],myNode.SucPre.get(repnodes-1));
                        }else if(inSocket==null && repnodes==2){
                            if(onRFailStore.containsKey(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes-1)))){
                                //ArrayList<String> temp = onRFailStore.get(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes-1)));
                                System.out.println("Before putting into onRFailStore:"+onRFailStore);
                                onRFailStore.get(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes-1))).add(msgs[1]);
                                System.out.println("After putting into onRFailStore:"+onRFailStore);
                                //onRFailStore.put(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes-1)),temp);
                            }else {
                                ArrayList<String> temp = new ArrayList<String>();
                                temp.add(msgs[1]);
                                System.out.println("Before putting into onRFailStore:"+onRFailStore);
                                onRFailStore.put(myNode.nodeHashMap.get(myNode.SucPre.get(repnodes-1)),temp);
                                System.out.println("After putting into onRFailStore:"+onRFailStore);
                            }
                        }
                        socket.close();
                    }
                }else if (op.equalsIgnoreCase("READ")) {
                    String[] inarr = msgs[1].split("#"); // key#toreadport#fromqueriedport
                    System.out.println("In Case Read:" + msgs[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = op + "#" + inarr[0] + "#"+inarr[2];// "READ#"+key+ "#" + myPort;
                    pw.println(msgToSend);
                    pw.flush();
                    String inSocket = in.readLine();
                    //socket.close();
                    System.out.println("Sending msg to Write:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                    if(inSocket==null){
                            myNode.handleReadFail(inarr[0],inarr[1],inarr[2]);
                            System.out.println("To read from the back up port next to:"+inarr[1]);
                    }
                    socket.close();
                }else if(op.equalsIgnoreCase("UC")){
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(msgs[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "UC#"+msgs[1];// + "#" + myPort;
                    pw.println(msgToSend);
                    pw.flush();
                    System.out.println("Completed Writing updates");
                    socket.close();
                }else if(op.equalsIgnoreCase("RU")){
                    String[] inarr = msgs[1].split("#");
                    String msgToSend = "REPLICATE#"+inarr[0]+"#"+inarr[1];
                    System.out.println(msgToSend+" in port:"+inarr[2]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(inarr[2]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pw.println(msgToSend);
                    pw.flush();
                } else if(op.equalsIgnoreCase("RONE")){
                    System.out.println("In RONE:"+msgs[1]);
                    String msgToSend = "REPLICATE#"+msgs[1]; // replicate#key#value
                    System.out.println(msgToSend+" in port"+myNode.nodeHashMap.get(myNode.SucPre.get(0)));
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(myNode.nodeHashMap.get(myNode.SucPre.get(0))));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pw.println(msgToSend);
                    pw.flush();
                    //repnodes++;
                    String inSocket = in.readLine();
                    //socket.close();
                    System.out.println("Wrote msg to replicate one:" + msgToSend);
                    System.out.println("##################################################Recieved ping:"+inSocket);
                }

            }catch (ConnectException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (SocketTimeoutException e){
                System.out.println("This is a timeout exception!");
                e.printStackTrace();
            }
            catch (Exception e) {
                System.out.println("Exception occured");
                e.printStackTrace();
            }
            return null;


        }

    }
}
