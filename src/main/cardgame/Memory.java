package main.cardgame;

public interface Memory {
    int read(int address);
    int readBit(int address, int position);
    void write(int address, int value);
    void writeBit(int address, int position);
    int getBit(int address, int position);

}
