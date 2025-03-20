package main;

public class RAM {
    int[] memory = new int[128];
    int upperZeroMask = 0b11111111;

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int value) {
        value = value & upperZeroMask;
        memory[address] = value;
    }
}
