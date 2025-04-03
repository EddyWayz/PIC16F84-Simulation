package main.cardgame;

import main.exceptions.MirroringErrorException;
import main.tools.BitOperator;
import main.tools.Label_Lib;
import main.tools.Mask_Lib;


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
     * checks if two added integers would have a carry to the upper nibble and (un)sets the digit carry flag corresponding to the result
     * @param valA
     * @param valB
     */
    public void check_n_manipulate_DC(int valA, int valB) {
        // mask both values to only the 4 lowest bits
        int masked_val1 = valA & Mask_Lib.NIBBLE_MASK;
        int masked_val2 = valB & Mask_Lib.NIBBLE_MASK;
        if((masked_val1 + masked_val2) > Mask_Lib.NIBBLE_MASK) {
            set_DC();
        } else {
            unset_DC();
        }
    }

    /**
     * checks if the carry flag has to be (un)set
     * @param result that will be checked
     */
    public void check_n_manipulate_C(int result) {
        if(result > 255) {
            set_C();
        } else {
            unset_C();
        }
    }

    /**
     * checks if the zero flag has to be (un)set
     * @param result
     */
    public void check_n_manipulate_Z(int result) {
        if(result == 0) {
            set_Z();
        } else {
            unset_Z();
        }
    }



    /**
     * checks if an address is 0 and the instruction will use an indirect addressing
     * @param address of the instruction
     * @return correct address
     */
    public int check_IndirectAddressing(int address) {
        // reads the address of the FSR register -> indirect addressing
        return address == 0 ? read(Label_Lib.FSR) : address;
    }

    /**
     * SETS the zeroflag in status register
     */
    public void set_Z() {
        setBit(Label_Lib.STATUS, Label_Lib.zeroflag);
    }

    /**
     * UNSETS the zeroflag in the status register
     */
    public void unset_Z() {
        unsetBit(Label_Lib.STATUS, Label_Lib.zeroflag);
    }

    /**
     * SETS the rp0 bit in status register
     */
    public void set_RP0() {
        setBit(Label_Lib.STATUS, Label_Lib.rp0);
    }

    /**
     * UNSETS the rp0 bit in the status register
     */
    public void unset_RP0() {
        unsetBit(Label_Lib.STATUS, Label_Lib.rp0);
    }

    /**
     * SETS the carry flag in status register
     */
    public void set_C() {
        setBit(Label_Lib.STATUS, Label_Lib.carry);
    }


    /**
     * UNSETS the carry flag in status register
     */
    public void unset_C() {
        unsetBit(Label_Lib.STATUS, Label_Lib.carry);
    }


    /**
     * SETS the digit carry in status register
     */
    public void set_DC() {
        setBit(Label_Lib.STATUS, Label_Lib.digitcarry);
    }

    /**
     * UNSETS the digit carry in status register
     */
    public void unset_DC() {
        unsetBit(Label_Lib.STATUS, Label_Lib.digitcarry);
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
     * sets a bit at a given address and positon
     * @param address of register
     * @param position of bit
     */
    @Override
    public void setBit(int address, int position) {
        if(hasToMirrored(address)) {
            RAM[0].setBit(address, position);
            RAM[1].setBit(address, position);
        } else {
            RAM[getRP0()].setBit(address, position);
        }
    }

    /**
     * unsets a bit at a given address and position
     * @param address of a register
     * @param position of a bit
     */
    @Override
    public void unsetBit(int address, int position) {
        if(hasToMirrored(address)) {
            RAM[0].unsetBit(address, position);
            RAM[1].unsetBit(address, position);
        } else {
            RAM[getRP0()].unsetBit(address, position);
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
