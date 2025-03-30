package main.tools;

/**
 * class to hold all masks for each instruction of the PIC
 */
public class Instr_Lib {

    //BYTE-ORIENTED FILE REGISTER OPERATIONS
    public static final int ADDWF =  0x0700;
    public static final int ANDWF =  0x0500;
    public static final int CLRF =  0x0180;
    public static final int CLRW =  0x0100;
    public static final int COMF =  0x0900;
    public static final int DECF =  0x0300;
    public static final int DECFSZ =  0x0B00;
    public static final int INCF =  0x0A00;
    public static final int INCFSZ =  0x0F00;
    public static final int IORWF =  0x0400;
    public static final int MOVF =  0x0800;
    public static final int MOVWF =  0x0080;
    public static final int NOP =  0x0000;
    public static final int RLF =  0x0D00;
    public static final int RRF =  0x0C00;
    public static final int SUBWF =  0x0200;
    public static final int SWAPF =  0x0E00;
    public static final int XORWF =  0x0600;

    //BIT-ORIENTED FILE REGISTER OPERATIONS
    public static final int BCF =  0x1000;
    public static final int BSF =  0x1400;
    public static final int BTFSC =  0x1800;
    public static final int BTFSS =  0x1C00;

    //LITERAL AND CONTROL OPERATIONS
    public static final int ADDLW =  0x3E00;
    public static final int ANDLW =  0x3900;
    public static final int CALL =  0x2000;
    public static final int CLRWDT =  0x0064;
    public static final int GOTO =  0x2800;
    public static final int IORLW =  0x3800;
    public static final int MOVLW =  0x3000;
    public static final int RETFIE =  0x0009;
    public static final int RETLW =  0x3400;
    public static final int RETURN =  0x0008;
    public static final int SLEEP =  0x0063;
    public static final int SUBLW =  0x3C00;
    public static final int XORLW =  0x3A00;

}
