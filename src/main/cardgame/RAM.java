package main.cardgame;

import main.Port;
import main.exceptions.MirroringErrorException;
import main.libraries.register_libraries.STATUS_lib;
import main.timers.PrescalerCounter;
import main.tools.BitOperator;
import main.libraries.Label_Lib;
import main.libraries.Mask_Lib;


/**
 * The RAM class simulates a Random Access Memory (RAM) module with two separate banks.
 * It provides methods to read, write, and manipulate bits within the memory.
 * The class also handles the program counter (PC).
 */
public class RAM implements Memory {
    // separate banks
    Bank bank0;
    Bank bank1;
    // array of both banks
    Bank[] RAM;

    PrescalerCounter psCounter;

    private int PC;

    //Port A and B
    public Port PortA;
    public Port PortB;


    public RAM(PrescalerCounter psCounter) {
        //create memory
        RAM = new Bank[2];
        bank0 = new Bank();
        bank1 = new Bank();
        RAM[0] = bank0;
        RAM[1] = bank1;

        this.psCounter = psCounter;

        PC = 0;

        //instance of ports
        PortA = new Port("PortA");
        PortB = new Port("PortB");
    }

    /**
     * Method to calculate the integer value of the pins of a port
     *
     * @param address to calculate
     * @return value of the port
     */
    private int getValueOfPort(int address) {
        Port port = address == 5 ? PortA : PortB;
        int value = 0;
        for (int index = 0; index < 8; index++) {
            if (port.pins[index].getValue()) {
                value += Math.pow(2, index);
            }
        }
        return value;
    }

    /**
     * returns the value of one single pin of a port
     *
     * @param address  of the port
     * @param position of the pin
     * @return value of the pin 1 / 0
     */
    private int getPinValue(int address, int position) {
        Port port = address == 5 ? PortA : PortB;
        return port.pins[position].getValue() ? 1 : 0;
    }

    /**
     * method to check if port register is read
     * @param address to be checked
     * @param bank of the
     * @return true if a port register is addressed
     */
    private static boolean ReadOfPort(int address, int bank) {
        return bank == 0 && (address == Label_Lib.PORTA || address == Label_Lib.PORTB);
    }

    /**
     * method to read a value of a bit of the port register and not the pins
     * @param address of the port register
     * @param position of the bit
     * @return value of the bit
     */
    public int readBitOfPortRegister(int address, int position) {
        return RAM[0].readBit(address, position);
    }

    public Bank[] getRAM() {
        return RAM;
    }

    public int getPC() {
        return PC;
    }

    /**
     * Sets the PC and writes it into the PCL file
     *
     * @param value new value of PC
     */
    public void setPC(int value) {
        PC = value;
        int pcl_val = PC & Mask_Lib.LOWER8BIT_MASK;
        writeBothBanks(Label_Lib.PCL, pcl_val);
    }

    /**
     * Increments the programm counter and writes the lower 8 Bit into the PCL register
     */
    public void increment_PC() {
        PC++;
        if (PC >= 1024) {
            PC = 0;
        }
        int pcl_val = PC & Mask_Lib.LOWER8BIT_MASK;
        writeBothBanks(Label_Lib.PCL, pcl_val);
    }

    /**
     * Puts the 3rd and 4th bit of the pclath register at the 12th and 11th bit of the PC
     */
    public void pclath_3n4_ontoPC(int k11_newPC) {
        // puts the third and fourth bit of pclath into the PC
        int pclath = read(Label_Lib.PCLATH);
        pclath = pclath & Mask_Lib.PCLATH_4_3_MASK;

        //shift pclath by 8 bits to the left
        //zu 8 bearbeitet nach Abgabegespraech
        pclath = pclath << 8;
        //combine pclath and PC
        setPC(pclath | k11_newPC);
    }

    /**
     * Checks if the PCL register or TMR register are manipulated if so do special things see code below
     *
     * @param address of current instruction that is affected
     */
    private void checkManipulationPC_TMR(int address) {
        if (address == Label_Lib.PCL) {
            System.out.println("PC WRITTEN at " + Integer.toHexString(PC) + "======================== ");
            int pc = read(address);
            int pclath = read(Label_Lib.PCLATH);
            //get the lowest 5 bits of pclath
            pclath = pclath & 0b1_1111;
            //shift them 8 to the left
            pclath = pclath << 8;
            //combine with pc
            pc = pc | pclath;
            setPC(pc);
        }

        //is the timer register manipulated --> clear value of the prescaler
        if (address == Label_Lib.TMR0) {
            psCounter.clear();
        }
    }

    /**
     * Init of all registers a Power On
     */
    public void powerOn_reset() {
        RAM[0].write(Label_Lib.PCL, 0);
        writeBothBanks(Label_Lib.STATUS, 0b0001_1000);
        write_bank(Label_Lib.OPTION, 0b1111_1111, 1);
        RAM[1].write(Label_Lib.TRISA, 0b0001_1111);
        RAM[1].write(Label_Lib.TRISB, 0b1111_1111);
    }




    /**
     * Returns the value of register at a given address
     *
     * @param address of register
     * @return value of the register
     */
    @Override
    public int read(int address) {
        if (address == Label_Lib.PORTA || address == Label_Lib.PORTB) {
            return getValueOfPort(address);
        }
        return RAM[getRP0()].read(address);
    }

    /**
     * Returns the value of a register at a given address and bank
     *
     * @param address of register
     * @param bank    for reading
     * @return value of register
     */
    public int read_bank(int address, int bank) {
        if (ReadOfPort(address, bank)) {
            return getValueOfPort(address);
        }
        return RAM[bank].read(address);
    }

    /**
     * Reads a register at a given indirect address
     *
     * @param address of the register
     * @return value of the file
     */
    public int read_indirect(int address, boolean indirect) {
        if (indirect) {
            int bank = BitOperator.getBit(address, 8);
            address &= Mask_Lib.ADDRESS_MASK;
            if(ReadOfPort(address, bank)) {
                return getValueOfPort(address);
            }
            return RAM[bank].read(address);
        } else {
            return read(address);
        }
    }



    /**
     * Returns the value of a bit of a register at given address
     *
     * @param address  of register
     * @param position of bit
     * @return value of the bit
     */
    @Override
    public int readBit(int address, int position) {
        if(ReadOfPort(address, getRP0())) {
            return getPinValue(address, position);
        }
        return RAM[getRP0()].readBit(address, position);
    }

    /**
     * Returns the value of a bit of a register at a given address and bank
     *
     * @param address  of register
     * @param position of bit
     * @param bank     for reading
     * @return value of the bit
     */
    public int readBit_bank(int address, int position, int bank) {
        if(ReadOfPort(address, bank)) {
            return getPinValue(address, position);
        }
        return RAM[bank].readBit(address, position);
    }

    /**
     * Writes a whole register at a given address
     *
     * @param address of register
     * @param value   that will be written at the address
     */
    @Override
    public void write(int address, int value) {
        if (hasToMirrored(address)) {
            writeBothBanks(address, value);
        } else {
            RAM[getRP0()].write(address, value);
        }
        checkManipulationPC_TMR(address);
    }

    /**
     * Method to write a value on a specific bank
     *
     * @param address of register
     * @param value   that will be written
     * @param bank    of the ram
     */
    public void write_bank(int address, int value, int bank) {
        RAM[bank].write(address, value);
        checkManipulationPC_TMR(address);
    }

    /**
     * Writes a value into an indirect register
     *
     * @param address of a register
     * @param value   that will be saved in the register
     */
    public void write_indirect(int address, int value) {
        int bank = BitOperator.getBit(address, 7);
        address = address & Mask_Lib.ADDRESS_MASK;
        if (hasToMirrored(address)) {
            writeBothBanks(address, value);
        } else {
            RAM[bank].write(address, value);
        }
        checkManipulationPC_TMR(address);
    }

    /**
     * Sets a bit at a given address and positon
     *
     * @param address  of register
     * @param position of bit
     */
    @Override
    public void setBit(int address, int position) {
        if (hasToMirrored(address)) {
            setBitBothBanks(address, position);
        } else {
            RAM[getRP0()].setBit(address, position);
        }
        checkManipulationPC_TMR(address);
    }

    /**
     * Sets a bit at a given address, position and bank
     *
     * @param address  of register
     * @param position of bit
     * @param bank     of RAM
     */
    public void setBit_bank(int address, int position, int bank) {
        RAM[bank].setBit(address, position);
        checkManipulationPC_TMR(address);
    }

    /**
     * Unsets a bit at a given address and position
     *
     * @param address  of a register
     * @param position of a bit
     */
    @Override
    public void unsetBit(int address, int position) {
        if (hasToMirrored(address)) {
            unsetBitBothBanks(address, position);
        } else {
            RAM[getRP0()].unsetBit(address, position);
        }
        checkManipulationPC_TMR(address);
    }

    /**
     * Unsets a bit at a given address, position and bank
     *
     * @param address  of a register
     * @param position of a bit
     */
    public void unsetBit_bank(int address, int position, int bank) {
        RAM[bank].unsetBit(address, position);
        checkManipulationPC_TMR(address);
    }

    /**
     * Gets a value of a bit at a given address and positon
     *
     * @param address  of register
     * @param position of bit
     * @return value of the asked bit
     */
    @Override
    public int getBit(int address, int position) {
        return RAM[getRP0()].getBit(address, position);
    }

    /**
     * Returns the value of the rp0 bit and checks if it is the same in both banks
     *
     * @return value of rp0 bit
     */
    public int getRP0() {
        int first = bank0.readBit(Label_Lib.STATUS, STATUS_lib.rp0);
        int second = bank1.readBit(Label_Lib.STATUS, STATUS_lib.rp0);
        if (first == second) {
            return first;
        } else {
            throw new MirroringErrorException("RP0 bit is not the same in both banks");
        }
    }

    /**
     * Checks if a specific register has to be mirrored
     *
     * @param address that will be checked
     * @return true if the register has to be mirrored
     */
    private boolean hasToMirrored(int address) {
        switch (address) {
            case Label_Lib.TMR0, Label_Lib.PORTA, Label_Lib.PORTB, 8, 9:
                //bank1: OPTION, TRISA, TRISB, EEDATA, EECON1, EEADR, EECON2
                return false;
            default:
                return true;
        }
    }

    //METHOD TO WRITE ON BOTH BANKS SIMULTANEOUSLY
    // ONLY FOR SPECIFIC INTERNAL USE

    /**
     * Writes a value on both banks
     *
     * @param address of register
     * @param value   that will be written
     */
    private void writeBothBanks(int address, int value) {
        RAM[0].write(address, value);
        RAM[1].write(address, value);
    }

    /**
     * Sets a bit on both banks
     *
     * @param address  of register
     * @param position of the set bit
     */
    public void setBitBothBanks(int address, int position) {
        RAM[0].setBit(address, position);
        RAM[1].setBit(address, position);
    }

    /**
     * Unsets a bit on both banks
     *
     * @param address  of register
     * @param position of the unset bit
     */
    private void unsetBitBothBanks(int address, int position) {
        RAM[0].unsetBit(address, position);
        RAM[1].unsetBit(address, position);
    }

    //METHODS FOR CHECKING AND MANIPULATING STATUS FLAGS

    /**
     * Checks if two added integers would have a carry to the upper nibble and (un)sets the digit carry flag corresponding to the result
     *
     * @param valA first value
     * @param valB second value
     */
    public void check_n_manipulate_DC(int valA, int valB) {
        // mask both values to only the 4 lowest bits
        int masked_val1 = valA & Mask_Lib.NIBBLE_MASK;
        int masked_val2 = valB & Mask_Lib.NIBBLE_MASK;
        if ((masked_val1 + masked_val2) > Mask_Lib.NIBBLE_MASK) {
            set_DC();
        } else {
            unset_DC();
        }
    }

    /**
     * Checks if the carry flag has to be (un)set
     *
     * @param result that will be checked
     */
    public void check_n_manipulate_C(int result) {
        if (result > 255) {
            set_C();
        } else {
            unset_C();
        }
    }

    /**
     * Checks if the zero flag has to be (un)set
     *
     * @param result that will be checked
     */
    public void check_n_manipulate_Z(int result) {
        if (result == 0) {
            set_Z();
        } else {
            unset_Z();
        }
    }

    /**
     * SETS the zeroflag in status register
     */
    public void set_Z() {
        setBit(Label_Lib.STATUS, STATUS_lib.zeroflag);
    }

    /**
     * UNSETS the zeroflag in the status register
     */
    public void unset_Z() {
        unsetBit(Label_Lib.STATUS, STATUS_lib.zeroflag);
    }

    public int get_Z() {
        return getBit(Label_Lib.STATUS, STATUS_lib.zeroflag);
    }

    /**
     * SETS the rp0 bit in status register
     */
    public void set_RP0() {
        setBit(Label_Lib.STATUS, STATUS_lib.rp0);
    }

    /**
     * UNSETS the rp0 bit in the status register
     */
    public void unset_RP0() {
        unsetBit(Label_Lib.STATUS, STATUS_lib.rp0);
    }

    public int get_RP0() {
        return getBit(Label_Lib.STATUS, STATUS_lib.rp0);
    }

    /**
     * SETS the carry flag in status register
     */
    public void set_C() {
        setBit(Label_Lib.STATUS, STATUS_lib.carry);
    }

    /**
     * UNSETS the carry flag in status register
     */
    public void unset_C() {
        unsetBit(Label_Lib.STATUS, STATUS_lib.carry);
    }

    public int get_C() {
        return getBit(Label_Lib.STATUS, STATUS_lib.carry);
    }

    /**
     * SETS the digit carry in status register
     */
    public void set_DC() {
        setBit(Label_Lib.STATUS, STATUS_lib.digitcarry);
    }

    /**
     * UNSETS the digit carry in status register
     */
    public void unset_DC() {
        unsetBit(Label_Lib.STATUS, STATUS_lib.digitcarry);
    }

    public int get_DC() {
        return getBit(Label_Lib.STATUS, STATUS_lib.digitcarry);
    }
    /**
     * Method to turn the PCL into an 4 digit hex-number as a string
     *
     * @return hex string
     */
    public String convertPCLTo4BitsString() {
        return String.format("%04X", RAM[0].read(Label_Lib.PCL));
    }
}
