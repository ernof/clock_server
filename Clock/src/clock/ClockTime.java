package clock;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;

public class ClockTime implements Runnable{
    private Date date = new Date();
    private int szamlalo, rezgo, delay;
    private long newDate;

    public ClockTime(){
        Random rn = new Random();
        rezgo = (rn.nextInt(300) + 100);
        szamlalo = (rn.nextInt(9) + 1);
        System.out.println(rezgo + " " + szamlalo);

        int delay = (rn.nextInt(10000) - 5000);
        newDate = date.getTime() + delay;

        date.setTime(newDate);

    }

    @Override
    public void run() {
        Thread im = new Thread(new IncomingTime(this));
        im.start();

        int countDown = szamlalo;
        while(true){
            if(countDown == 0){
                newDate += 1000;
                date.setTime(newDate);
                countDown = szamlalo - 1;
                try {
                    sleep(rezgo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                --countDown;
                try {
                    sleep(rezgo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRezgoSzamlalo(){
        return "1s = " + rezgo*szamlalo + "millis";
    }

    public String getTime(){
        Calendar c1 = GregorianCalendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        c1.setTime(date);
        String str = sdf.format(c1.getTime());

        return str;
    }

    public void increaseSzamlalo(){
        if(szamlalo >= 15) return;
        ++szamlalo;
    }

    public void decreaseSzamlalo(){
        if(szamlalo <= 1) return;
        --szamlalo;
    }

    public long getDate(){
        return date.getTime();
    }
}
