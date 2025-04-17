package main.timers;

import main.PIC;

/**
 * class to represent the Watch Dog timer of the PIC
 */
public class WatchdogTMR {
    PIC pic;
    Prescaler ps;
    int counter = 0;

    public WatchdogTMR(PIC pic, Prescaler ps) {
        this.pic = pic;
        this.ps = ps;
    }

    /**
     * updates the Watch Dog Timer considering the prescaler
     */
    public void update() {
        ps.impulsFromWDT();
        //TODO: connection to GUI to check if WDT is activated
        //18.000 instruction equals 18ms
        if(counter >= 18_000) {
            //reset or wake up depending on if the pic is in sleep mode
            if(pic.getSleep()) {
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
