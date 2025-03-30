package main;

import java.util.ArrayList;

public class InstructionLibrary {
    private ArrayList<Instruction> instructions;

    public InstructionLibrary() {
        instructions = new ArrayList<Instruction>(35);

        //BYTE-ORIENTED FILE REGISTER OPERATIONS
        instructions.add(new Instruction("ADDWF", 0x0700));
        instructions.add(new Instruction("ANDWF", 0x0500));
        instructions.add(new Instruction("CLRF", 0x0180));
        instructions.add(new Instruction("CLRW", 0x0100));
        instructions.add(new Instruction("COMF", 0x0900));
        instructions.add(new Instruction("DECF", 0x0300));
        instructions.add(new Instruction("DECFSZ", 0x0B00));
        instructions.add(new Instruction("INCF", 0x0A00));
        instructions.add(new Instruction("INCFSZ", 0x0F00));
        instructions.add(new Instruction("IORWF", 0x0400));
        instructions.add(new Instruction("MOVF", 0x0800));
        instructions.add(new Instruction("MOVWF", 0x0080));
        instructions.add(new Instruction("NOP", 0x0000));
        instructions.add(new Instruction("RLF", 0x0D00));
        instructions.add(new Instruction("RRF", 0x0C00));
        instructions.add(new Instruction("SUBWF", 0x0200));
        instructions.add(new Instruction("SWAPF", 0x0E00));
        instructions.add(new Instruction("XORWF", 0x0600));

        //BIT-ORIENTED FILE REGISTER OPERATIONS
        instructions.add(new Instruction("BCF", 0x1000));
        instructions.add(new Instruction("BSF", 0x1400));
        instructions.add(new Instruction("BTFSC", 0x1800));
        instructions.add(new Instruction("BTFSS", 0x1C00));

        //LITERAL AND CONTROL OPERATIONS
        instructions.add(new Instruction("ADDLW", 0x3E00));
        instructions.add(new Instruction("ANDLW", 0x3900));
        instructions.add(new Instruction("CALL", 0x2000));
        instructions.add(new Instruction("CLRWDT", 0x0064));
        instructions.add(new Instruction("GOTO", 0x2800));
        instructions.add(new Instruction("IORLW", 0x3800));
        instructions.add(new Instruction("MOVLW", 0x3000));
        instructions.add(new Instruction("RETFIE", 0x0009));
        instructions.add(new Instruction("RETLW", 0x3400));
        instructions.add(new Instruction("RETURN", 0x0008));
        instructions.add(new Instruction("SLEEP", 0x0063));
        instructions.add(new Instruction("SUBLW", 0x3C00));
        instructions.add(new Instruction("XORLW", 0x3A00));

    }

    /**
     * method to get a mask for a given instruction
     * @param instructionName
     * @return mask as an integer
     */
    public Instruction getMask(String instructionName) {
        for(Instruction i : instructions) {
            if(i.getName().equals(instructionName)) {
                return i;
            }
        }
        //this return should never be reached
        return null;
    }


}
