package main.cardgame;

/**
 * Interface to force classes to implement methods for manipulating
 */
public interface Memory {
    int read(int address);
    int readBit(int address, int position);
    void write(int address, int value);
    void setBit(int address, int position);
    void unsetBit(int address, int position);
    int getBit(int address, int position);
}
