package main.cardgame;

import main.tools.BitOperator;

public class Bank implements Memory {
    int[] memory;
    int UPPERZEROS_MASK = 0b11111111;

    public Bank() {
        memory = new int[128];
    }

    @Override
    public int read(int address) {
        return memory[address];
    }

    @Override
    public int readBit(int address, int position) {
        return BitOperator.getBit(memory[address], position);
    }

    @Override
    public void write(int address, int value) {
        value = value & UPPERZEROS_MASK;
        memory[address] = value;
    }

    @Override
    public void setBit(int address, int position) {
        int tmp_value = read(address);
        tmp_value = BitOperator.setBit(tmp_value, position);
        write(address, tmp_value);
    }

    @Override
    public void unsetBit(int address, int position) {
        int tmp_value = read(address);
        tmp_value = BitOperator.unsetBit(tmp_value, position);
        write(address, tmp_value);
    }

    @Override
    public int getBit(int address, int position) {
        return BitOperator.getBit(memory[address], position);
    }


}
