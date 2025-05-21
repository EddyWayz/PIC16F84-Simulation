package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.libraries.Instr_Lib;

/**
 * class to parse the file as line to the hex instructions
 */
public class InstructionParser {
    private static final Logger LOGGER = Logger.getLogger(InstructionParser.class.getName());
    //list the instructions will be saved to
    ArrayList<Integer> program = new ArrayList<>();
    ArrayList<String> fileAsLines;

    public InstructionParser(String path) {
        //parser to parse file as lines
        FileLineParser flParser = new FileLineParser(path);
        try {
            fileAsLines = flParser.parseFileToLines();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error beim Einlesen: " + path, e);
        }
        if (fileAsLines == null || fileAsLines.isEmpty()) {
            LOGGER.log(Level.WARNING, "Keine Zeilen eingelesen oder Datei leer: {0}", path);
        }
    }

    public ArrayList<Integer> parseLinesToInstructions() {
        String currentLine;
        //gets the hex code and adds it to the program list
        for (int i = findFirstIndex(); i < fileAsLines.size(); i++) {
            try {
                currentLine = fileAsLines.get(i);
                //insertNOPS(currentLine, i);
                int instruction = getInstruction(currentLine);
                if (instruction != -1 && program.size() < 1024) {
                    program.add(instruction);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, String.format("Ungueltige Instruktion in Zeile %d: %s", i, fileAsLines.get(i)), e);
            }
        }
        return program;
    }

    @SuppressWarnings("unsued")
    private void insertNOPS(String line, int i) {
        int index = line.indexOf("org") + 1;

        if (index != -1) {
            index++;
            String amountString = "";
            while (Character.digit(line.charAt(index), 16) != -1) {
                amountString += line.charAt(index);
                index++;
            }

            System.out.println("Amount of inserted NOPs: " + amountString);

            int amount = Integer.parseInt(amountString, 16) - i - 1;

            for (int x = 0; x < amount; x++) {
                program.add(Instr_Lib.NOP);
            }
        }

    }

    /**
     * finds the first index of an LST file with an instruction
     *
     * @return index
     */
    private int findFirstIndex() {
        char current = fileAsLines.getFirst().charAt(0);
        int index = 0;
        while (current == ' ') {
            index++;
            current = fileAsLines.get(index).charAt(0);
        }
        return index;
    }

    /**
     * parses an instruction out of given string (line of LST file)
     *
     * @param line string containing the instruction
     * @return instruction as integer
     */
    private int getInstruction(String line) {
        if (line.charAt(0) != ' ') {
            StringBuilder instruction = new StringBuilder();
            for (int i = 5; i <= 8; i++) {
                instruction.append(line.charAt(i));
            }
            try {
                return Integer.parseInt(instruction.toString(), 16);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Fehler beim Parsen der Instruktion: " + instruction, e);
                return -1;
            }
        } else {
            return -1;
        }
    }


}
