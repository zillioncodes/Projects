package edu.buffalo.cse.cse486586.groupmessenger2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by zappykid on 3/6/15.
 */
public class TheSynchronizer {
    static int counter=-1; static int dCtr=-1;int numAvds;
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124"; static int max=-1;
//    HashMap<String, Integer> pmap= new HashMap<String,Integer>();
//    HashMap<String, Integer> pact= new HashMap<String,Integer>();


    static final String[] allport = {REMOTE_PORT0,REMOTE_PORT1,REMOTE_PORT2,REMOTE_PORT3,REMOTE_PORT4};
    HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();PriorityQueue<String> pq; Queue<String> pqd = new LinkedList<String>();
    ArrayList<Integer> pValues = new ArrayList<Integer>();
    ArrayList<String> fAvds = new ArrayList<String>();
    Queue<String> msgs = new LinkedList<String>();
    public TheSynchronizer(){
        pq = new PriorityQueue<String>(30, new Comparator<String>() {
            public int compare(String n1, String n2) {
                // compare n1 and n2
                int str;
                String[] arr1 = n1.split("#");String[] arr2 = n2.split("#");
                float one = Float.parseFloat(arr1[1]); float two = Float.parseFloat(arr2[1]);
                if(one<two){str = -1;}else{str=1;}
                return str;
            }
        });
    }



        public synchronized String getProposalCounter (String strReceived, String
        msgOwnerPort, String msgRemotePort){

        counter++;
        String strProposed = strReceived + "$trackAvds$" + msgOwnerPort + "$" + msgRemotePort + "$" + String.valueOf(counter);
        msgs.add(strProposed);
        return strProposed;
    }

        public synchronized PriorityQueue<String> arrangeDeliver (String strReceived, String
        msgCounter, String msgOwnerPort, String msgRemotePort){
        int qSize = msgs.size() - 1;
        //int dCtr=//Integer.parseInt(msgCounter);
        //System.out.println("dctr value "+dCtr+"counter:"+counter);

        int diff = qSize - dCtr; if(Integer.parseInt(msgCounter)>max){max = Integer.parseInt(msgCounter);}
        //System.out.println("Inside arrange deliver, the size of msg queue:" + msgs.size() + " and diff:" + diff);
        // if(diff>1){
        //System.out.println("Recieved msg to deliver from "+msgOwnerPort+" to "+msgRemotePort+" counter value:"+msgCounter+" msg:"+strReceived);
        int pty = getPriority(msgOwnerPort);
        strReceived = strReceived + "#" + msgCounter + "." + String.valueOf(pty);

            pq.add(strReceived);

           // System.out.println(pq);
            //System.out.println(pq);
        //System.out.println(diff + " is diff value and size of queue is " + pq.size());
//            if(Integer.parseInt(msgCounter)>qSize){
//                return null;
//            }else{0
//                return pq;
//            }

        if (diff==pq.size() && max==counter) {
            return pq;
        } else {
            System.out.println(max + " orginal counter:"+ counter);
            return null;
        }
    }

    public synchronized void checkAvds(String strReceived, String msgOwnerPort, String msgRemotePort) {

        String strProposed = strReceived+"$checkPing$"+msgOwnerPort+"$"+msgRemotePort+"$"+String.valueOf(counter);
        //System.out.println("Inside Check AVDs:"+strProposed);
        //msgs.add(strProposed);
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, strProposed);
    }

//    public void storeMaxProposed(String strReceived, String counterVal, String msgOwnerPort, String msgRemotePort) {
    public void storeMaxProposed(String strReceived, String counterVal, String msgOwnerPort) {
       // System.out.println("got the max counter value " + counterVal + " from the port:" + msgRemotePort);
        strReceived = strReceived + "$D$" + msgOwnerPort + "$" + "tempRemotePort" + "$" + counterVal;

            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, strReceived);
           // pValues.clear();
        }
        //pValues.clear();




    private int getPriority(String msgOwnerPort) {
        int m = Integer.parseInt(msgOwnerPort);
        switch (m) {
            case 11108:
                return 1;
            case 11112:
                return 2;
            case 11116:
                return 3;
            case 11120:
                return 4;
            case 11124:
                return 5;
            default:
                return 0;
        }
    }

    public void setNumAvds(int numAvds) {
        this.numAvds = numAvds;
    }

    public int getNumAvds() {
        return numAvds;
    }


    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            System.out.println("i am here:"+msgs[0]);
            String msg = msgs[0].trim();
            msg = msg.replaceAll("\\r|\\n", "");
            //int initCounter = 0;
            //String ownerPort = msgs[1].trim();
            String[] tmpmsg = msg.split("\\$");
            String strReceived = tmpmsg[0].trim();
           // System.out.println("String recieved is:" + strReceived);
            String msgState = tmpmsg[1].trim();
          //  System.out.println("String recieved is:" + msgState);
            String msgOwnerPort = tmpmsg[2].trim(); // owner message port
          //  System.out.println("String recieved is:" + msgOwnerPort);
            String msgRemotePort = tmpmsg[3].trim();
          //  System.out.println("String recieved is:" + msgRemotePort);
            String msgCounter = tmpmsg[4].trim();
          //  System.out.println("String recieved is:" + msgCounter);
          //  System.out.println("Synchronizer, this is again new:" + msg + ":from port:" + msgOwnerPort);

            try {
                if (msgState.equalsIgnoreCase("checkPing")) {
                    String strProposed = strReceived + "$checkPing$" + msgOwnerPort + "$" + msgRemotePort + "$" + String.valueOf(msgCounter);
                    String remotePort = msgOwnerPort.trim();
                    //System.out.println("this is while sending the proposed value to parent port from:" + remotePort);
                    //if (msgs[1].equals(REMOTE_PORT0))
                    //  remotePort = REMOTE_PORT1;
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(remotePort));
                    socket.setSoTimeout(500);
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //System.out.println("Wrote on the socket "+msgOwnerPort+" with the proposal value "+counter+" the string sent is "+strProposed);
                    writer.println(strProposed);
                    writer.flush();
                    String inSocket = in.readLine();
                    Thread.sleep(2);
                    System.out.print("Message Received by pingack proposer:" + inSocket+"..##################****************..............from port "+remotePort);
                    if ((inSocket == null)) {
                        System.out.println("yaya I hav gt a null");
                        fAvds.add(remotePort);
                        for (String s : msgs) {
                            String[] array = s.split("$");
                            String fport = array[3];
                            if (s.equalsIgnoreCase(fport)) {
                                for(String q:pq){
                                    if(q.contains(array[0])){
                                        fAvds.remove(s);
                                    }
                                }
                            }
                        }
                    }
                }else {
                    for (String port : allport) {

                        String remotePort = port.trim();
                        //  System.out.println("this:" + remotePort);
                        //if (msgs[1].equals(REMOTE_PORT0))
                        //  remotePort = REMOTE_PORT1;
                        String msgToSend = strReceived + "$D$" + msgOwnerPort + "$" + remotePort + "$" + msgCounter;

                        System.out.println(msgToSend + "will be delivered   to the " + remotePort + " from mother port:" + msgOwnerPort);

                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                Integer.parseInt(remotePort));
                        socket.setSoTimeout(500);


                /*
                 * TODO: Fill in your client code that sends out a message.
                 */
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.println(msgToSend);
                        writer.flush();
                      //  socket.close();
                        //Cursor resultCursor = getContentResolver().query(providerUri, null, key, null, null);
                    //}
                }
                }
            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ahahahah ClientTask socket IOException");
            } catch (InterruptedException e) {
                  Log.e(TAG, "Thread Exception");
                  e.printStackTrace();
            }

            return null;
        }
    }
}