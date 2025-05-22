package main.timers;

import main.PIC;
import main.libraries.Label_Lib;
import main.libraries.register_libraries.INTCON_lib;
import main.libraries.register_libraries.OPTION_lib;



public class Timer0 {
    PIC pic;
    Prescaler ps;

    private int RA4_old;


    public Timer0(PIC pic, Prescaler ps) {
        this.pic = pic;
        this.ps = ps;
        RA4_old = pic.memory.readBit_bank(Label_Lib.PORTA, 4, 1);
    }

    /**
     * updates the Timer0 corresponding to the prescaler
     */
    public void update() {
        //Timer0 Counter Selection Bit
        int TOCS = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.TOCS, 1);
        //Timer0 Selection Edge Bit
        int TOSE = pic.memory.readBit_bank(Label_Lib.OPTION, OPTION_lib.TOSE, 1);

        //TMR0 is only active when the PIC is awake
        if(!pic.getSleep()) {
            if(TOCS == 1) {
                int RA4_new = pic.memory.readBit_bank(Label_Lib.PORTA, 4, 0);
                //edge detection
                if(RA4_new != RA4_old) {
                    if(TOSE == 0 && RA4_new == 1) {
                        //rising edge
                        ps.impulsFromTMR();
                    } else if(TOSE == 1 && RA4_new == 0) {
                        //falling edge
                        ps.impulsFromTMR();
                        System.out.println("### Falling edge of RA4 #########################");
                    }

                    //save new value of RA4 bit
                    RA4_old = RA4_new;
                }

            } else {
                //Timer Mode
                ps.impulsFromTMR();
            }
        }

    }

    /**
     * increments the TMR0 register by one
     */
    public void increment() {
        int value = pic.memory.read_bank(Label_Lib.TMR0,0);
        value++;
        //int value = pic.memory.read_bank(Label_Lib.TMR0,0);
        if(value >= 256) {
            value = 0;
            //not setting zeroflag, will be set from movf instruction
            System.out.println("### Overflow of timer ####################################");

            pic.memory.setBitBothBanks(Label_Lib.INTCON, INTCON_lib.T0IF);
        }
        //direct access to bank to skip the checking of writing timer file
        //so clearing of prescaler is not executed
        pic.memory.getRAM()[0].write(Label_Lib.TMR0, value);
        //pic.memory.write_bank(Label_Lib.TMR0, value, 0);
    }
}
