package main.tools;

/**
 * class as a library for commonly used register addresses can be imported
 */
public class Label_Lib {
    public static final int TMR0 = 0;
    public static final int OPTION = TMR0;
    public static final int PCL = 2;
    public static final int STATUS = 3;
    public static final int FSR = 4;

    public static final int PORTA = 5;
    public static final int TRISA = PORTA;

    public static final int PORTB = 6;
    public static final int TRISB = PORTB;

    public static final int PCLATH = 0xA;
    public static final int INTCON = 0xB;

    //labels for different bits
    //status register;
    public static final int carry = 0;
    public static final int digitcarry = 1;
    public static final int zeroflag = 2;
    public static final int powerdown = 3;
    public static final int timeout = 4;
    public static final int rp0 = 5;

    //option register
    public static final int PSA = 3;

    //intcon register
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

    //EEPROM Interrupt flag (EECON1 register)
    public static final int EEIF = 4;

}
