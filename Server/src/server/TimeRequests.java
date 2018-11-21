package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class TimeRequests extends Thread {
    Socket myClientSocket;
    boolean m_bRunThread = true;
    boolean ServerOn = true;
    private Date date = new Date();

    public TimeRequests() {
        super();
    }

    TimeRequests(Socket s) {
        myClientSocket = s;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        System.out.println(
                "Accepted Client Address - " + myClientSocket);

        String tempUser = "";

        try {
            in = new BufferedReader(
                    new InputStreamReader(myClientSocket.getInputStream()));
                /*out = new PrintWriter(
                        new OutputStreamWriter(myClientSocket.getOutputStream()));*/

            while(m_bRunThread) {
                String clientCommand = in.readLine();
                System.out.println("Client Says :" + clientCommand);
                //char[] buffer = clientCommand.toCharArray();

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
                    date = new Date();
                    long time = date.getTime();
                    String timeString = String.valueOf(time);
                    System.out.println("Server is sending long time: " + timeString);

                    out = new PrintWriter(
                            new OutputStreamWriter(myClientSocket.getOutputStream()));
                    out.println(timeString);
                    out.flush();
                }
            }
        } catch(Exception e) {
            //e.printStackTrace();

        }
        finally {
            try {
                in.close();
                out.close();
                myClientSocket.close();
                System.out.println("...Stopped");
            } catch(IOException ioe) {
                //ioe.printStackTrace();
            }
        }
    }
}
