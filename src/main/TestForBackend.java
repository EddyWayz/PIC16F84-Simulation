package main;

import main.tools.In;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class to test backend functions
 */
public class TestForBackend {
    public static void main(String[] args) {
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }

        InstructionParser instParser = new InstructionParser(path);
        ArrayList<Integer> list = instParser.parseLinesToInstructions();
        PIC pic = new PIC(path);
        for (Integer i : list) {
            System.out.println(Integer.toHexString(i.intValue()));
            pic.step();
            System.out.println("Status: " + Integer.toBinaryString(pic.memory.read(3)) + "\n");
        }
/*
        //testing of decoder method
        PIC pic = new PIC("C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST");

        String tmp;
        do {
            tmp = In.readString("exit or input: ");
            pic.decode_n_execute(Integer.parseInt(tmp, 16));
        } while (!tmp.equals("exit"));

*/



    }
}
