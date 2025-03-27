package main;

/**
 * class for standard bit operation
 * static methods can be imported
 */
public class BitOperator {
    /**
     * generates a mask with a 1 at the position
     * @param position
     * @return mask
     */
    public static int bitMaskGen(int position) {
       return 1 << position;
    }

    /**
     * SETS a bit of a given integer at the postion
     * @param value
     * @param position
     * @return changed value
     */
    public static int setBit(int value, int position) {
        return value | (1 << position);
    }

    /**
     * UNSETS a bit of a given integer at the postion
     * @param value
     * @param position
     * @return changed value
     */
    public static int unsetBit(int value, int position) {
        int mask = bitMaskGen(position);
        mask = ~mask;
        return value & mask;
    }

    /**
     * retuns a bit of a given integer of the given postion
     * @param value
     * @param position
     * @return
     */
    public static int getBit(int value, int position) {
        int result = bitMaskGen(position);
        result = result & value;
        result = result >> position;
        return result;
    }




}
