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
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim2.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }


        PIC pic = new PIC(path);

        pic.decode_n_execute(0x3e25);
        for(int x = 0; x < 12; x++) {
            pic.step();
            System.out.println("Status: " + Integer.toBinaryString(pic.memory.read(3)));
            System.out.println("W: " + Integer.toHexString(pic.getW()) + "\n");
        }

    }
}
