package main;

import main.cardgame.RAM;

import main.tools.BitOperator;
import main.tools.Instr_Lib;
import main.tools.Label_Lib;
import main.tools.Mask_Lib;


import java.util.ArrayList;


public class PIC {
    // special vars for computing instructions
    int address;
    boolean indirect;
    private int instruction;

    //memory for data and program
    public RAM memory;
    private final ArrayList<Integer> program;

    Stack_PIC stack;

    //special registers
    private int W;
    private int PC;



    public PIC(String path) {
        //INIT of special registers
        W = 0;
        PC = 0;

        //INIT of stack
        stack = new Stack_PIC();

        //new instance of RAM
        memory = new RAM();
        memory.powerOn_reset();
        //TODO INIT of all registers at Power On

        //Parse file to get the program
        InstructionParser instrParser = new InstructionParser(path);
        program = instrParser.parseLinesToInstructions();

        //TODO Link to IO Pins

    }

    /**
     * simunlates one line of assembly code
     */
    public void step() {
        instruction = fetch();
        //PC increment
        increment_PC();
        decode_n_execute();

        System.out.println("W: " + Integer.toHexString(getW()));
        System.out.println("Wert1: " + Integer.toHexString(memory.read(0xC)) + " ");
        System.out.println("Wert2: " + Integer.toHexString(memory.read(0xD)) + " ");
        System.out.print("DC: " + memory.readBit(Label_Lib.STATUS, Label_Lib.digitcarry) + " ");
        System.out.print("C: " + memory.readBit(Label_Lib.STATUS, Label_Lib.carry) + " ");
        System.out.println("Z: " + memory.readBit(Label_Lib.STATUS, Label_Lib.zeroflag) + " \n");



        //TODO: update IO Pins
    }

    /**
     * fetches the next instruction from the program at index of the program counter
     * increments the PC
     * @return the current instrcution that will be executed
     */
    private int fetch() {
        //get next instruction
        instruction = program.get(PC);
        return instruction;
    }

    /**
     * decodes and executes the next instruction
     */
    private void decode_n_execute() {
        //fetch special instruction with 14 bit code
        switch(instruction) {
            case Instr_Lib.NOP:
                instr_NOP();
                break;
            case Instr_Lib.CLRWDT:
                instr_CLRWDT();
                break;
            case Instr_Lib.RETFIE:
                instr_RETFIE();
                break;
            case Instr_Lib.RETURN:
                instr_RETURN();
                break;
            case Instr_Lib.SLEEP:
                instr_SLEEP();
                break;
        }

        //7 BIT INSTRUCTIONS
        int maskedInstr_7bit = instruction & 0x3F80;
        switch(maskedInstr_7bit) {
            case Instr_Lib.CLRF:
                instr_CLRF();
                break;
            case Instr_Lib.CLRW:
                instr_CLRW();
                break;
            case Instr_Lib.MOVWF:
                instr_MOVWF();
                break;
        }

        //6 BIT INSTRUCTIONS (Table order without instructions above)
        int maskedInstr_6bit = instruction & 0x3F00;
        switch(maskedInstr_6bit) {
            case Instr_Lib.ADDWF:
                instr_ADDWF();
                break;
            case Instr_Lib.ANDWF:
                instr_ANDWF();
                break;
            case Instr_Lib.COMF:
                instr_COMF();
                break;
            case Instr_Lib.DECF:
                instr_DECF();
                break;
            case Instr_Lib.DECFSZ:
                instr_DECFSZ();
                break;
            case Instr_Lib.INCF:
                instr_INCF();
                break;
            case Instr_Lib.INCFSZ:
                instr_INCFSZ();
                break;
            case Instr_Lib.IORWF:
                instr_IORWF();
                break;
            case Instr_Lib.MOVF:
                instr_MOVF();
                break;
            case Instr_Lib.RLF:
                instr_RLF();
                break;
            case Instr_Lib.RRF:
                instr_RRF();
                break;
            case Instr_Lib.SUBWF:
                instr_SUBWF();
                break;
            case Instr_Lib.SWAPF:
                instr_SWAPF();
                break;
            case Instr_Lib.XORWF:
                instr_XORWF();
                break;
            case Instr_Lib.BCF:
                instr_BCF();
                break;
            case Instr_Lib.BSF:
                instr_BSF();
                break;
            case Instr_Lib.BTFSC:
                instr_BTFSC();
                break;
            case Instr_Lib.BTFSS:
                instr_BTFSS();
                break;
            case Instr_Lib.ADDLW:
                instr_ADDLW();
                break;
            case Instr_Lib.ANDLW:
                instr_ANDLW();
                break;
            case Instr_Lib.CALL:
                instr_CALL();
                break;
            //CLRWDT see above
            case Instr_Lib.GOTO:
                instr_GOTO();
                break;
            case Instr_Lib.IORLW:
                instr_IORLW();
                break;
            case Instr_Lib.MOVLW:
                instr_MOVLW();
                break;
            //RETFIE see above
            case Instr_Lib.RETLW:
                instr_RETLW();
                break;
            case Instr_Lib.SUBLW:
                instr_SUBLW();
                break;
            case Instr_Lib.XORLW:
                instr_XORLW();
                break;
        }


    }

    //BYTE-ORIENTED FILE REGISTER OPERATIONS
    /**
     * Add W and f
     * Add the contents of the W register with the contents of register f.
     * If d is 0 the result is stored back in the W register
     * If d is 1 the result is stored back in register f
     * Status affected: C, DC, Z
     */
    private void instr_ADDWF() {
        // mask the address
        computeAddress(instruction);

        // get value of the register
        int value = memory.read_indirect(address, indirect);

        //check for digit carry
        memory.check_n_manipulate_DC(W, value);
        //check flags after computing
        int result = W + value;
        memory.check_n_manipulate_C(result);
        memory.check_n_manipulate_Z(result);

        //writes the result in the right location
        writeInMemoryDestinationBit_indirect(address, result, indirect);

        System.out.println("ADDWF");
    }

    /**
     * AND W with f
     * AND the W register with contents of register 'f'.
     * If 'd' is 0 the result is stored in the W register.
     * If 'd' is 1 the result is stored back in register 'f'.
     * Status affected: Z
     */
    private void instr_ANDWF() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        int result = W & value;
        memory.check_n_manipulate_Z(result);

        writeInMemoryDestinationBit_indirect(address, result, indirect);
        System.out.println("ANDWF");
    }

    /**
     * Clear f
     * The contents of register ’f’ are cleared
     * and the Z bit is set.
     * Status affected: Z
     */
    private void instr_CLRF() {
        computeAddress(instruction);

        memory.set_Z();
        writeInMemoryDestinationBit_indirect(address, 0, indirect);
        System.out.println("CLRF");
    }

    /**
     * Clear W
     * W register is cleared. Zero bit (Z) is set.
     * Status affected: Z
     */
    private void instr_CLRW() {
        writeInW(0);
        memory.set_Z();
        System.out.println("CLRW");
    }

    /**
     * Complement f
     * The contents of register ’f’ are complemented. If ’d’ is 0 the result is stored in W.
     * If ’d’ is 1 the result is stored back in
     * register ’f’.
     * Status affected: Z
     */
    private void instr_COMF() {
        computeAddress(instruction);

        //complement of the content of the register of given address
        int value = ~memory.read_indirect(address, indirect);
        memory.check_n_manipulate_Z(value);

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("COMF");
    }

    /**
     * Decrement f
     * Decrement contents of register ’f’. If ’d’ is 0 the
     * result is stored in the W register. If ’d’ is
     * 1 the result is stored back in register ’f’.
     * Status affected: Z
     */
    private void instr_DECF() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        value--;
        if(value < 0) {
            value = 255;
        }
        memory.check_n_manipulate_Z(value);
        writeInMemoryDestinationBit_indirect(address, value, indirect);

        System.out.println("DECF");
    }

    /**
     * Decrement f, Skip if 0
     * The contents of register ’f’ are decremented. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     * If the result is not 0, the next instruction, is
     * executed. If the result is 0, then a NOP is
     * executed instead making it a 2TCY instruction.
     * Status affected: None
     */
    private void instr_DECFSZ() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        value--;
        if(value < 0) {
            value = 255;
            increment_PC();
        }
        memory.check_n_manipulate_Z(value);
        writeInMemoryDestinationBit_indirect(address, value, indirect);

        System.out.println("DECFSZ");
    }

    /**
     * Increment f
     * The contents of register ’f’ are incremented.
     * If ’d’ is 0 the result is placed in the W register.
     * If ’d’ is 1 the result is placed back in register ’f’.
     * Status affected: Z
     */
    private void instr_INCF() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        value++;
        if(value > 255) {
            value = 0;
        }
        memory.check_n_manipulate_Z(value);
        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("INCF");
    }

    /**
     * Increment f, Skip if 0
     * The contents of register ’f’ are incremented. If ’d’ is 0 the result is placed in
     * the W register. If ’d’ is 1 the result is placed back in register ’f’.
     * If the result is not 0, the next instruction is executed.
     * If the result is 0, a NOP is executed instead making it a 2TCY instruction.
     * Status affected: None
     */
    private void instr_INCFSZ() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        value++;
        if(value > 255) {
            value = 0;
            increment_PC();
        }

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("INCFSZ");
    }

    /**
     * Inclusive OR W with f
     * Inclusive OR the W register with contents of register ’f’.
     * If ’d’ is 0 the result is placed in the W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     * Status affected: Z
     */
    private void instr_IORWF() {
        computeAddress(instruction);

        int result = W | memory.read_indirect(address, indirect);
        memory.check_n_manipulate_Z(result);

        writeInMemoryDestinationBit_indirect(address, result, indirect);
        System.out.println("IORWF");
    }

    /**
     * Move f
     * The contents of register f is moved to a destination dependant upon the status of d.
     * If d = 0, destination is W register. If d = 1, the destination is file register f itself.
     * d = 1 is useful to test a file register since status flag Z is affected.
     * Status affected: Z
     */
    private void instr_MOVF() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);

        memory.check_n_manipulate_Z(value);
        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("MOVF");
    }

    /**
     * Move W to f
     * Move data from W register to register
     * Status affected: None
     */
    private void instr_MOVWF() {
        computeAddress(instruction);

        writeInMemoryDestinationBit_indirect(address, W, indirect);
        System.out.println("MOVWF");
    }

    /**
     * No Operation
     * No operation.
     * Status affected: None
     */
    private void instr_NOP() {
        System.out.println("NOP");
    }

    /**
     * Rotate Left f through Carry
     * The contents of register ’f’ are rotated one bit to the left through the Carry Flag.
     * If ’d’ is 0 the result is placed in the W register. If ’d’ is 1 the result is stored
     * back in register ’f’.
     * Status affected: C
     */
    private void instr_RLF() {
        computeAddress(instruction);
        //get carry and value of f
        int value = memory.read_indirect(address, indirect);
        int carry = memory.get_C();
        int bit7 = BitOperator.getBit(value, 7);

        //left shift of one bit
        value = value << 1;
        //manipulate first bit corresponding to carry flag
        if(carry == 1) {
            value = BitOperator.setBit(value, 0);
        }

        //manipulate carry corresponding to MSb
        if(bit7 == 0) {
            memory.unset_C();
        } else {
            memory.set_C();
        }

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("RLF");
    }

    /**
     * Rotate Right f through Carry
     * The contents of register ’f’ are rotated one bit to the right through the Carry Flag.
     * If ’d’ is 0 the result is placed in the W register.
     * If ’d’ is 1 the result is placed back in register ’f’.
     * Status affected: C
     */
    private void instr_RRF() {
        computeAddress(instruction);
        //get carry flag and value of f
        int value = memory.read_indirect(address, indirect);
        int carry = memory.get_C();
        int bit0 = BitOperator.getBit(value, 0);

        //right shift of one bit
        value = value >> 1;
        //manipulate MSb corresponding to carry flag
        if(carry == 1) {
            value = BitOperator.setBit(value, 7);
        }

        //manipulate carry corresponding to LSb
        if(bit0 == 0) {
            memory.unset_C();
        } else {
            memory.set_C();
        }

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("RRF");
    }

    /**
     * Subtract W from f
     * Subtract (2’s complement method) contents of W register from register 'f'.
     * If 'd' is 0 the result is stored in the W register. If 'd' is 1 the
     * result is stored back in register 'f'.
     * Status affected: C, DC, Z
     */
    private void instr_SUBWF() {
        computeAddress(instruction);

        int value = memory.read_indirect(address, indirect);
        int result = value - W;

        memory.check_n_manipulate_Z(result);
        if(result >= 0) { // 0 or positive
            memory.set_C();
        } else { // negative
            //redundant because it will be masked later again
            result = result & Mask_Lib.LOWER8BIT_MASK;
            memory.unset_C();
        }

        int w_nibble = ~W;
        w_nibble = w_nibble & Mask_Lib.NIBBLE_MASK;
        //val_nibble = val_nibble;
        int val_nibble = value & Mask_Lib.NIBBLE_MASK;

        //set digit carry reversed
        if((val_nibble + w_nibble) + 1 > Mask_Lib.NIBBLE_MASK) {
            memory.set_DC();
        } else {
            memory.unset_DC();
        }

        writeInMemoryDestinationBit_indirect(address, result, indirect);
        System.out.println("SUBWF");
    }

    /**
     * Swap Nibbles in f
     * The upper and lower nibbles of contents of register 'f' are exchanged.
     * If 'd' is 0 the result is placed in W register.
     * If 'd' is 1 the result is placed in register 'f'.
     * Status affected: None
     */
    private void instr_SWAPF() {
        computeAddress(instruction);

        // get nibbles from f
        int value = memory.read_indirect(address, indirect);
        int lower_Nibble = value & Mask_Lib.NIBBLE_MASK;
        int upper_Nibble_tmp = value & Mask_Lib.UPPER_NIBBLE_MASK;

        int upper_Nibble = lower_Nibble << 4;
        lower_Nibble = upper_Nibble_tmp >> 4;

        // swap nibbles
        int result = lower_Nibble;
        result = result | upper_Nibble;

        writeInMemoryDestinationBit_indirect(address, result, indirect);
        System.out.println("SWAPF");
    }

    /**
     * Exclusive OR W with f
     * The contents of the W register are XOR’ed with the eight bit literal 'k'.
     * The result is placed in the W register.
     * Status affected: Z
     */
    private void instr_XORWF() {
        computeAddress(instruction);

        int result = W ^ memory.read_indirect(address, indirect);
        memory.check_n_manipulate_Z(result);

        writeInMemoryDestinationBit_indirect(address, result, indirect);
        System.out.println("XORWF");
    }

    //BIT-ORIENTED FILE REGISTER OPERATIONS
    /**
     * Bit Clear f
     * Bit ’b’ in register ’f’ is cleared
     * Status affected: None
     */
    private void instr_BCF() {
        computeAddress(instruction);

        int pos = instruction & Mask_Lib.LOWER8BIT_MASK;
        int value = memory.read_indirect(address, indirect);

        value = BitOperator.unsetBit(value, pos);

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("BCF");
    }

    /**
     * Bit Set f
     * Bit ’b’ in register ’f’ is set.
     * Status affected: None
     */
    private void instr_BSF() {
        computeAddress(instruction);

        int pos = instruction & Mask_Lib.LOWER8BIT_MASK;
        int value = memory.read_indirect(address, indirect);

        value = BitOperator.setBit(value, pos);

        writeInMemoryDestinationBit_indirect(address, value, indirect);
        System.out.println("BSF");
    }

    /**
     * Bit Test f, Skip if Clear
     * If bit ’b’ in register ’f’ is ’1’ then the next instruction is executed.
     * If bit ’b’, in register ’f’, is ’0’ then the next
     * instruction is discarded, and a NOP is executed instead, making this a 2TCY instruction.
     * Status affected: None
     */
    private void instr_BTFSC() {
        computeAddress(instruction);
        int pos = instruction & Mask_Lib.LOWER8BIT_MASK;
        int value = memory.read_indirect(address, indirect);

        int bit = BitOperator.getBit(value, pos);
        //skip if clear
        if(bit == 0) {
            increment_PC();
        }

        System.out.println("BTFSC");
    }

    /**
     * Bit Test, Skip if Set
     * If bit ’b’ in register ’f’ is ’0’ then the next instruction is executed.
     * If bit ’b’ is ’1’, then the next instruction is
     * discarded and a NOP is executed instead, making this a 2TCY instruction.
     * Status affected: None
     */
    private void instr_BTFSS() {
        computeAddress(instruction);
        int pos = instruction & Mask_Lib.LOWER8BIT_MASK;
        int value = memory.read_indirect(address, indirect);

        int bit = BitOperator.getBit(value, pos);
        //skip if set
        if(bit == 1) {
            increment_PC();
        }

        System.out.println("BTFSS");
    }

    //LITERAL AND CONTROL OPERATIONS
    /**
     * Add literal and W
     * The contents of the W register are
     * added to the eight bit literal ’k’ and the
     * result is placed in the W register.
     * Status affected: C, DC, Z
     */
    private void instr_ADDLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;
        int result = k + W;
        memory.check_n_manipulate_DC(k, W);
        memory.check_n_manipulate_C(result);
        memory.check_n_manipulate_Z(result);
        writeInW(result);
        System.out.println("ADDLW");
    }

    /**
     * AND literal with W
     * The contents of W register are
     * AND’ed with the eight bit literal 'k'. The
     * result is placed in the W register.
     * Status affected: Z
     */
    private void instr_ANDLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;

        int value = k & W;
        memory.check_n_manipulate_Z(value);

        writeInW(value);
        System.out.println("ANDLW");
    }

    /**
     * Call Subroutine
     * Call Subroutine. First, return address
     * (PC+1) is pushed onto the stack. The
     * eleven bit immediate address is loaded
     * into PC bits <10:0>. The upper bits of
     * the PC are loaded from PCLATH. CALL
     * is a two cycle instruction.
     * Status affected: None
     */
    private void instr_CALL() {
        int k11 = instruction & Mask_Lib.GOTO_CALL_MASK;
        stack.push(PC);
        pclath_3n4_ontoPC();
        PC = PC | k11;
        System.out.println("CALL");
    }

    /**
     * Clear Watchdog Timer
     * CLRWDT instruction resets the Watchdog Timer. It also resets the prescaler
     * of the WDT. Status bits TO and PD are
     * set.
     * Status affected: !TO, !PD (also so ein Dach Strich drauf)
     */
    private void instr_CLRWDT() {
        System.out.println("CLRWDT");
    }

    /**
     * Go to address
     * GOTO is an unconditional branch. The
     * eleven bit immediate value is loaded
     * into PC bits <10:0>. The upper bits of
     * PC are loaded from PCLATH<4:3>.
     * GOTO is a two cycle instruction.
     * Status affected: None
     */
    private void instr_GOTO() {
        int k11 = instruction & Mask_Lib.GOTO_CALL_MASK;
        pclath_3n4_ontoPC();
        PC = PC | k11;
        System.out.println("GOTO");
    }

    /**
     * Inclusive OR literal with W
     * The contents of the W register is
     * OR’ed with the eight bit literal 'k'. The
     * result is placed in the W register.
     * Status affected: Z
     */
    private void instr_IORLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;
        int result = k | W;
        memory.check_n_manipulate_Z(result);
        writeInW(result);
        System.out.println("IORLW");
    }

    /**
     * Move literal to W
     * The eight bit literal ’k’ is loaded into W
     * register. The don’t cares will assemble
     * as 0’s.
     * Status affected: None
     */
    private void instr_MOVLW() {
        writeInW(instruction & Mask_Lib.LITERAL_MASK);
        System.out.println("MOVLW");
    }

    /**
     * Return from interrupt
     * Return from Interrupt. Stack is POPed
     * and Top of Stack (TOS) is loaded in the
     * PC. Interrupts are enabled by setting
     * Global Interrupt Enable bit, GIE
     * (INTCON<7>). This is a two cycle
     * instruction.
     * Status affected: None
     */
    private void instr_RETFIE() {
        System.out.println("RETFIE");
    }

    /**
     * Return with literal in W
     * The W register is loaded with the eight
     * bit literal ’k’. The program counter is
     * loaded from the top of the stack (the
     * return address). This is a two cycle
     * instruction.
     * Status affected: None
     */
    private void instr_RETLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;
        writeInW(k);
        PC = stack.pop();
        System.out.println("RETLW");
    }

    /**
     * Return from Subroutine
     * Return from subroutine. The stack is
     * POPed and the top of the stack (TOS)
     * is loaded into the program counter. This
     * is a two cycle instruction.
     * Status affected: None
     */
    private void instr_RETURN() {
        PC = stack.pop();
        System.out.println("RETURN");
    }

    /**
     * Go into standby mode
     * The power-down status bit, PD is
     * cleared. Time-out status bit, TO is
     * set. Watchdog Timer and its prescaler are cleared.
     * The processor is put into SLEEP
     * mode with the oscillator stopped. See
     * Section 14.8 for more details.
     * Status affected: !TO, !PD
     */
    private void instr_SLEEP() {
        System.out.println("SLEEP");
    }

    /**
     * Subtract W from literal
     * The contents of W register is subtracted (2’s complement method) from the eight bit literal 'k'.
     * The result is placed in the W register.
     * Status affected: C, DC, Z
     */
    private void instr_SUBLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;
        int result = k - W;
        // setting of carry flag is inverted due to a hardware error
        // page 16
        if(result > 0) {
            memory.set_C();
        } else {
            memory.unset_C();
        }

        // setting of digit carry flag is inverted due to a hardware error
        int nibbleK = k & Mask_Lib.NIBBLE_MASK;
        int nibbleW = W & Mask_Lib.NIBBLE_MASK;
        if((nibbleK - nibbleW) > 0) {
            memory.unset_DC();
        } else {
            memory.set_DC();
        }
        memory.check_n_manipulate_Z(result);
        writeInW(result);
        System.out.println("SUBLW");
    }

    /**
     * Exclusive OR literal with W
     * The contents of the W register are
     * XOR’ed with the eight bit literal 'k'.
     * The result is placed in the W register.
     * Status affected: Z
     */
    private void instr_XORLW() {
        int k = instruction & Mask_Lib.LITERAL_MASK;
        int result = k ^ W;
        memory.check_n_manipulate_Z(result);
        writeInW(result);
        System.out.println("XORLW");
    }


    //GENERAL METHODS
    /**
     * increments the programm counter and writes the lower 8 Bit into the PCL register
     */
    private void increment_PC() {
        PC++;
        if(PC >= 1024) {
            PC = 0;
        }
        int pcl_val = PC & Mask_Lib.LOWER8BIT_MASK;
        memory.write(Label_Lib.PCL, pcl_val);
    }

    /**
     * puts the 3rd and 4th bit of the pclath register at the 12th and 11th bit of the PC
     */
    private void pclath_3n4_ontoPC() {
        // puts the third and fourth bit of pclath into the PC
        int pclath = memory.read(Label_Lib.PCLATH);
        pclath = pclath & Mask_Lib.PCLATH_3_4_MASK;
        pclath = pclath << 8;
        PC = pclath;
    }

    /**
     * overloaded method: writes in the memory depending on the destination bit with an indirect address possible
     *
     * @param address of the register that will be written into
     * @param value   that will be stored
     */
    private void writeInMemoryDestinationBit_indirect(int address, int value, boolean indirect) {
        int destination = BitOperator.getBit(instruction, 7);
        if(destination == 0) {
            writeInW(value);
        } else {
            if(indirect) {
                memory.write_indirect(address, value);
            } else {
                memory.write(address, value);
            }
        }
    }

    /**
     * writes the given value into the W register after masking it
     * @param value that will be written in W
     */
    private void writeInW(int value) {
        W = value & Mask_Lib.LOWER8BIT_MASK;
    }

    public int getW() {
        return W;
    }

    /**
     * checks if an indirect address has been used. If so the new address will be stored in the global variable address
     * and the boolean variable will be true
     * @param instruction of the current cycle
     */
    private void computeAddress(int instruction) {
        address = instruction & Mask_Lib.ADDRESS_MASK;
        indirect = (address == 0);
        if(address == 0) {
            address = memory.read(Label_Lib.FSR);
        }
    }
}
