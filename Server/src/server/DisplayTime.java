package server;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class DisplayTime {
    private JPanel panel1;
    private JTextField timeField;
    private JTextField rezgesField;
    public static JFrame jframe;
    private String ip = "127.0.0.1"; //"192.168.113.85";
    private int port = 13005;//13000
    public OutputStreamWriter out;
    java.net.Socket socket;
    boolean ServerOn = true;
    ServerSocket myServerSocket;

    public void startTime(){
        jframe = new JFrame("Time Server");
        jframe.setContentPane(this.panel1);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);

        try {
            myServerSocket = new ServerSocket(port);
        } catch(IOException ioe) {
            System.out.println("Could not create server socket on port 13005. Quitting.");
            System.exit(-1);
        }

        Thread im = new Thread(new RefreshWindow(this));
        im.start();

        while(ServerOn) {
            try {
                System.out.println("Waiting for clocks to connect...");
                Socket clientSocket = myServerSocket.accept();
                TimeRequests cliThread = new TimeRequests(clientSocket);
                cliThread.start();
            } catch(IOException ioe) {
                System.out.println("Exception found on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }
        try {
            myServerSocket.close();
            System.out.println("Server Stopped");
        } catch(Exception ioe) {
            System.out.println("Error Found stopping server socket");
            System.exit(-1);
        }

    }

    public String getTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public JTextField getTimeField() {
        return timeField;
    }

    public JTextField getRezgesField() {
        return rezgesField;
    }
}
