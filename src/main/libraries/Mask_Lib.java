package main.libraries;

public class Mask_Lib {
    // mask to delete all upper bits (except first 8)
    public static final int LOWER8BIT_MASK = 0b1111_1111;
    public static final int ADDRESS_MASK  = 0b0111_1111;
    public static final int BIT_POS_MASK = 0b0011_1000_0000;

    //nibbles masks
    public static final int NIBBLE_MASK = 0b1111;
    public static final int UPPER_NIBBLE_MASK = 0b1111_0000;

    // redundant to LOWER8BIT_MASK but added for readability
    public static final int LITERAL_MASK  = 0b1111_1111;

    // masks for pc and pclath manipulation
    // lowest 11 bits
    public static final int GOTO_CALL_MASK = 0b0111_1111_1111;
    public static final int PCLATH_3_4_MASK = 0b1_1000;

}
