package main;

import java.io.*;
import java.util.ArrayList;

/**
 * class to parse a file as lines
 */
public class FileLineParser {
    private ArrayList<String> fileAsLines = new ArrayList<>();
    File input;
    BufferedReader bufferReader;

    /**
     * constructor for a new parser
     * @param path of the file that will be parsed
     */
    public FileLineParser(String path) {
        File input = new File(path);
        try {
            bufferReader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * parses a given file as lines
     * @return list of the parsed lines
     * @throws IOException
     */
    public ArrayList<String> parseFileToLines() throws IOException {
        String line;
        while((line = bufferReader.readLine()) != null) {
            fileAsLines.add(line);
        }
        bufferReader.close();
        return fileAsLines;
    }

    /**
     * method to print the parsed file to check if it was parsed correctly
     */
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
