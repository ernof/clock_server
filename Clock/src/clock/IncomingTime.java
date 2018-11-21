package clock;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class IncomingTime implements Runnable {
    ClockTime ct;
    java.net.Socket socket;
    private String ip = "127.0.0.1"; //"192.168.113.85";
    private int port = 13005;
    boolean m_bRunThread = true;
    boolean ServerOn = true;
    private long sendTime = 0, receiveTime = 0, responseTime = 0;
    private Date date = new Date();

    IncomingTime(ClockTime ct){
        this.ct = ct;

        try {
            socket = new java.net.Socket(ip, port);
            System.out.println("Socket done");
        } catch (IOException ex) {
            Logger.getLogger(IncomingTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            out.println("0");
            out.flush();
            sendTime = date.getTime();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                /*out = new PrintWriter(
                        new OutputStreamWriter(myClientSocket.getOutputStream()));*/

            while(m_bRunThread) {
                String clientCommand = in.readLine();
                receiveTime = date.getTime();
                //System.out.println("Client Says :" + clientCommand);
                if(sendTime != 0 && receiveTime != 0 && receiveTime >= sendTime){
                    responseTime = (receiveTime - sendTime)/2;
                }
                else{
                    System.out.println("Error with send/receive time!");
                    System.exit(0);
                }


                if(!ServerOn) {
                    System.out.print("Server has already stopped");
                    out.println("Server has already stopped");
                    out.flush();
                    m_bRunThread = false;
                }
                if(clientCommand.equalsIgnoreCase("quit")) {
                    m_bRunThread = false;
                    System.out.print("Stopping client thread for client : ");
                } else if(clientCommand.equalsIgnoreCase("end")) {
                    m_bRunThread = false;
                    System.out.print("Stopping client thread for client : ");
                    ServerOn = false;
                } else {
                    long serverTime = Long.parseLong(clientCommand);
                    long finalServerTime = serverTime + responseTime;
                    long localTime = ct.getDate();
                    System.out.println("Time difference seconds: " + (localTime - finalServerTime)/1000.0);

                    if(localTime < finalServerTime){
                        System.out.println("The clock is behind!");
                        ct.decreaseSzamlalo();
                    }
                    else if(localTime == finalServerTime){
                        System.out.println("Clocks align!");
                    }
                    else{
                        System.out.println("The clock is ahead!");
                        ct.increaseSzamlalo();
                    }

                    sleep(1000);
                    out = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    out.println("0");
                    out.flush();
                    sendTime = date.getTime();
                }
            }
        } catch(Exception e) {
            //e.printStackTrace();

        }
        finally {
            try {
                in.close();
                out.close();
                socket.close();
                System.out.println("...Stopped");
            } catch(IOException ioe) {
                //ioe.printStackTrace();
            }
        }
    }
}
