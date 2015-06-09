package edu.buffalo.cse.cse486586.simpledynamo;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zappykid on 4/14/15.
 */
public class nodeHub {
    static final String[] allport = {"11108", "11112", "11116", "11120", "11124"};
    HashMap<String,String> nodeHashMap  = new HashMap<String,String>();//boolean updateFlag=true;
    public static HashMap<String,String> failInserts = new HashMap<String,String>(); boolean checkFlag = false;
    public static HashMap<String,String> nodeState = new HashMap<String,String>();int count=0;
    ArrayList<String> SucPre = new ArrayList<String>();//, predecessor, successorPort, predecessorPort;
    String myPort, myPortHash;ArrayList<String> nodes = new ArrayList<String>();
    //public static HashMap<String,ArrayList<String>> nodeSucPreMap = new HashMap<String,ArrayList<String>>();
    nodeHub(String port){ myPort = port;}

    public void initNodes() throws NoSuchAlgorithmException {
        int intPort = Integer.parseInt(myPort)/2;
        myPortHash = genHash(String.valueOf(intPort));nodeHashMap.put(myPortHash,myPort);
        nodes.add(myPortHash);
        System.out.println("My Port:"+myPort+" My PortHash:"+myPortHash);
        for(String s:allport){
            if(s.equalsIgnoreCase(myPort)){
                //nodeState.put(myPort,"A");
                continue;
            }else{
                int tmpPort = Integer.parseInt(s)/2;
                String tmp = genHash(String.valueOf(tmpPort));
                //nodeState.put(s,"A");
                nodeHashMap.put(tmp,s);
                nodes.add(tmp);
            }
        }
        Collections.sort(nodes);
        System.out.println("State of Nodes:"+nodes);
        int index = nodes.indexOf(myPortHash);
        while(SucPre.size()<nodes.size()-1){
            index++;
            if(index>nodes.size()-1){
                index = 0;
            }
            SucPre.add(nodes.get(index));
        }
        System.out.println("State of Nodes:"+nodes);
        System.out.println("State of PreSuc Nodes:"+SucPre);
        //String joined = TextUtils.join(",", SucPre);
        //System.out.println("Sending info about Suc Pre to other nodes:"+joined);
        //new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "SUCPRE", joined);
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
//
//    public void initSucPre(String inmsg, String frmPort) {
//        System.out.println("In initSucPre:"+inmsg+" from port:"+frmPort);
//        String[] arr = inmsg.split(",");
//        ArrayList<String> arrList = new ArrayList<String>(Arrays.asList(arr));
//        nodeSucPreMap.put(frmPort,arrList);
//        System.out.println("The nodeSucPreMap:" + nodeSucPreMap);
//
//    }

    public void handleInsert(String msgToSend, String WastoBeSendPort) {
        System.out.println("Handling insert with message:"+msgToSend+" was to be send to port:"+WastoBeSendPort);
        String toSendPort,toSendPortReplica;        /// mtS = IF#key#value
        for(Map.Entry<String,String> etr:nodeHashMap.entrySet()){
            if(etr.getValue().equals(WastoBeSendPort)){
                WastoBeSendPort = etr.getKey();
            }
        }
        System.out.println("After conversion port:"+WastoBeSendPort);
        int pos = SucPre.indexOf(WastoBeSendPort);
        pos++;
        if(pos>3){
            toSendPort = myPort;
            pos=0;
        }else{
            toSendPort = nodeHashMap.get(SucPre.get(pos));
        }
        pos++;
        if(pos>3){
            toSendPortReplica = myPort;
        }else{
            toSendPortReplica = nodeHashMap.get(SucPre.get(pos));
        }
        System.out.println("Failure,Sending to Port:"+toSendPort);
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "IF", msgToSend, toSendPort, toSendPortReplica, nodeHashMap.get(WastoBeSendPort));

    }

    public void handleQuery(String msgToQueryOnFail, String WasToQueryPort) {
        System.out.println("Handling Query with message:"+msgToQueryOnFail);
        String toQueryPort;
        for(Map.Entry<String,String> etr:nodeHashMap.entrySet()){
            if(etr.getValue().equals(WasToQueryPort)){
                WasToQueryPort = etr.getKey();
            }
        }
        System.out.println("After conversion port:"+WasToQueryPort);
        int pos = SucPre.indexOf(WasToQueryPort);
        pos++;
        if(pos>3){
            toQueryPort = myPortHash;
            pos=0;
        }else{
            toQueryPort = SucPre.get(pos);
        }
        System.out.println("Query Failure,Sending to Port:"+toQueryPort);
        String[] temparr = msgToQueryOnFail.split("#");
        msgToQueryOnFail = temparr[0]+"#"+nodeHashMap.get(toQueryPort)+"#"+temparr[1]; //key#toQueryPort#fromQueryPort
        System.out.println("new changes query after fail:"+msgToQueryOnFail);
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "QF", msgToQueryOnFail, nodeHashMap.get(toQueryPort));

    }

    public void updateInsFail() {
        System.out.println("Updating myself after failure :(");
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UPDTINS", SucPre.get(0));
    }

    public void updateRepFail() {
        System.out.println("Updating myself after failure :(");
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UPDTREP", SucPre.get(0));
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "UPDTREPONE", SucPre.get(SucPre.size()-2));
    }

    public void handleRepFail(String msg, String WastoBeSendPort) {
        String toRFSendPort; // msg = key#value
        System.out.println("Msg which failed to replicate:"+msg+" was to be send to port:"+nodeHashMap.get(WastoBeSendPort));
        int pos = SucPre.indexOf(WastoBeSendPort); // wastobesend in hash form
        pos++;
        if(pos>3){
            toRFSendPort = myPort;
            pos=0;
        }else{
            toRFSendPort = nodeHashMap.get(SucPre.get(pos));
        }
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "RF", msg , toRFSendPort, WastoBeSendPort);
    }

    public void checkAVDNum() {
        checkFlag = true;
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "CHK", "ping");
    }

    public void handleReadFail(String key, String WasToReadPort, String FromPort) {
        System.out.println("Handling Query with key:"+key);
//        String NowToReadPort;
//        for(Map.Entry<String,String> etr:nodeHashMap.entrySet()){
//            if(etr.getValue().equals(WasToReadPort)){
//                WasToReadPort = etr.getKey();
//            }
//        }
//        System.out.println("After conversion port:"+WasToReadPort);
//        int pos = SucPre.indexOf(WasToReadPort);
//        pos++;
//        if(pos>3){
//            NowToReadPort = myPort;
//            pos=0;
//        }else{
//            NowToReadPort = SucPre.get(pos);
//        }
//        System.out.println("Read Failure,Sending to Port:"+NowToReadPort);
//        //String[] temparr = msgToQueryOnFail.split("#");
//        String msgToReadOnFail = key+"#"+nodeHashMap.get(NowToReadPort)+"#"+FromPort;
        //String msgToReadOnFail = key+"#"+myPort+"#"+FromPort;
        System.out.println("new changes query after fail:"+key);
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "READFAIL", key,FromPort, myPort);

    }

    private class ClientTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msgs) {
            String op = msgs[0];String inSocket = null;
            try {
                if(op.equalsIgnoreCase("IF")){
                    System.out.println("In IF while Sending to the port:"+msgs[2]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(msgs[2]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = msgs[1]+"#"+msgs[3]+ "#"+ msgs[4];
                    pw.println(msgToSend);
                    //IF#key#value#tspr#wtbsp
                    pw.flush();
                    System.out.println("Failure handling msg sent"+msgToSend);
                }else if(op.equalsIgnoreCase("QF")){
                    System.out.println("In QF while Sending to the port:"+msgs[2]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(msgs[2]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String msgToSend = "QONE#"+msgs[1];  //Query#key#toQueryPort#fromQueryPort
                    pw.println(msgToSend);
                    //IF#key#value#keyhash#tspr#wtbsp
                    pw.flush();
                    System.out.println("In QF,Failure handling query sent"+msgToSend);
                }else if(op.equalsIgnoreCase("UPDTINS")){
                    do {
                        System.out.println("To Update insert in my port:" + myPort + " from port:" + msgs[1]);
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(nodeHashMap.get(msgs[1])));
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String msgToSend = "UPDTINS#" + myPort;
                        pw.println(msgToSend);
                        pw.flush();
                        inSocket = in.readLine();
                        //socket.close();
                        System.out.println("update msg to Write:" + msgToSend);
                        System.out.println("##################################################Recieved ping:" + inSocket);
                    }while(inSocket==null);
                }else if(op.equalsIgnoreCase("UPDTREP")){
                    do {
                        System.out.println("To Update replicate in my port:" + myPort + " from port:" + msgs[1]);
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(nodeHashMap.get(msgs[1])));
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String msgToSend = "UPDTREP#" + myPort;
                        pw.println(msgToSend);
                        pw.flush();
                        inSocket = in.readLine();
                        //socket.close();
                        System.out.println("update msg replicate fail to Write:" + msgToSend);
                        System.out.println("##################################################Recieved ping:" + inSocket);
                    }while(inSocket==null);
                }else if(op.equalsIgnoreCase("RF")){
                    System.out.println("Replication failed,sending "+msgs[1]+" to store in next node"+msgs[2]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(msgs[2]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "RF#" + msgs[1]+"#"+nodeHashMap.get(msgs[3]);
                    pw.println(msgToSend);
                    pw.flush();
                    System.out.println("Sending complete");
                }else if(op.equalsIgnoreCase("UPDTREPONE")){
                    do {
                        System.out.println("To Update replicate one in my port:" + myPort + " from port:" + msgs[1]);
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(nodeHashMap.get(msgs[1])));
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String msgToSend = "UPDTREPONE#" + myPort;
                        pw.println(msgToSend);
                        pw.flush();
                        inSocket = in.readLine();
                        //socket.close();
                        System.out.println("update msg replicate one fail to Write:" + msgToSend);
                        System.out.println("##################################################Recieved ping:" + inSocket);
                    }while(inSocket==null);
                }else if(op.equalsIgnoreCase("READFAIL")){
                    System.out.println("Sending new read query key:"+msgs[1]+" to port:"+msgs[3]);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(msgs[3]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msgToSend = "READ#" + msgs[1]+"#"+ msgs[2];
                    pw.println(msgToSend);
                    pw.flush();
                    System.out.println("Send read complete:"+msgToSend);
                }
            }catch(Exception e){
                System.out.println("Exception Occurred");
                e.printStackTrace();
            }
            return null;
        }
    }
}
