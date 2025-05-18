package main.timers;

import main.PIC;
import main.libraries.Label_Lib;
import main.libraries.register_libraries.OPTION_lib;

public class Prescaler {
    PIC pic;
    public Timer0 TMR;
    public WatchdogTMR WDT;

    //Prescaler Counter
    private int psCounter;
    //PreScaler Assignment bit
    private int PSA;

    private final int PSA_TMR = 0;
    private final int PSA_WDT = 1;

    //rates for TMR0 and WDT
    private int[][] rates = {{2, 4, 8, 16, 32, 64, 128, 256}, {1, 2, 4, 8, 16, 32, 64, 128}};
    private int rate_TMR = 1;
    private int rate_WDT = 1;

    public Prescaler(PIC pic) {
        this.pic = pic;
        TMR = new Timer0(pic, this);
        WDT = new WatchdogTMR(pic, this);


        psCounter = 0;
        PSA = 1;


        rate_WDT = rates[PSA][getPSA_Rate()];
        rate_TMR = 1;

    }

    /**
     * updates the rates of the prescaler corresponding to the bit in the option register
     */
    public void update() {
        int psa_updated = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.PSA, 1);
        if (psa_updated != PSA) {
            PSA = psa_updated;
            this.clear();
            //prescaler changed to timer?
            if (PSA == PSA_TMR) {
                rate_TMR = rates[PSA][getPSA_Rate()];
                rate_WDT = 1;
            } else {
                rate_WDT = rates[PSA][getPSA_Rate()];
                rate_TMR = 1;
            }
        }
        WDT.update();
        TMR.update();

    }


    /**
     * method will be called, when there's an impuls from the timer
     * --> wil increment it depending on prescaler rates
     */
    public void impulsFromTMR() {
        //if ps is not assigned to the timer increment it with ervery impuls
        //if it is assigned: increment ps, if ps is higher than the ps rate for the timer
        //corresponding to the ps0 - ps2 bits increment timer
        if (PSA == PSA_TMR) {
            psCounter++;
            if (psCounter >= rate_TMR) {
                TMR.increment();
            }
        } else {
            TMR.increment();
        }
    }

    /**
     * method will be called, when there's an impuls from the WDT
     * --> wil increment it depending on prescaler rates
     */
    public void impulsFromWDT() {
        //if ps is not assigned to the wdt increment wdt with every impuls from it
        if (PSA == PSA_WDT) {
            psCounter++;
            if (psCounter >= rate_WDT) {
                WDT.increment();
            }
        } else {
            WDT.increment();
        }
    }

    /**
     * clears the current counter of the prescaler for the WDT
     */
    public void clearPS_WDT() {
        if (PSA == PSA_WDT) {
            psCounter = 0;
        }
    }

    /**
     * clears the prescaler value
     */
    public void clear() {
        psCounter = 0;
    }

    /**
     * calculates the current rate for the prescaler
     *
     * @return the current rate that is selected
     */
    private int getPSA_Rate() {
        int ps0 = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.PS0, 1);
        int ps1 = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.PS1, 1);
        int ps2 = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.PS2, 1);

        //shifting Prescaler bits
        ps1 = ps1 << 1;
        ps2 = ps2 << 2;
        //combine all three bits
        int psa_rate = ps0 | ps1 | ps2;
        System.out.println(psa_rate);
        return psa_rate;
    }


}
