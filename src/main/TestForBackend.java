package main;

import java.io.File;

public class TestForBackend {
    public static void main(String[] args) {
        String path = "test_files\\TPicSim1.LST";


        File file = new File(path);
        System.out.println(path);
        System.out.println(file.getAbsolutePath());
        FileLineParser flParser = new FileLineParser(file);
        flParser.printFile();


    }
}
