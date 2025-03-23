package main;


import java.io.File;
import java.util.ArrayList;
import main.BitOperator.*;

public class PIC {
    RAM bank0;
    RAM bank1;
    private int wRegister;
    private int PCL;
    ArrayList<Integer> program = new ArrayList<>();


    public PIC(File input) {
        bank0 = new RAM(0);
        bank1 = new RAM(1);
        //INIT of basic register
        wRegister = 0;
        PCL = 0;

        //Parse file to get the program
        InstructionParser instrParser = new InstructionParser(input);
        program = instrParser.parseLinesToInstructions();


        //TODO Link to IO Pins
        //TODO Link to Instruction Decoder
        //TODO Stack init



    }


    /**
     * Move literal to W
     * @param literal
     */
    public void instr_MOVLW(int literal) {
        wRegister = literal;
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

    public int getWRegister() {
        return wRegister;
    }

    public int getPCL() {
        return PCL;
    }


}
