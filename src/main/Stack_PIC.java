package main;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class to reassemble the hidden stack of the PIC
 */
public class Stack_PIC {
    private static final Logger LOGGER = Logger.getLogger(Stack_PIC.class.getName());
    private final int[] stack;
    private int stack_pointer;

    public Stack_PIC() {
        stack = new int[8];
        stack_pointer = 0;
    }

    /**
     * pushes a new address onto the stack
     * @param address that will be put onto the stack
     */
    public void push(int address) {
        try {
            stack[stack_pointer] = address;
            inc_SP();
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, String.format("Stack overflow bei Push Funktion: index %d", stack_pointer), e);
        }
    }

    /**
     * pops the element on top of the stack
     * @return the popped element
     */
    public int pop() {
        try {
            dec_SP();
            return stack[stack_pointer];
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, String.format("Stack underflow bei Pop Funktion: index %d", stack_pointer), e);
            return -1;
        }
    }

    /**
     * method to increment the stack pointer
     */
    private void inc_SP() {
        stack_pointer++;
        stack_pointer %= 8;
    }

    /**
     * method to decrement the stack pointer
     */
    private void dec_SP() {
        stack_pointer--;
        if(stack_pointer < 0) {
            stack_pointer = 7;
        }
    }

    /**
     * getter method for a value of the stack
     * @param index of an elem in the stack
     * @return integer of the index
     */
    public int getVal(int index) {
        try {
            return stack[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE,
                    String.format("Ungueltiger Stack-Index bei getVal Funktion: %d", index), e);
            return -1;
        }
    }

    public int getStack_pointer() {
        return stack_pointer;
    }
}
