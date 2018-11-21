package server;

import static java.lang.Thread.sleep;

public class RefreshWindow implements Runnable {
    DisplayTime dt;

    RefreshWindow(DisplayTime dt){
        this.dt = dt;
    }

    @Override
    public void run() {
        while(true){
            String cTime = dt.getTimeStamp();

            //System.out.println(cTime);
            dt.getTimeField().setText(cTime);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
