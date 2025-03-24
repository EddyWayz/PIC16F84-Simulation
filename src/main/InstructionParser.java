package main;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class InstructionParser {
    ArrayList<Integer> program = new ArrayList<>();
    File input;
    ArrayList<String> fileAsLines;

    public InstructionParser(String path) {
        FileLineParser flParser = new FileLineParser(path);
        try {
            fileAsLines = flParser.parseFileToLines();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while parsing file");
        }
    }

    public ArrayList<Integer> parseLinesToInstructions() {
        String currentLine;
        for (int i = findFirstIndex(); i < fileAsLines.size(); i++) {
            currentLine = fileAsLines.get(i);
            int instruction = getInstruction(currentLine);
            if(instruction != 0) {
                program.add(instruction);
            }
        }
        return program;
    }

    private int findFirstIndex() {
        char current = fileAsLines.getFirst().charAt(0);
        int index = 0;

        while (current == ' ') {
            index++;
            current = fileAsLines.get(index).charAt(0);
        }
        return index;
    }

    private int getInstruction(String line) {
        String instruction = "";
        if (line.charAt(0) != ' ') {
            for (int i = 5; i <= 8; i++) {
                instruction += line.charAt(i);
            }
        } else {
            instruction = "0";
        }
        return Integer.parseInt(instruction, 16);
    }


}
