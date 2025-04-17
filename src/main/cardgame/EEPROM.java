package main.cardgame;

/**
 * class to represent the EEPROM of the PIC
 */
public class EEPROM {

    private int[] data;
    private RAM memory;

    public EEPROM(RAM memory) {
        //64byte size EEPROM
        data = new int[64];
        this.memory = memory;


        //TODO: Random File access


    }
}
