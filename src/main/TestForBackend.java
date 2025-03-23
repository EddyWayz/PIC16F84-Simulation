package main;

import java.io.File;
import java.util.ArrayList;

public class TestForBackend {
    public static void main(String[] args) {
        String path = "test_files\\TPicSim1.LST";


        File file = new File(path);
        FileLineParser flParser = new FileLineParser(file);
        flParser.printFile();

        InstructionParser instParser = new InstructionParser(file);
        ArrayList<Integer> list = instParser.parseLinesToInstructions();
        for (Integer i : list) {
            System.out.println(Integer.toHexString(i.intValue()));
        }


    }
}
