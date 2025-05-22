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


    private int WDT_threshold;

    public WatchdogTMR(PIC pic, Prescaler ps) {
        this.pic = pic;
        this.ps = ps;
        //18.000 instruction equals 18ms at 4Mhz
        //WDT_threshold = (int) (18_000 * (4 / pic.getQuarz_frequenzy()));
        //TODO: value changed for testing
        WDT_threshold = 10;
    }

    /**
     * updates the Watch Dog Timer considering the prescaler
     */
    public void update() {
        //is wdt active: if not -> skip
        if (!active) {
            return;
        }

        //send impuls to prescaler
        ps.impulsFromWDT();


        if (counter >= WDT_threshold) {
            counter = 0;
            //reset or wake up depending on if the pic is in sleep mode
            ps.clear();
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
        System.out.println(counter);
        counter++;
    }

    public void clear() {
        counter = 0;
    }

}
