package main;

/**
 * class to represents one instruction
 */
public class Instruction {
    private int mask;
    //mnemonik of the instruction
    private String name;

    public Instruction(String instructionName, int maskedInstruction) {
        this.mask = maskedInstruction;
        this.name = instructionName;
    }

    public int getMask() {
        return mask;
    }

    public String getName() {
        return name;
    }
}
