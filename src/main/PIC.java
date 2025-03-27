package main;

import java.util.ArrayList;



public class PIC {
    //mask to delete all upper bits (except first 8)
    private int upperZeroMask = 0b11111111;
    //memory for data and program
    RAM bank0;
    RAM bank1;
    RAM[] memory;

    ArrayList<Integer> program = new ArrayList<>(1024);

    //special registers
    private int wRegister;
    private int PC;
    private int current_instr;




    public PIC(String path) {
        //create memory
        memory = new RAM[2];
        bank0 = new RAM();
        bank1 = new RAM();
        memory[0] = bank0;
        memory[1] = bank1;
        //INIT of basic register
        wRegister = 0;
        PC = 0;

        //TODO INIT of all registers at PO

        //Parse file to get the program
        InstructionParser instrParser = new InstructionParser(path);
        program = instrParser.parseLinesToInstructions();

        //INIT of instruction library
        InstructionLibrary lib = new InstructionLibrary();



        //TODO Link to IO Pins
        //TODO Stack init





    }

    /**
     * fetches the next instruction from the program at index of the program counter
     * then the instruction will be decoded and excecuted
     *
     */
    private void fetch() {
        //get next instruction
        current_instr = program.get(PC);
        //PC increment
        PC++;
        decode_n_execute(current_instr);
    }

    private void decode_n_execute(int instruction) {
        //TODO special Instruction 10 00 (CALL)

        //TODO split instructions in 3 groups

    }


    /**
     * Move literal to W
     * @param literal
     */
    public void instr_MOVLW(int literal) {
        writeInW(literal);
        //TODO affects any Flags?
    }





    public void mirrorBanks(int mirrorToBank, int address) {
        //TODO
    }

    private void writeInW(int value) {
        wRegister = value & upperZeroMask;
    }

    public int getWRegister() {
        return wRegister;
    }

    public int getPCL() {
        return PC;
    }


}
