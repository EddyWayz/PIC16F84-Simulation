package main.cardgame;

import main.tools.BitOperator;
import main.libraries.Mask_Lib;

public class Bank implements Memory {
    int[] memory;


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
        value = value & Mask_Lib.LOWER8BIT_MASK;
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
