package main;

/**
 * class for standard bit operation
 * static methods can be imported
 */
public class BitOperator {
    public static int bitMaskGen(int position) {
       return 1 << position;
    }

    public static int setBit(int value, int position) {
        return value | (1 << position);
    }

    public static int unsetBit(int value, int position) {
        int mask = bitMaskGen(position);
        mask = ~mask;
        return value & mask;
    }

    public static int getBit(int value, int position) {
        int result = bitMaskGen(position);
        result = result & value;
        result = result >> position;
        return result;
    }




}
