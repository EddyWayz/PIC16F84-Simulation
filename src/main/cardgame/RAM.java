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

    private int PC;

    public RAM() {
        //create memory
        RAM = new Bank[2];
        bank0 = new Bank();
        bank1 = new Bank();
        RAM[0] = bank0;
        RAM[1] = bank1;

        PC = 0;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int value) {
        PC = value;
    }

    /**
     * increments the programm counter and writes the lower 8 Bit into the PCL register
     */
    public void increment_PC() {
        PC++;
        if(PC >= 1024) {
            PC = 0;
        }
        int pcl_val = PC & Mask_Lib.LOWER8BIT_MASK;
        write(Label_Lib.PCL, pcl_val);
    }

    /**
     * puts the 3rd and 4th bit of the pclath register at the 12th and 11th bit of the PC
     */
    public void pclath_3n4_ontoPC() {
        // puts the third and fourth bit of pclath into the PC
        int pclath = read(Label_Lib.PCLATH);
        pclath = pclath & Mask_Lib.PCLATH_3_4_MASK;
        pclath = pclath << 8;
        PC = pclath;
    }

    /**
     * Init of all registers a Power On
     */
    public void powerOn_reset(){
        RAM[0].write(Label_Lib.PCL, 0);
        writeBothBanks(Label_Lib.STATUS, 0b0001_1000);
        writeBothBanks(Label_Lib.OPTION, 0b1111_1111);
        RAM[1].write(Label_Lib.TRISA, 0b0001_1111);
        RAM[1].write(Label_Lib.TRISB, 0b1111_1111);
    }



    /**
     * checks if two added integers would have a carry to the upper nibble and (un)sets the digit carry flag corresponding to the result
     * @param valA first value
     * @param valB second value
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
     * @param result that will be checked
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
     * @param instruction of current cycle
     * @return correct address
     */
    public int getIndirectAddress(int instruction) {
        // reads the address of the FSR register -> indirect addressing
        int address  = instruction & Mask_Lib.ADDRESS_MASK;
        return address == 0 ? read(Label_Lib.FSR) : address;
    }

    /**
     * checks if an address is 0
     * @param address of a register
     * @return true if address is 0
     */
    public boolean check_indirectAddressing(int address) {
        String tmp = Integer.toBinaryString(address);
        return tmp.length() == 8;
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

    public int get_Z() {
        return getBit(Label_Lib.STATUS, Label_Lib.zeroflag);
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

    public int get_RP0() {
        return getBit(Label_Lib.STATUS, Label_Lib.rp0);
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

    public int get_C() {
        return getBit(Label_Lib.STATUS, Label_Lib.carry);
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

    public int get_DC() {
        return getBit(Label_Lib.STATUS, Label_Lib.digitcarry);
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
     * returns the value of a register at a given address and bank
     * @param address of register
     * @param bank for reading
     * @return value of register
     */
    public int read_bank(int address, int bank) {
        return RAM[bank].read(address);
    }

    /**
     * reads a register at a given indirect address
     * @param address of the register
     * @return the
     */
    public int read_indirect(int address, boolean indirect) {
        if(indirect) {
            int bank = BitOperator.getBit(address, 8);
            return RAM[bank].read(address & Mask_Lib.ADDRESS_MASK);
        } else {
            return read(address);
        }
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
     * returns the value of a bit of a register at a given address and bank
     * @param address of register
     * @param position of bit
     * @param bank for reading
     * @return value of the bit
     */
    public int readBit_bank(int address, int position, int bank) {
        return RAM[bank].readBit(address, position);
    }

    /**
     * writes a whole register at a given address
     * @param address of register
     * @param value that will be written at the address
     */
    @Override
    public void write(int address, int value) {
        if(hasToMirrored(address)) {
            writeBothBanks(address, value);
        } else {
            RAM[getRP0()].write(address, value);
        }
    }

    public void write_bank(int address, int value, int bank) {
        RAM[bank].write(address, value);
    }

    /**
     * writes a value into an indirect register
     * @param address of a register
     * @param value that will be saved in the register
     */
    public void write_indirect(int address, int value) {
        int bank = BitOperator.getBit(address, 7);
        address = address & Mask_Lib.ADDRESS_MASK;
        if(hasToMirrored(address)) {
            writeBothBanks(address, value);
        } else {
            RAM[bank].write(address, value);
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
            setBitBothBanks(address, position);
        } else {
            RAM[getRP0()].setBit(address, position);
        }
    }

    /**
     * sets a bit at a given address, position and bank
     * @param address of register
     * @param position of bit
     * @param bank of RAM
     */
    public void setBit_bank(int address, int position, int bank) {
        RAM[bank].setBit(address, position);
    }

    /**
     * unsets a bit at a given address and position
     * @param address of a register
     * @param position of a bit
     */
    @Override
    public void unsetBit(int address, int position) {
        if(hasToMirrored(address)) {
            unsetBitBothBanks(address, position);
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
            case Label_Lib.TMR0, Label_Lib.PORTA, Label_Lib.PORTB, 8, 9:
                //bank1: OPTION, TRISA, TRISB, EEDATA, EECON1, EEADR, EECON2
                return false;
            default:
                return true;
        }
    }

    /**
     * writes a value on both banks
     * @param address of register
     * @param value that will be written
     */
    private void writeBothBanks(int address, int value) {
        RAM[0].write(address, value);
        RAM[1].write(address, value);
    }

    /**
     * sets a bit on both banks
     * @param address of register
     * @param position of the set bit
     */
    private void setBitBothBanks(int address, int position) {
        RAM[0].setBit(address, position);
        RAM[1].setBit(address, position);
    }

    /**
     * unsets a bit on both banks
     * @param address of register
     * @param position of the unset bit
     */
    private void unsetBitBothBanks(int address, int position) {
        RAM[0].unsetBit(address, position);
        RAM[1].unsetBit(address, position);
    }

    public String convertPCLTo4BitsString(){
        return String.format("%04X", RAM[0].read(Label_Lib.PCL));
    }
}
