package main.tools;

public class Mask_Lib {
    // mask to delete all upper bits (except first 8)
    public static final int UPPERZEROS_MASK = 0b1111_1111;
    public static final int ADDRESS_MASK  = 0b0111_1111;
    public static final int NIBBLE_MASK = 0b1111;
    public static final int LITERAL_MASK  = 0b1111_1111;
}
