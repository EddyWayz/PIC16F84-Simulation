package main.cardgame;

import main.exceptions.MirroringErrorException;
import main.tools.Label_Lib;


public class RAM implements Memory {
    // separate banks
    Bank bank0;
    Bank bank1;
    // array of both banks
    Bank[] RAM;


    public RAM() {
        //create memory
        RAM = new Bank[2];
        bank0 = new Bank();
        bank1 = new Bank();
        RAM[0] = bank0;
        RAM[1] = bank1;
    }

    /**
     * returns the value of register at a given address
     * @param address of register
     * @return value of the register
     */
    @Override
    public int read(int address) {
        return RAM[getRP0()].read(address);
    }

    /**
     * returns the value of a bit of a register at given address
     * @param address of register
     * @param position of bit
     * @return value of the bit
     */
    @Override
    public int readBit(int address, int position) {
        return RAM[getRP0()].readBit(address, position);
    }

    /**
     * writes a whole register at a given address
     * @param address of register
     * @param value that will be written at the address
     */
    @Override
    public void write(int address, int value) {
        if(hasToMirrored(address)) {
            RAM[0].write(address, value);
            RAM[1].write(address, value);
        } else {
            RAM[getRP0()].write(address, value);
        }
    }

    /**
     * writes a bit at a given address and positon
     * @param address of register
     * @param position of bit
     */
    @Override
    public void writeBit(int address, int position) {
        if(hasToMirrored(address)) {
            RAM[0].writeBit(address, position);
            RAM[1].writeBit(address, position);
        } else {
            RAM[getRP0()].writeBit(address, position);
        }
    }

    /**
     * gets a value of a bit at a given address and positon
     * @param address of register
     * @param position of bit
     * @return value of the asked bit
     */
    @Override
    public int getBit(int address, int position) {
        return RAM[getRP0()].getBit(address, position);
    }

    /**
     * returns the value of the rp0 bit and checks if it is the same in both banks
     * @return value of rp0 bit
     */
    public int getRP0() {
        int first = bank0.readBit(Label_Lib.STATUS, Label_Lib.rp0);
        int second = bank1.readBit(Label_Lib.STATUS, Label_Lib.rp0);
        if(first == second) {
            return first;
        } else {
            throw new MirroringErrorException("RP0 bit is not the same in both banks");
        }
    }

    /**
     * checks if a specific register has to be mirrored
     * @param address that will be checked
     * @return true if the register has to be mirrored
     */
    private boolean hasToMirrored(int address) {
        switch(address) {
            case Label_Lib.PCL, Label_Lib.STATUS, Label_Lib.FSR, Label_Lib.PCLATH, Label_Lib.INTCON:
                return true;
            default:
                return false;
        }
    }
}
