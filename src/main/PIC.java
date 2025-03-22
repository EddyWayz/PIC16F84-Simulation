package main;

import java.util.ArrayList;

public class PIC {
    RAM bank0;
    RAM bank1;
    int wRegister;
    int PCL;
    ArrayList<Integer> program = new ArrayList<>();


    public PIC() {
        bank0 = new RAM();
        bank1 = new RAM();
        wRegister = 0;
        PCL = 0;

        //TODO Link to IO Pins
        //TODO Link to Instruction Decoder
        //TODO Stack init
        //TODO: get program from Instruction Parser



    }


}
