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
    public static final int rp0 = 5;

    //option register
    public static final int psa = 3;

    //intcon
    public static final int t0if = 2;
}
