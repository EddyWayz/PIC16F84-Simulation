package main;

import main.BitOperator.*;

public class RAM {
    int[] memory;
    int bank;
    int upperZeroMask = 0b11111111;

    public RAM (int bank) {
        memory = new int[128];
        this.bank = bank;
    }

    public int getRegister(int address) {
        return memory[address];
    }

    public int read(int address) {
        return memory[address];
    }

    public int readBit(int address, int position) {
        return BitOperator.getBit(memory[address], position);
    }

    public void write(int address, int value) {
        value = value & upperZeroMask;
        memory[address] = value;
    }

    public void writeBit(int address, int position) {
        int tmp_value = read(address);
        BitOperator.setBit(tmp_value, position);
        write(address, tmp_value);
    }


}
