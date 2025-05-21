package main.timers;

import main.PIC;
import main.libraries.Label_Lib;
import main.libraries.register_libraries.OPTION_lib;

public class Prescaler {
    PIC pic;
    public Timer0 TMR;
    public WatchdogTMR WDT;

    //Prescaler Counter
    private PrescalerCounter psCounter;
    //PreScaler Assignment bit
    private int PSA;

    private final int PSA_TMR = 0;
    private final int PSA_WDT = 1;

    //rates for TMR0 and WDT
    private final int[][] rates = {{2, 4, 8, 16, 32, 64, 128, 256}, {1, 2, 4, 8, 16, 32, 64, 128}};
    private int currentRate = 0;

    public Prescaler(PIC pic, PrescalerCounter psCounter) {
        this.pic = pic;
        TMR = new Timer0(pic, this);
        WDT = new WatchdogTMR(pic, this);

        //init of the counter and the assignment to wdt
        PSA = 1;
        this.psCounter = psCounter;

        //set the rate of the prescaler
        currentRate = rates[PSA][getPSA_Rate()];

    }

    /**
     * updates the rates of the prescaler corresponding to the bit in the option register
     */
    public void update() {
        int psa_updated = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.PSA, 1);
        //change ps setting either if the assignment switches or the ps0-ps2 bits in the option register
        if (psa_updated != PSA || (currentRate != rates[PSA][getPSA_Rate()])) {
            PSA = psa_updated;
            //clear value of ps
            psCounter.clear();
            //change rate corresponding to psa bit
            currentRate = rates[PSA][getPSA_Rate()];
            System.out.println("----------------------" + currentRate);
        }
        WDT.update();
        TMR.update();
    }


    /**
     * method will be called, when there's an impuls from the timer
     * --> wil increment it depending on prescaler rates
     */
    public void impulsFromTMR() {
        //if ps is not assigned to the timer increment it with every impuls
        //if it is assigned: increment ps, if ps is higher than the ps rate for the timer
        //corresponding to the ps0 - ps2 bits increment timer
        if (PSA == PSA_TMR) {
            psCounter.increment();
            if (psCounter.getCounter() == currentRate) {
                TMR.increment();
                psCounter.clear();
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
            psCounter.increment();
            if (psCounter.getCounter() == currentRate) {
                WDT.increment();
                psCounter.clear();
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
            psCounter.clear();
        }
    }

    /**
     * clears the prescaler value
     */
    public void clear() {
        psCounter.clear();
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

        return psa_rate;
    }


}
