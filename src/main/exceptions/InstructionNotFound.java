package main.exceptions;

/**
 * Exception to be thrown in the decoder method of the PIC
 */
public class InstructionNotFound extends RuntimeException {
    public InstructionNotFound(String message) {
        super(message);
    }
    public InstructionNotFound() {
        super("Instruction not found. (Decoder)");
    }
}
