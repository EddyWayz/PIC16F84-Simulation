package main;

import java.io.*;
import java.util.ArrayList;

public class FileLineParser {
    ArrayList<String> fileAsLines = new ArrayList<>();
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

    public ArrayList<String> parseFileToLines(){
        String line;
        try {
            //if(bufferReader.ready()) {
                while((line = bufferReader.readLine()) != null) {
                    fileAsLines.add(line);
                    System.out.print("line read");
                }
                bufferReader.close();
            //}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileAsLines;
    }

    public void printFile() {
        for(String line : fileAsLines) {
            System.out.println(line);
        }
    }


}
