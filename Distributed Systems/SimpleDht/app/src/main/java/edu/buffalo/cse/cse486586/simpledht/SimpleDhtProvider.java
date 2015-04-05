package edu.buffalo.cse.cse486586.simpledht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SimpleDhtProvider extends ContentProvider {
    //static final String[] myport = new String[6];
    static String myPort, myPortHash;
    static String myQry=null;
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    static final String[] allport = {REMOTE_PORT0, REMOTE_PORT1, REMOTE_PORT2, REMOTE_PORT3, REMOTE_PORT4};
    String successor,predecessor,successorPort, predecessorPort;
    static final int SERVER_PORT = 10000;
    static final String masterPort = "11108";
    static final String toJoin = "J";
    ArrayList<String> nodes = new ArrayList<String>();
    private Uri mUri; static boolean lock = true;
    private MyDbHandler myHandler;
    static final String TAG = SimpleDhtProvider.class.getSimpleName();
    private static final String dbname = "mydb";
    public HashMap<String,String> portHash = new HashMap<String,String>();
    private SQLiteDatabase db;
    private Cursor myQueryCursor;
    HashMap<String,String> map = new HashMap<String,String>();
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        ///System.out.println("Inside delete with:"+selection);
        String star = "\"*\""; String atRate = "@";
        if(selection.equalsIgnoreCase(star)){
            deleteAll();
            star = star+"#"+myPort;
            ///System.out.println("Sending to other ports to delete");
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", star);
        }else if(selection.equalsIgnoreCase(atRate)){
            ///System.out.println("delete local values");
            deleteAll();
        }else{
            int in = deleteQuery(selection);
            if(in==1){
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", selection);
            }
        }

        return 0;
    }

    private int deleteQuery(String selection) {
        Cursor check = searchQuery(selection);
        String key = null;String query;
        while (check.moveToNext()) {
            key = check.getString(0);
            String value = check.getString(1);
            ///System.out.println("The value of key in cursor is " + key);
            ///System.out.println("The value of value in cursor is " + value);
        }
        if(key!=null){
            query = "DELETE FROM kvPair where key='"+selection+"'";
            db = myHandler.getWritableDatabase();
            db.execSQL(query);
            return 0;
        }else{
            return 1;
        }
    }

    private void deleteAll() {
        String query;
        ///System.out.println("Trying to delete all values in AVD");
        query = "DELETE FROM kvPair";
        ///System.out.println("In Query Delete");

        try {
//            db = myHandler.getWritableDatabase();
//            db.rawQuery(query,null);
            db = myHandler.getWritableDatabase();
            db.execSQL(query);
           //return cursor;
        }catch(Exception e) {
            Log.e(TAG, "database delete failed");
           // return null;
        }

    }

    private void deleteSubAll(String selection) {
        ///System.out.println("Inside sub delete");
        String[] parts = selection.split("#");
        String star = "\"*\"";
        if(parts[0].equalsIgnoreCase(star)) {
           /// System.out.println("Have reached here");
            deleteAll();
            if (!(parts[1].equals(getSuccessorPort()))) {
                star = star + "#" + parts[1];
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", star);
            }
        }else{
                int in = deleteQuery(selection);
                if(in==1) {
                    if (!(parts[1].equals(getSuccessorPort()))) {
                       new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Del", selection);
                    }
                }
        }
    }
    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;

    }

    public void setMyQueryResult(String qres){
         this.myQry = qres;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        ///System.out.println("OLD CV:"+values);
        Set<String> keys = values.keySet();String key=null;
        String[] keyVal = new String[4]; //String keyHash;
        Iterator iterator = keys.iterator(); int i=2;
        while(iterator.hasNext()){
            key = (String) iterator.next();
//            System.out.println("The value of Key is:"+key);
//            System.out.println("The value of value is:"+values.get(key));
//            i++;
            ///System.out.println("the value in keyset:"+key);
            if(key.equalsIgnoreCase("key")) {
                keyVal[1] = (String) values.get(key);
            }else if(key.equalsIgnoreCase(("value"))){
                keyVal[2] = (String) values.get(key);
            }
        }
        //String key = (String) iterator.next();
        ///System.out.println("To be inserted key:"+keyVal[1]+" and the values:"+keyVal[2]);
        try {
            keyVal[0] = genHash(keyVal[1]);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyVal[3] = myPortHash;
        ///System.out.println("OLD CV:"+values);
//        values.clear();
//        values.put("key",keyVal[0]);
//        values.put("value",keyVal[2]);
        ///System.out.println("NEW CV:"+values);
        //String query;
        //query = "Select * FROM kvPair WHERE key =  \"" + key + "\"";
        try {
            lookUpPort(keyVal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void lookUpPort(String[] keyVal) throws InterruptedException {
        ///System.out.println("In LookUp");
        String inString = keyVal[0]+"#"+keyVal[1]+"#"+keyVal[2]+"#"+keyVal[3];
        ///System.out.println("LookUp.InString:"+inString);
        ///System.out.println(keyVal[0]+" and the predecessor "+getPredecessor()+" the compare value:");
        int strCompr = keyVal[0].compareTo(getPredecessor());
        int splCompr = keyVal[0].compareTo(getSuccessor());
        ///System.out.println(strCompr);
        int strComprSelf = keyVal[0].compareTo(myPortHash);int predComp = myPortHash.compareTo(getPredecessor());
        ///System.out.println(keyVal[1]+" and the value of myPorthash "+myPortHash+" the compare value:"+strComprSelf);
        ///System.out.println("predcomp:"+predComp);
        if(strCompr>0 && strComprSelf<=0){
            finalInsert(inString);
        }else if(getPredecessor().equalsIgnoreCase(myPortHash)){
            finalInsert(inString);
        }else if(strCompr<0 && strComprSelf<=0 && predComp<0){
            finalInsert(inString);
        }else if(splCompr>0 && strComprSelf>0){
            int comp = myPortHash.compareTo(getSuccessor());
            ///System.out.println("the value of comp is "+comp);
            if(comp>0){
                ///System.out.println("Calling ClientExecutor for final insert:" + inString);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "FINS", inString);
            }else {
                ///System.out.println("Calling ClientExecutor on String:"+inString);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "insert", inString);
            }
        }
        else{
                ///System.out.println("Calling ClientExecutor on String:"+inString);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "insert", inString);
                //Thread.sleep(10);
        }
    }

    private void finalInsert(String inString) {
        ///System.out.println("Inside final Insert with String"+inString);
        ContentValues newCV = new ContentValues();
        String[] tmparr=inString.split("#");
        newCV.put("key",tmparr[1]);
        newCV.put("value",tmparr[2]);
        ///System.out.println("The values in CV is Key:"+newCV.get("key")+"and Value is:"+newCV.get("value"));
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
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mUri = buildUri("content", "/edu.buffalo.cse.cse486586.simpledht.provider");
        ///System.out.println("I am here");
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        ///System.out.println("the port number is the following:"+myPort);
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
            ///System.out.println("serverTask has been called with the server socket set to 10000");
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
        }
        if(myPort.equals(masterPort)){
            try {
                int mp = Integer.parseInt(myPort)/2;
                ///System.out.println("The value of mp:"+mp);
                String myPortGenHash = String.valueOf(mp);
                String temp = genHash(myPortGenHash);
                ///System.out.println("hash the port:"+masterPort+" to value:"+temp);
                portHash.put(temp,masterPort);myPortHash = temp;
                setSuccessor(temp);setPredecessor(temp);
                ///System.out.println("Predecessor Set"+getPredecessor());
                setSuccessorPort(masterPort);setPredecessorPort(masterPort);
                ///System.out.println("Hash for the portnumber:"+masterPort+" generated:"+temp);
                nodes.add(temp);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }else{
            try {
                int mp = Integer.parseInt(myPort)/2;
                ///System.out.println("The value of mp:"+mp);
                String myPortGenHash = String.valueOf(mp);
                String temp = genHash(myPortGenHash); myPortHash = temp;
                setSuccessor(temp);setPredecessor(temp);
                ///System.out.println("Predecessor Set"+getPredecessor());
                setSuccessorPort(myPort);setPredecessorPort(myPort);
                ///System.out.println("Hash for the portnumber:" + myPort + " generated:" + temp);
                ///System.out.println("Send to serial executor by the port:"+myPort);
                nodes.add(temp);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, toJoin, myPort);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        myHandler = new MyDbHandler(
                getContext(),
                dbname,
                null,
                1
        );
        myHandler.getWritableDatabase();
        myHandler.onCreate(db);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // TODO Auto-generated method stub
        String selectionKeyHash = null;
        try {
            ///System.out.println("The value queried for selection is : " + selection);
            selectionKeyHash = genHash(selection);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String atRate = "\"@\"";
        String star = "\"*\"";
        if (selection.equals(atRate)) {
            Cursor csr = getAllQuery();
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            while (csr.moveToNext()) {
                String key = csr.getString(0);
                String value = csr.getString(1);
                ///System.out.println("The value of key in cursor is " + key);
                ///System.out.println("The value of value in cursor is " + value);
                mc.addRow((new String[]{key, value}));
            }
            ///System.out.println("the matrixcursor atRate:" + mc);
            return mc;
        } else if (selection.equals(star)) {
            Cursor csr = getAllQuery();
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            while (csr.moveToNext()) {
                String key = csr.getString(0);
                String value = csr.getString(1);
                ///System.out.println("The value of key in cursor is " + key);
                ///System.out.println("The value of value in cursor is " + value);
                //mc.addRow((new String[]{key, value}));
                map.put(key, value);
            }

            ///System.out.println("the map in port:" + myPort + ":" + map);
            if ((myPortHash.equalsIgnoreCase(getSuccessor()))) {
                for (Map.Entry<String, String> etr : map.entrySet()) {
                    mc.addRow(new String[]{etr.getKey(), etr.getValue()});
                }
                return mc;
            }
            setMyQueryMap(map);
            String msgToQuery = star + "#" + myPort;
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", msgToQuery);
            //coordinate(msgToQuery);
//            //while (getMyQueryCursor() == null) {
            try {
                Thread.sleep(1000);
//                System.out.println("Lock value:"+lock);
//                while(lock==false){
//                    wait();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            //}
            ///System.out.println("Map before returning the MC"+map);
            for (Map.Entry<String, String> etr : map.entrySet()) {
                mc.addRow(new String[]{etr.getKey(), etr.getValue()});
            }
            return mc;
           // return getMyQueryCursor();
        }
        Cursor check = searchQuery(selection);
        String key = null;
        while (check.moveToNext()) {
            key = check.getString(0);
            String value = check.getString(1);
            ///System.out.println("The value of key in cursor is " + key);
            ///System.out.println("The value of value in cursor is " + value);
        }
        // if (qlResult.equalsIgnoreCase(myPortHash)) {
        if (key != null) {
            ///System.out.println("Query Found in this AVD");
            Cursor csr = searchQuery(selection);
            while (csr.moveToNext()) {
                String key1 = csr.getString(0);
                String value1 = csr.getString(1);
                ///System.out.println("The value of key in cursor is " + key1);
                ///System.out.println("The value of value in cursor is " + value1);
            }
            return csr;
        } else {
            ///System.out.println("Calling new client on the query look up from other ports");
            String msgToQuery = selection + "#" + myPort;
            ///System.out.println("msgToQuery:" + msgToQuery);

            //coordinate(msgToQuery);
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", msgToQuery);
            //while (getMyQueryResult() == null) {
            try {
                Thread.sleep(1000);
//                System.out.println("Lock value:"+lock);
//                while(lock==false){
//                    wait();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }//
            ///System.out.println("My query value has been set to:" + getMyQueryResult());
            MatrixCursor mc = new MatrixCursor(new String[]{"key", "value"});
            mc.addRow((new String[]{selection, getMyQueryResult()}));
            return mc;
        }
    }

    private synchronized void coordinate(String msgToQuery) {
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", msgToQuery);
        while(lock==false){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
            notify();
        }
    }
    //return null;

    private Cursor getAllQuery() {
        String query;
        query = "Select * FROM kvPair";
        ///System.out.println("In Query STAR");

        try {
            db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Log.i("cursor",cursor.toString());
            return cursor;
        }catch(Exception e) {
            Log.e(TAG, "database read");
            return null;
        }
    }

    private Cursor searchQuery(String selection) {

        String query;
        query = "Select * FROM kvPair WHERE key =  \"" + selection + "\"";
        ///System.out.println("In Query"+selection);
        String key = null;
        try {
            db = myHandler.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Log.i("cursor",cursor.toString());
            return cursor;

        }catch(Exception e) {
            Log.e(TAG, "database read");
            return null;
        }
    }

    private String queryPortLookUp(String selectionKeyHash) {
        ///System.out.println("In Query Port lookUp");
        int strCompr = selectionKeyHash.compareTo(getPredecessor());
        ///System.out.println(strCompr);
        int strComprSelf = selectionKeyHash.compareTo(myPortHash);
        ///System.out.println(selectionKeyHash+" and the value of myPorthash "+myPortHash+" the compare value:"+strComprSelf);
        if(strCompr>0 && strComprSelf<=0){
            return myPortHash;
        }else {
            return getSuccessor();
        }
    }

    @Override

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
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

    public void setSuccessor(String s) {
        this.successor = s;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getPredecessor() {
        return this.predecessor;
    }

    public String getPredecessorPort() {
        return this.predecessorPort;
    }

    public void setPredecessorPort(String predecessorPort) {
        this.predecessorPort = predecessorPort;
    }

    public void setSuccessorPort(String successorPort) {
        this.successorPort = successorPort;
    }

    public String getSuccessorPort() {
        return successorPort;
    }

    public String getMyQueryResult() {
        return myQry;
    }

    public void setMyQueryMap(HashMap<String,String> myQueryMap) {
        this.map = myQueryMap;
    }

    //public Cursor getMyQueryCursor() {
    //    return myQueryCursor;
    //}

    //public void setMyQueryCursor(Cursor myQueryCursor) {
      //  this.myQueryCursor = myQueryCursor;
    //}

    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            String inSocket;
            Socket socket;
            while (true) {
                try {
                    ///System.out.println("Inside ServerTask");
                    socket = serverSocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    inSocket = br.readLine();
                    ///System.out.println("Recieved msg:"+inSocket);
                    //this.publishProgress(inSocket);
                    String[] msgparts = inSocket.split("#");
                    if(msgparts[0].equalsIgnoreCase("PS")){
                        setPredecessor(msgparts[1]);
                        setPredecessorPort(msgparts[2]);
                        setSuccessor(msgparts[3]);
                        setSuccessorPort(msgparts[4]);
                        ///System.out.println("Predecessor Set of the port:" + myPort + " is the following port:" + getPredecessorPort());
                        ///System.out.println("Successor Set of the port:"+myPort+" is the following port:"+getSuccessorPort());
                    }else if(msgparts[0].equalsIgnoreCase("IN")){
                        this.publishProgress(inSocket);
                    }else if(msgparts[0].equalsIgnoreCase("HMU")){
                        portHash.put(msgparts[2],msgparts[1]);
                    }
                    else if(msgparts[0].equalsIgnoreCase(toJoin)){
                        String[] parts  = inSocket.trim().split("#");
                        ///System.out.println("Received request of join"+parts[1]);
                        int jp = Integer.parseInt(parts[1])/2;String hashport = String.valueOf(jp);
                        ///System.out.println("HashOfthejoiningport:"+hashport);
                        String hashPort = genHash(hashport);
                        ///System.out.println("About to Write the hashvalue of port "+parts[1]+" which is:"+hashPort);
                        portHash.put(hashPort,msgparts[1]);inSocket = parts[0]+"#"+hashPort;
                        ///System.out.println("Passing msg to publish progress"+inSocket);
                        this.publishProgress(inSocket);

                    }else if(msgparts[0].equalsIgnoreCase("QLU")) {
                        ///System.out.println("Msg recieved for lookUp querying my own database"+inSocket);
                        String[] parts = inSocket.trim().split("#");
                        if(parts[1].equalsIgnoreCase("star")){
                            parts[1] = "\"*\"";
                        }
                        queryCheck(parts[1]+"#"+parts[2]);
                    }else if(msgparts[0].equals("FINS")){
                        ///System.out.println("KeyValue recieved to final store");
                        String[] parts = inSocket.trim().split("#");String comb = parts[1]+"#"+parts[2]+"#"+parts[3]+"#"+parts[4];
                        ///System.out.println(comb);
                        finalInsert(comb);
                    }else if(msgparts[0].equalsIgnoreCase("del")){
                        ///System.out.println("Delete comes here"+inSocket);
                        String[] parts = inSocket.trim().split("#");
                        ///System.out.println("Now see this:"+parts[1]+parts[2]);
                        deleteSubAll(parts[1] + "#" + parts[2]);
                    }else if(msgparts[0].equalsIgnoreCase("WQP")){
                        setMyQueryResult(msgparts[2]);
                        //lock=false;
                        ///System.out.println("The key:"+msgparts[1]+" found in port:"+msgparts[3]);
                    }else if(msgparts[0].equalsIgnoreCase("WQPALL")){
                        ///System.out.println("String recieved to write"+msgparts[1]);
                        String[] kvp = msgparts[1].split("\\$");
                        MatrixCursor matrixCursor = new MatrixCursor(new String[] { "key", "value"});
                        ///System.out.println("In SERVER WQPALL"+map);
                        for(String s:kvp){
                            String[] kvn = s.split("@");
                            ///System.out.println("the keyvalue:"+s);
                            String key = kvn[0];
                            String value = kvn[1];
                            map.put(key,value);
                        }
                        //getMyQueryCursor().moveToFirst();
                        ///System.out.println("Current map values:"+map);
//                        while(getMyQueryCursor().moveToNext()) {
//                            String key = getMyQueryCursor().getString(0);
//                            String value = getMyQueryCursor().getString(1);
//                            System.out.println("for * The value of key in cursor is "+key);
//                            System.out.println("for * The value of value in cursor is "+value);
//
//                        }
//                        MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor, getMyQueryCursor()});
//                        System.out.println("Just merged the cursor");
//                        mergeCursor.moveToFirst();
//                        System.out.println("Merge Cursor values");
//                        while(mergeCursor.moveToNext()) {
//                            String key = mergeCursor.getString(0);
//                            String value = mergeCursor.getString(1);
//                            System.out.println("for * The value of key in cursor is "+key);
//                            System.out.println("for * The value of value in cursor is "+value);
//
//                        }
//                        System.out.println("After merge currnt cursor values");
//                        while(getMyQueryCursor().moveToNext()) {
//                            String key = getMyQueryCursor().getString(0);
//                            String value = getMyQueryCursor().getString(1);
//                            System.out.println("for * The value of key in cursor is "+key);
//                            System.out.println("for * The value of value in cursor is "+value);
//
//                        }


                    }

                }catch (ConnectException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }

        protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String[] msgparts  = strings[0].trim().split("#");
            if(msgparts[0].equals(toJoin)) {
                String hashPort = msgparts[1];int i = 0;
                String afterJoin = "";
                ///System.out.println("Recieved request of JOIN by portId:" + msgparts[1] + " Check nodes before addition :" + nodes);
                nodes.add(hashPort);
                ///System.out.println("Added nodes,Check nodes after addition:" + nodes);
                Collections.sort(nodes);
                ///System.out.println("After Sorting nodes:" + nodes);
                for(String node: nodes){
                        afterJoin = afterJoin + node+"#";
                }
                afterJoin = nodes.get(nodes.size()-1)+"#"+afterJoin+nodes.get(0);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Arrange", afterJoin);
            }
                else if(msgparts[0].equalsIgnoreCase("IN")){
                       String[] keyVal = new String[4];
                       for(int i=1;i<msgparts.length;i++){
                           keyVal[i-1] = msgparts[i];
                       }
                try {
                    lookUpPort(keyVal);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }

          // return;
        //}

    }

    private void queryCheck(String selport) {
        String[] parts = selport.split("#");
        ///System.out.println("Selection port:"+selport);
        String selectionKeyHash = null;
        try {
            selectionKeyHash = genHash(parts[0]);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Cursor check = searchQuery(parts[0]);
        String atRate ="\"@\"";String star = "\"*\"";
        if(parts[0].equals(star)){
            ///System.out.println("In QUery Check Star");
            Cursor csr = getAllQuery();String resOfQuery = "";
            MatrixCursor mc = new MatrixCursor(new String[]{"key","value"});
            while(csr.moveToNext()) {
                String key = csr.getString(0);
                String value = csr.getString(1);
                ///System.out.println("The value of key in cursor is "+key);
                ///System.out.println("The value of value in cursor is "+value);
                resOfQuery = resOfQuery+key+"@"+value+"$";
                //mc.addRow((new String[] {key,value}));
            }
            ///System.out.println("Final Value of resOfQuery is:"+resOfQuery);
            if(resOfQuery.equals("")){
                ///System.out.println("In resOfQuery null");
                if(!(parts[1].equalsIgnoreCase(getSuccessorPort()))) {
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", selport);
                }
            }else{
                resOfQuery = resOfQuery+"#"+parts[1];
                ///System.out.println("Final resOfQuery:"+resOfQuery+" to be sent to port:"+getSuccessorPort());
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,"WQPALL",resOfQuery);
                if(!(parts[1].equalsIgnoreCase(getSuccessorPort()))) {
                    ///System.out.println("Going for next look up:");
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", selport);
                }
            }


        }
        //if (qlResult.equalsIgnoreCase(myPortHash)) {
        String key = null;
        while (check.moveToNext()) {
            key = check.getString(0);
            String value = check.getString(1);
            ///System.out.println("The value of key in cursor is " + key);
            ///System.out.println("The value of value in cursor is " + value);
        }

        if(key!=null){
            Cursor csr = searchQuery(parts[0]);String resOfQuery = "";
            while(csr.moveToNext()) {
                String key1 = csr.getString(0);
                String value1 = csr.getString(1);
                ///System.out.println("The value of key in cursor is "+key1);
                ///System.out.println("The value of value in cursor is "+value1);
                resOfQuery = key1+"#"+value1+"#"+parts[1];
            }
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,"WQP",resOfQuery);
        }else{
            if(!(parts[1].equalsIgnoreCase(getSuccessorPort()))) {
                ///System.out.println("Going to next port to look up");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QLU", selport);
            }
        }
    }

    private String getSuccessor() {
        return this.successor;
    }

//    private String getSuccessor(String msgpart) {
//        String scsr;
//        int index = nodes.indexOf(msgpart);
//        index++;
//        if(index!=nodes.size()){
//            scsr = nodes.get(index);
//        }else{
//            scsr = nodes.get(0);
//        }
//        return scsr;
//    }


    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            String op = msgs[0];
            System.out.println("Going to perform this operation:" + op);
            try {

                if (op.equals(toJoin)) {
                    ///System.out.println("Joining process initiated at using the portId:"+msgs[1]);
                    //String portId = genHash(msgs[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(masterPort));
                    //socket.setSoTimeout(100);
                    //socket.setSoTimeout(500);
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    //BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = toJoin+"#"+msgs[1];
                    pw.println(msgToSend);
                    pw.flush();
                    ///System.out.println("Just written on the master port");
                    //String inHashPortVal = br.readLine();
                    //System.out.println("recieved ping from masterport");
                    pw.close();
                    //myPortHash = inHashPortVal;

                }
                if(op.equals("Arrange")){
                    String[] arrayPorts = msgs[1].split("#");
                    ///System.out.println("Recieved String to arrange:"+msgs[1]);
                    int i=1;
                    while(i<arrayPorts.length-1) {
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(portHash.get(arrayPorts[i])));
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        String msgToSend = "PS#" + arrayPorts[i - 1]+"#"+portHash.get(arrayPorts[i-1])+"#"+arrayPorts[i+1]+"#"+portHash.get(arrayPorts[i+1]);
                        pw.println(msgToSend);
                        pw.flush();
                        ///System.out.println("Successor Send:"+msgToSend+" to the port:"+portHash.get(arrayPorts[i]));
                        i++;
                    }
                }
                if(op.equalsIgnoreCase("insert")){
                    ///System.out.println("Sending the insert request to the port:"+getSuccessor()+" string value:"+getSuccessorPort());
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(getSuccessorPort()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = "IN#" + msgs[1];
                    pw.println(msgToSend);
                    pw.flush();
                    ///System.out.println("Insert request Send:"+msgToSend+" to the port:"+portHash.get(getSuccessor()));
                }
                if(op.equalsIgnoreCase("QLU")) {
                    //lock = false;
                    ///System.out.println("sending the lookUp request to the successor port:" + getSuccessor() + " string value:" + getSuccessorPort());
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(getSuccessorPort()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = "QLU#" + msgs[1];
                    pw.println(msgToSend);
                    pw.flush();
                    //lock = false;
                    ///System.out.println("LookUp request Send:" + msgToSend + " to the port:" + portHash.get(getSuccessor()));
                }
                if(op.equalsIgnoreCase("FINS")){
                    ///System.out.println("Sending to store finally in the successor port:"+getSuccessor()+" from myPort:"+myPortHash);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(getSuccessorPort()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = "FINS#"+msgs[1];
                    pw.println(msgToSend);
                    pw.flush();
                    ///System.out.println("Writng Complete");
                }
                if(op.equalsIgnoreCase("WQP")){
                    ///System.out.println(msgs[1]);
                    String[] qresult = msgs[1].split("#");
                    ///System.out.println("Sending to write on the port"+qresult[2]+" from myPort:"+qresult[1]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(qresult[2]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = "WQP#"+msgs[1];
                    pw.println(msgToSend);
                    pw.flush();
                    ///System.out.println("Writng Complete");
                }
                if(op.equalsIgnoreCase("WQPALL")){
                    ///System.out.println("RCVD to send for look up:"+msgs[1]);
                    String[] qresult = msgs[1].split("#");
                    ///System.out.println("Sending to write on other port for lookup"+qresult[1]+" from myPort:"+myPort);
                    String msgToSend = "WQPALL#"+msgs[1];
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(qresult[1]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    pw.println(msgToSend);
                    pw.flush();
                    ///System.out.println("Writng Complete");
                }
                if(op.equalsIgnoreCase("del")){
                    String msgToSend = "del#"+msgs[1];
                    ///System.out.println("Sending delete to other port"+getSuccessorPort());
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(getSuccessorPort()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    pw.println(msgToSend);
                    pw.flush();

                }

            }catch (ConnectException e) {
                // TODO Auto-generated catch block
               e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
            // null;
        }
    }
}
