package main;

import java.io.*;
import java.util.ArrayList;

public class FileLineParser {
    private ArrayList<String> fileAsLines = new ArrayList<>();
    File input;
    BufferedReader bufferReader;

    public FileLineParser(File input) {
        this.input = input;
        try {
            bufferReader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> parseFileToLines() throws IOException {
        String line;
        while((line = bufferReader.readLine()) != null) {
            fileAsLines.add(line);
        }
        bufferReader.close();
        return fileAsLines;
    }

    public void printFile() {
        try {
            parseFileToLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line : fileAsLines) {
            System.out.println(line);
        }
    }


}
