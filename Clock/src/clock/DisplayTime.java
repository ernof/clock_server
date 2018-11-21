package clock;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class DisplayTime {
    private JPanel panel1;
    private JTextField timeField;
    private JTextField rezgesField;
    private static JFrame jframe;
    private String ip = "127.0.0.1"; //"192.168.113.85";
    private int port = 13005;//13000


    public void startTime(){
        jframe = new JFrame("Random Clock");
        jframe.setContentPane(this.panel1);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);

        ClockTime ct = new ClockTime();
        rezgesField.setText(ct.getRezgoSzamlalo());
        timeField.setText(ct.getTime());

        Thread im = new Thread(ct);
        im.start();

        while(true){

            timeField.setText(ct.getTime());
            rezgesField.setText(ct.getRezgoSzamlalo());
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}