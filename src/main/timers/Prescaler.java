package main.timers;

import main.PIC;

public class Prescaler {
    PIC pic;
    Timer0 TMR;
    WatchdogTMR WDT;

    //Prescaler Counter
    private int psCounter;
    //PreScaler Assignment bit
    private int PSA;

    public Prescaler(PIC pic) {
        this.pic = pic;
        TMR = new Timer0(pic);
        WDT = new WatchdogTMR(pic);

        psCounter = 0;
        PSA = 1;

    }

    public void update() {

    }





}
