package main.timers;

import main.PIC;
import main.tools.Label_Lib;

public class Timer0 {
    PIC pic;
    Prescaler ps;

    public Timer0(PIC pic, Prescaler ps) {
        this.pic = pic;
        this.ps = ps;
    }

    /**
     * updates the Timer0 corresponding to the prescaler
     */
    public void update() {
        //Timer0 Counter Selection Bit
        int TOCS = pic.memory.readBit_bank(Label_Lib.OPTION, 5, 1);
        //Timer0 Selection Edge Bit
        int TOSE = pic.memory.readBit_bank(Label_Lib.OPTION, 5, 1);

        if(TOCS == 1) {
            if(TOSE == 0) {
                //rising edge
                //TODO: Eddy connection to IO Pins
            } else {
                //falling edge
                //TODO: Eddy connection to IO Pins
            }
        } else {
            //Timer Mode
            ps.impulsFromTMR();
        }

        int value = pic.memory.read_bank(Label_Lib.TMR0,0);
        if(value == 0) {
            pic.memory.setBit(Label_Lib.INTCON, Label_Lib.T0IF);
        }
    }

    /**
     * increments the TMR0 register by one
     */
    public void increment() {
        int value = pic.memory.read_bank(Label_Lib.TMR0,0);
        value++;
        pic.memory.write_bank(Label_Lib.TMR0, value, 0);
    }
}
