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
    private int W;
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
        W = 0;
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
     */
    private void fetch() {
        //get next instruction
        current_instr = program.get(PC);
        //PC increment
        PC++;
        decode_n_execute(current_instr);
    }

    /**
     * decodes and executes the next instruction
     * @param instruction
     */
    private void decode_n_execute(int instruction) {


    }

    //BYTE-ORIENTED FILE REGISTER OPERATIONS
    /**
     * Add W and f
     *
     * Add the contents of the W register with the contents of register f.
     * If d is 0 the result is stored back in the W register
     * If d is 1 the result is stored back in register f
     *
     * Status affected: C, DC, Z
     * @param instruction
     */
    private void instr_ADDWF(int instruction) {

    }

    /**
     * AND W with f
     *
     * AND the W register with contents of register 'f'.
     * If 'd' is 0 the result is stored in the W register.
     * If 'd' is 1 the result is stored back in register 'f'.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_ANDWF(int instruction) {

    }

    /**
     * Clear f
     *
     * The contents of register ’f’ are cleared
     * and the Z bit is set.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_CLRF(int instruction) {

    }

    /**
     * Clear W
     *
     * W register is cleared. Zero bit (Z) is set.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_CLRW(int instruction) {

    }

    /**
     * Complement f
     *
     * The contents of register ’f’ are complemented. If ’d’ is 0 the result is stored in
     * W. If ’d’ is 1 the result is stored back in
     * register ’f’.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_COMF(int instruction) {

    }

    /**
     * Decrement f
     *
     * Decrement contents of register ’f’. If ’d’ is 0 the
     * result is stored in the W register. If ’d’ is
     * 1 the result is stored back in register ’f’.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_DECF(int instruction) {

    }

    /**
     * Decrement f, Skip if 0
     *
     * The contents of register ’f’ are decremented. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     * If the result is not 0, the next instruction, is
     * executed. If the result is 0, then a NOP is
     * executed instead making it a 2TCY instruction.
     *
     * Status affected: None
     * @param instruction
     */
    private void instr_DECFSZ(int instruction) {}

    /**
     * Increment f
     *
     * The contents of register ’f’ are incremented.
     * If ’d’ is 0 the result is placed in the W register.
     * If ’d’ is 1 the result is placed back in register ’f’.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_INCF(int instruction) {}

    /**
     * Increment f, Skip if 0
     *
     * The contents of register ’f’ are incremented. If ’d’ is 0 the result is placed in
     * the W register. If ’d’ is 1 the result is placed back in register ’f’.
     * If the result is not 0, the next instruction is executed.
     * If the result is 0, a NOP is executed instead making it a 2TCY instruction.
     *
     * Status affected: None
     * @param instruction
     */
    private void instr_INCFSZ(int instruction) {}

    /**
     * Inclusive OR W with f
     *
     * Inclusive OR the W register with contents of register ’f’.
     * If ’d’ is 0 the result is placed in the W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_IORWF(int instruction) {}

    /**
     * Move f
     *
     * The contents of register f is moved to a destination dependant upon the status of d.
     * If d = 0, destination is W register. If d = 1, the destination is file register f itself.
     * d = 1 is useful to test a file register since status flag Z is affected.
     *
     * Status affected: Z
     * @param instruction
     */
    private void instr_MOVF(int instruction) {}

    /**
     * Move W to f
     *
     * Move data from W register to register
     *
     * Status affected: None
     * @param instruction
     */
    private void instr_MOVWF(int instruction) {}

    /**
     * No Operation
     *
     * No operation.
     *
     * Status affected: None
     * @param instruction
     */
    private void instr_NOP(int instruction) {}

    /**
     * Rotate Left f through Carry
     *
     * The contents of register ’f’ are rotated one bit to the left through the Carry Flag.
     * If ’d’ is 0 the result is placed in the W register. If ’d’ is 1 the result is stored
     * back in register ’f’.
     *
     * Status affected: C
     * @param instruction
     */
    private void instr_RLF(int instruction) {}

    /**
     * Rotate Right f through Carry
     *
     * The contents of register ’f’ are rotated one bit to the right through the Carry Flag.
     * If ’d’ is 0 the result is placed in the W register.
     * If ’d’ is 1 the result is placed back in register ’f’.
     *
     * Status affected: C
     * @param instruction
     */
    private void instr_RRF(int instruction) {}

    /**
     * Subtract W from f
     *
     * Subtract (2’s complement method) contents of W register from register 'f'.
     * If 'd' is 0 the result is stored in the W register. If 'd' is 1 the
     * result is stored back in register 'f'.
     *
     * Status affected: C, DC, Z
     * @param instruction
     */
    private void instr_SUBWF(int instruction) {}

    /**
     * Swap Nibbles in f
     *
     * The upper and lower nibbles of contents of register 'f' are exchanged.
     * If 'd' is 0 the result is placed in W register.
     * If 'd' is 1 the result is placed in register 'f'.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_SWAPF(int instruction) {

    }

    /**
     * Exclusive OR W with f
     *
     * The contents of the W register are XOR’ed with the eight bit literal 'k'.
     * The result is placed in the W register.
     *
     * Status affected: Z
     * @param instruction
     */
    public void instr_XORWF(int instruction) {

    }

    //BIT-ORIENTED FILE REGISTER OPERATIONS
    /**
     * Bit Clear f
     *
     * Bit ’b’ in register ’f’ is cleared
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_BCF(int instruction) {

    }

    /**
     * Bit Set f
     *
     * Bit ’b’ in register ’f’ is set.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_BSF(int instruction) {

    }

    /**
     * Bit Test f, Skip if Clear
     *
     * If bit ’b’ in register ’f’ is ’1’ then the next instruction is executed.
     * If bit ’b’, in register ’f’, is ’0’ then the next
     * instruction is discarded, and a NOP is executed instead, making this a 2TCY instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_BTFSC(int instruction) {

    }

    /**
     * Bit Test, Skip if Set
     *
     * If bit ’b’ in register ’f’ is ’0’ then the next instruction is executed.
     * If bit ’b’ is ’1’, then the next instruction is
     * discarded and a NOP is executed instead, making this a 2TCY instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_BTFSS(int instruction) {

    }

    //LITERAL AND CONTROL OPERATIONS
    /**
     * Add literal and W
     *
     * The contents of the W register are
     * added to the eight bit literal ’k’ and the
     * result is placed in the W register.
     *
     * Status affected: C, DC, Z
     * @param instruction
     */
    public void instr_ADDLW(int instruction) {

    }

    /**
     * AND literal with W
     *
     * The contents of W register are
     * AND’ed with the eight bit literal 'k'. The
     * result is placed in the W register.
     *
     * Status affected: Z
     * @param instruction
     */
    public void instr_ANDLW(int instruction) {

    }

    /**
     * Call Subroutine
     *
     * Call Subroutine. First, return address
     * (PC+1) is pushed onto the stack. The
     * eleven bit immediate address is loaded
     * into PC bits <10:0>. The upper bits of
     * the PC are loaded from PCLATH. CALL
     * is a two cycle instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_CALL(int instruction) {

    }

    /**
     * Clear Watchdog Timer
     *
     * CLRWDT instruction resets the Watchdog Timer. It also resets the prescaler
     * of the WDT. Status bits TO and PD are
     * set.
     *
     * Status affected: !TO, !PD (also so ein Dach Strich drauf)
     * @param instruction
     */
    public void instr_CLRWDT(int instruction) {

    }

    /**
     * Go to address
     *
     * GOTO is an unconditional branch. The
     * eleven bit immediate value is loaded
     * into PC bits <10:0>. The upper bits of
     * PC are loaded from PCLATH<4:3>.
     * GOTO is a two cycle instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_GOTO(int instruction) {

    }

    /**
     * Inclusive OR literal with W
     *
     * The contents of the W register is
     * OR’ed with the eight bit literal 'k'. The
     * result is placed in the W register.
     *
     * Status affected: Z
     * @param instruction
     */
    public void instr_IORLW(int instruction) {

    }

    /**
     * Move literal to W
     *
     * The eight bit literal ’k’ is loaded into W
     * register. The don’t cares will assemble
     * as 0’s.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_MOVLW(int instruction) {

    }

    /**
     * Return from interrupt
     *
     * Return from Interrupt. Stack is POPed
     * and Top of Stack (TOS) is loaded in the
     * PC. Interrupts are enabled by setting
     * Global Interrupt Enable bit, GIE
     * (INTCON<7>). This is a two cycle
     * instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_RETFIE(int instruction) {

    }

    /**
     * Return with literal in W
     *
     * The W register is loaded with the eight
     * bit literal ’k’. The program counter is
     * loaded from the top of the stack (the
     * return address). This is a two cycle
     * instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_RETLW(int instruction) {

    }

    /**
     * Return from Subroutine
     *
     * Return from subroutine. The stack is
     * POPed and the top of the stack (TOS)
     * is loaded into the program counter. This
     * is a two cycle instruction.
     *
     * Status affected: None
     * @param instruction
     */
    public void instr_RETURN(int instruction) {

    }

    /**
     * Go into standby mode
     *
     * The power-down status bit, PD is
     * cleared. Time-out status bit, TO is
     * set. Watchdog Timer and its prescaler are cleared.
     * The processor is put into SLEEP
     * mode with the oscillator stopped. See
     * Section 14.8 for more details.
     *
     * Status affected: !TO, !PD
     * @param instruction
     */
    public void instr_SLEEP(int instruction) {

    }

    /**
     * Subtract W from literal
     *
     * The contents of W register is subtracted (2’s complement method) from the eight bit literal 'k'.
     * The result is placed in the W register.
     *
     * Status affected: C, DC, Z
     * @param instruction
     */
    public void instr_SUBLW(int instruction) {

    }

    /**
     * Exclusive OR literal with W
     *
     * Exclusive OR the contents of the W
     * register with contents of register 'f'. If 'd' is
     * 0 the result is stored in the W register. If 'd' is
     * 1 the result is stored back in register 'f'.
     *
     * Status affected: Z
     * @param instruction
     */
    public void instr_XORLW(int instruction) {

    }


    //GENERAL METHODS
    /**
     * SETS the carry flag in status register
     */
    private void set_C() {

    }

    /**
     * UNSETS the carry flag in status register
     */
    private void unset_C() {

    }

    /**
     * SETS the digit carry in status register
     */
    private void set_DC() {

    }

    /**
     * UNSETS the digit carry in status register
     */
    private void unset_DC() {}

    /**
     * SETS the zeroflag in status register
     */
    private void set_Z() {

    }

    /**
     * UNSETS the zeroflag in the status register
     */
    private void unset_Z() {

    }

    /**
     * SETS the rp0 bit in status register
     */
    private void set_RP0() {

    }

    /**
     * UNSETS the rp0 bit in the status register
     */
    private void unset_RP0() {

    }

    private void writeInW(int value) {
        W = value & upperZeroMask;
    }

    public int getWRegister() {
        return W;
    }

    public int getPCL() {
        return PC;
    }

}
