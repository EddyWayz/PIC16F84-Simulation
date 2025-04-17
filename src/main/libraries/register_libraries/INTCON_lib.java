package main.libraries.register_libraries;

/**
 * labels for the bits of the INTCON register
 */
public class INTCON_lib {
    //PortB (RB7:RB4) change interrupt flag
    public static final int RBIF = 0;
    //External Interrupt RB0/INT pin
    public static final int INTF = 1;
    //Timer0 Interrupt Flag
    public static final int T0IF = 2;
    //PortB (RB7:RB4) change interrupt enable bit
    public static final int RBIE = 3;
    //External Interrupt RB0/Int enable bit
    public static final int INTE = 4;
    //Timer0 Interrupt enable bit
    public static final int T0IE = 5;
    //EEPROM Interrupt enable bit
    public static final int EEIE = 6;
    //Global Interrupt enable bit
    public static final int GIE = 7;

}
