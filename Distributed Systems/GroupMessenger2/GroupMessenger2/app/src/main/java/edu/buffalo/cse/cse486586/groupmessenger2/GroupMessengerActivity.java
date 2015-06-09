package edu.buffalo.cse.cse486586.groupmessenger2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 *
 * @author stevko
 */
public class GroupMessengerActivity extends Activity {

    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    static final String[] allport = {REMOTE_PORT0, REMOTE_PORT1, REMOTE_PORT2, REMOTE_PORT3, REMOTE_PORT4};
    static final String masterPort = "11108";
    static final int SERVER_PORT = 10000;int initCounter = 500;ArrayList<Integer> pmax = new ArrayList<Integer>();
    Uri providerUri; //static String myPort;
    static int counter = 0;
    PriorityQueue<String> msgToSave;
    TheSynchronizer ts = new TheSynchronizer();ArrayList<String> pmsgs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);

        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));

         System.out.println("the value of myport is:" + myPort);

        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
            //    System.out.println("serverTask hass been called with the server socket set to 10000");
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }


        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());


        final EditText editText = (EditText) findViewById(R.id.editText1);

       System.out.println("the edit text value has been taken which is:" + editText);
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
        
        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString() + "\n";
                editText.setText(""); // This is one way to reset the input box.
                //TextView localTextView = (TextView) findViewById(R.id.textView1);
                //localTextView.append("\t" + msg); // This is one way to display a string.
                //TextView remoteTextView = (TextView) findViewById(R.id.remote_text_display);
                //remoteTextView.append("\n");
                msg = msg.replaceAll("\\r|\\n", "");
                msg = msg + "$" + "UD";
                System.out.println("my port is " +myPort);
                System.out.println("the value sent from here are port number of mother AVD and the message " + myPort + " " + msg);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }

    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        //int counter = 0;

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            String inputSocket;
            try {
                Socket socket;
                while (true) {
                    socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    inputSocket = in.readLine();
                    //System.out.println("Just read from socket " + String.valueOf(socket) + " the message recieved is:"+inputSocket);
                    String[] array = inputSocket.split("\\$");
                    String strReceived = array[0].trim();String msgState = array[1].trim();String msgOwnerPort = array[2].trim();
                    String msgRemotePort = array[3].trim();String msgCounter = array[4].trim();
                  //  System.out.println("String recieved is:" + msgState);
                   // System.out.println("String recieved is:" + msgOwnerPort);
                   // System.out.println("String recieved is:" + msgRemotePort);
                   // System.out.println("String recieved is:" + msgCounter);
                    if (msgState.equalsIgnoreCase("UD")) {
                        //System.out.println("Get the proposal from the port number:" + msgOwnerPort + "Sending to the port:" + msgRemotePort);
                        inputSocket = ts.getProposalCounter(strReceived, msgOwnerPort, msgRemotePort);
                        out.println(inputSocket);
                        //System.out.println("Sending proposol : "+inputSocket);
                        //out.flush();
                        out.close();
                        this.publishProgress(inputSocket);
                    }else if (msgState.equalsIgnoreCase("checkPing")) {
                        //System.out.println("Get the proposal from the port number:" + msgOwnerPort + "Sending to the port:" + msgRemotePort);
                        //inputSocket = ts.checkAvds(strReceived, msgOwnerPort, msgRemotePort);
                        //System.out.println("in case checkPing:"+inputSocket);
                        String str = getPingAckString();
                        out.println(str);
                        //out.flush();
                       out.close();
                        //inputSocket = inputSocket.replace("checkPing","Proposed");
                        //System.out.println("from the checkping:"+inputSocket);
                        //this.publishProgress(inputSocket);
                    }else if(msgState.equalsIgnoreCase("D")){
                        //System.out.println("in case D the msg:"+strReceived+" from the ownerport:" + msgOwnerPort + "Storing to the port:" + msgRemotePort);
                        this.publishProgress(inputSocket);
                    }

                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */

             //  System.out.println("String recieved is:" + strings[0]);
            String array[] = strings[0].split("\\$");
            String strReceived = array[0].trim();
           // System.out.println("String recieved is:" + strings[0]);
            String msgState = array[1].trim();
              //    System.out.println("String recieved is:" + msgState);
            String msgOwnerPort = array[2].trim(); // owner message port
              //  System.out.println("String recieved is:" + msgOwnerPort);
            String msgRemotePort = array[3].trim();
              //System.out.println("String recieved is:" + msgRemotePort);
            String msgCounter = array[4].trim();
           // System.out.println("String recieved is:" + msgCounter);

            if (msgState.equalsIgnoreCase("trackAvds")) {
                //System.out.println("Get the proposal from the port number:" + msgOwnerPort + "Sending to the port:" + msgRemotePort);
                //ts.checkAvds(strReceived, msgOwnerPort, msgRemotePort);
//            }else if (msgState.equalsIgnoreCase("Proposed")) {
//                System.out.println("Storing the value:" + msgCounter + " in map of port " + msgOwnerPort + " from port:" + msgRemotePort);
//                ts.storeMaxProposed(strReceived, msgCounter, msgOwnerPort, msgRemotePort);
//
//            }
            }else if (msgState.equalsIgnoreCase("D")) {
                System.out.println("here to delibver msgsggsgs!!");
                msgToSave = ts.arrangeDeliver(strReceived,msgCounter,msgOwnerPort,msgRemotePort);
                System.out.println(msgToSave);
                if(!(msgToSave==null)) {

                    while (!(msgToSave.isEmpty())) {

                        //System.out.println("PQD not empty:" + msgToSave.peek());
                        //for(String temp:msgToSave){
                        String temp = msgToSave.remove();
                        //System.out.println("msgs to store in database:" + temp);
                        //temp = temp
                        String[] tmparr = temp.split("#");
                        strReceived = tmparr[0];ts.dCtr++;
                        //System.out.println("storage counter value:"+ts.dCtr);
                        msgCounter = String.valueOf(ts.dCtr);

                        TextView remoteTextView = (TextView) findViewById(R.id.textView1);
                        remoteTextView.append(strReceived + "\t\n");
                        //TextView localTextView = (TextView) findViewById(R.id.local_text_display);
                        //localTextView.append("\n");

            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             *
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */

                        //String filename = "SimpleMessengerOutput";
                        String string = strReceived;
                        providerUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger2.provider");
                        //FileOutputStream outputStream;
                        try {
                            ContentValues keyValueToInsert = new ContentValues();
                            keyValueToInsert.put("key", msgCounter);
                            System.out.println(msgCounter);
                            keyValueToInsert.put("value", string);
                            getContentResolver().insert(providerUri, keyValueToInsert);
                        } catch (Exception e) {
                            Log.e(TAG, "Database update failed");
                        }
                    }
                    return;
                }
            }


        }
    }

    private synchronized String getPingAckString() {
        return "MSGRecieved";
    }

    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
        //return null;
    }

    /**
     * ClientTask is an AsyncTask that should send a string over the network.
     * It is created by ClientTask.executeOnExecutor() call whenever OnKeyListener.onKey() detects
     * an enter key press event.
     *
     * @author stevko
     */
    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            System.out.println("Inside seeriel execueter");
            initCounter++; int avdCounter=0;
            String msg = msgs[0].trim();
            msg = msg.replaceAll("\\r|\\n", "");
            String ownerPort = msgs[1].trim();
            String uid = String.valueOf(initCounter);
            try {
                for (String port : allport) {
                    //if (port.equalsIgnoreCase(ownerPort)) {
                    //  continue;
                    //} else {
                    String remotePort = port.trim();
                    //      System.out.println("this:" + remotePort);
                    //if (msgs[1].equals(REMOTE_PORT0))
                    //  remotePort = REMOTE_PORT1;
                    String msgToSend = msg + "$" + ownerPort + "$" + remotePort + "$" + uid;
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(remotePort));
                    socket.setSoTimeout(500);

                /*
                 * TODO: Fill in your client code that sends out a message.                 */
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //System.out.println(msgToSend + ": sending for getproposol to : " + remotePort + "from mother port:" + ownerPort);

                    writer.println(msgToSend);
                    writer.flush();
                    String inSocket = in.readLine();
                    socket.close();
                   // socket=null;
                    //inSocket = inSocket.replace("checkPing", "Proposed");
                    System.out.print("Message Received:" + inSocket + "..##################****************..............from port " + remotePort+ "in response for get proposal");
                    if ((inSocket != null)&&(!inSocket.trim().equalsIgnoreCase("null"))) {
                        String[] ay = inSocket.split("\\$");
                        int cv = Integer.parseInt(ay[4]);
                        avdCounter++;
                        pmsgs.add(inSocket);
                        pmax.add(cv);
                        System.out.println(" Incomplete The pmsgs value:"+pmsgs+" the pmax value:"+pmax);
                    } else {
                        ts.fAvds.add(remotePort);
                    }
                }
if(pmax.size()==5)
 System.out.println("size is  5 ------------Rifght");

                ts.setNumAvds(avdCounter);
                Collections.sort(pmax);
               // assert(pmax.size()==5);
                System.out.println("Complete The pmsgs value:"+pmsgs+" the pmax value:"+pmax);
                int mx = pmax.get(pmax.size() - 1);

//                for(String m:pmsgs) {
                    String[] arr = pmsgs.get(0).split("\\$");
                    ts.storeMaxProposed(arr[0], String.valueOf(mx), arr[2]);
//                }

                pmsgs.clear();
                pmax.clear();

            }catch(SocketTimeoutException e){
                Log.e(TAG, "Socket Time out Exception");

            }catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ahahahah ClientTask socket IOException");
                e.printStackTrace();
            }
            catch (Exception e) {
                Log.e(TAG, " Unknown exception in sending for proposol");
            }

           return  null;
                  // null;
        }
    }
}