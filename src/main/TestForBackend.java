package main;

import java.io.File;
import java.util.ArrayList;

public class TestForBackend {
    public static void main(String[] args) {
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "Dein/Weg/test_files/TPicSim1.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }

        FileLineParser flParser = new FileLineParser(path);
        flParser.printFile();

        InstructionParser instParser = new InstructionParser(path);
        ArrayList<Integer> list = instParser.parseLinesToInstructions();
        for (Integer i : list) {
            System.out.println(Integer.toHexString(i.intValue()));
        }

    }
}
