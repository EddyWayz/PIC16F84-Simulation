package main;

import main.tools.In;
import main.tools.Label_Lib;

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
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim3.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }


        PIC pic = new PIC(path);

        for(int x = 0; x < 17; x++) {
            pic.step();
            System.out.println("W: " + Integer.toHexString(pic.getW()));
            System.out.println("C: " + pic.memory.readBit(Label_Lib.STATUS, Label_Lib.carry) + " ");
            System.out.println("DC: " + pic.memory.readBit(Label_Lib.STATUS, Label_Lib.digitcarry) + " ");
            System.out.println("Z: " + pic.memory.readBit(Label_Lib.STATUS, Label_Lib.zeroflag) + " ");
            System.out.println("Wert1: " + Integer.toHexString(pic.memory.read(0xC)) + " ");
            System.out.println("Wert2: " + Integer.toHexString(pic.memory.read(0xD)) + " ");
            System.out.println("Erg: " + Integer.toHexString(pic.memory.read(0xE)) + " " + "\n");
        }

    }
}
