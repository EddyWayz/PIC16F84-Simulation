package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class to parse the file as line to the hex instructions
 */
public class InstructionParser {
    //list the instructions will be saved to
    ArrayList<Integer> program = new ArrayList<>();
    File input;
    ArrayList<String> fileAsLines;

    public InstructionParser(String path) {
        //parser to parse file as lines
        FileLineParser flParser = new FileLineParser(path);
        try {
            fileAsLines = flParser.parseFileToLines();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while parsing file");
        }
        if (fileAsLines == null || fileAsLines.isEmpty()) {
            System.err.println("⚠ Keine Zeilen eingelesen oder Datei leer: " + path);
        }
    }

    public ArrayList<Integer> parseLinesToInstructions() {
        String currentLine;
        //gets the hex code and adds it to the program list
        for (int i = findFirstIndex(); i < fileAsLines.size(); i++) {
            try {
                currentLine = fileAsLines.get(i);
                int instruction = getInstruction(currentLine);
                if (instruction != -1 && program.size() < 1024) {
                    program.add(instruction);
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠ Ungültige Instruktion in Zeile " + i + ": " + fileAsLines.get(i));
            }
        }
        return program;
    }

    /**
     * finds the first index of an LST file with a instruction
     * @return index
     */
    private int findFirstIndex() {
        char current = fileAsLines.getFirst().charAt(0);
        int index = 0;
        try {
            while (current == ' ') {
                index++;
                current = fileAsLines.get(index).charAt(0);
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("⚠ findFirstIndex: Datei nicht wie erwartet formatiert.");
            return 0;
        }
        return index;
    }

    /**
     * parses a instruction out of given string (line of LST file)
     * @param line
     * @return instruction as integer
     */
    private int getInstruction(String line) {
        String instruction = "";
        if (line.charAt(0) != ' ') {
            for (int i = 5; i <= 8; i++) {
                instruction += line.charAt(i);
            }
        } else {
            instruction = "-1";
        }
        return Integer.parseInt(instruction, 16);
    }


}
