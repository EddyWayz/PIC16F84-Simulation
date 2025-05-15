package main.timers;

import main.PIC;

/**
 * class to represent the Watch Dog timer of the PIC
 */
public class WatchdogTMR {
    public PIC pic;
    Prescaler ps;
    int counter = 0;
    public boolean active = false;

    //just for testing purposes
    private int WDT_threshold = 50; //18 * (4 / quarz_freqency)

    public WatchdogTMR(PIC pic, Prescaler ps) {
        this.pic = pic;
        this.ps = ps;
    }

    /**
     * updates the Watch Dog Timer considering the prescaler
     */
    public void update() {
        if (!active) {
            return;
        }

        ps.impulsFromWDT();

        //18.000 instruction equals 18ms
        if (counter >= WDT_threshold) {
            counter = 0;
            //reset or wake up depending on if the pic is in sleep mode
            System.out.println("Reset from WDT");
            if (pic.getSleep()) {
                pic.wakeUp_WDT();
            } else {
                pic.reset_WDT();
            }
        }
    }


    /**
     * increments the counter of the Watch Dog Timer
     */
    public void increment() {
        counter++;
    }

    public void clear() {
        counter = 0;
    }

}
