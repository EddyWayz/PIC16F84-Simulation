package main;

import java.sql.SQLOutput;
import java.util.ArrayList;
import main.BitOperator.*;

public class PIC {
    RAM bank0;
    RAM bank1;
    int wRegister;
    int PCL;
    ArrayList<Integer> program = new ArrayList<>();


    public PIC() {
        bank0 = new RAM(0);
        bank1 = new RAM(1);
        wRegister = 0;
        PCL = 0;

        //TODO Link to IO Pins
        //TODO Link to Instruction Decoder
        //TODO Stack init
        //TODO: get program from Instruction Parser

    }


    /**
     * Move literal to W
     * @param literal
     */
    public void instr_MOVLW(int literal) {
        wRegister = literal;
    }


    public void setCarry() {
        //int value = bank
    }

    /**
     * mirrors the value of a register to the other bank
     * @param mirrorToBank bank the value will be mirrored to
     * @param address
     */
    public void mirrorBanks(int mirrorToBank, int address) {
        if(mirrorToBank == 1) {
            bank1.write(address, bank0.read(address));
        } else if(mirrorToBank == 0) {
            bank0.write(address, bank1.read(address));
        } else {
            System.out.println("Mirroring was unsuccessful. No correct value for a bank was found");
        }
    }


}
