package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

import java.io.*;
import java.util.ArrayList;

/**
 * Diese Klasse liest eine Datei zeilenweise ein.
 * Zusätzlich ist hier die DataRow-Klasse integriert,
 * die eine Zeile (bzw. Blöcke einer Zeile) der LST-Datei modelliert.
 */
public class FileLineParser {
    private ArrayList<String> fileAsLines = new ArrayList<>();
    private File input;

    /**
     * Konstruktor: Erstellt einen Parser für die angegebene Datei.
     * @param path Pfad zur Datei
     */
    public FileLineParser(String path) {
        input = new File(path);
    }

    /**
     * Liest die Datei zeilenweise ein und speichert das Ergebnis.
     * Wird die Methode erneut aufgerufen, wird der bereits eingelesene Inhalt zurückgegeben.
     *
     * @return ArrayList mit den Zeilen der Datei
     * @throws IOException
     */
    public ArrayList<String> parseFileToLines() throws IOException {
        if (!input.exists()) {
            System.err.println("⚠ Datei nicht gefunden: " + input.getPath());
            return fileAsLines;
        }
        if (!fileAsLines.isEmpty()) {
            // Datei wurde bereits eingelesen
            return fileAsLines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileAsLines.add(line);
            }
        }
        return fileAsLines;
    }

    /**
     * Gibt den Inhalt der Datei zeilenweise auf der Konsole aus.
     */
    public void printFile() {
        try {
            ArrayList<String> lines = parseFileToLines();
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("⚠ Fehler beim Ausgeben der Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode parst eine übergebene Zeile in die jeweiligen Blöcke und gibt ein DataRow-Objekt zurück.
     * Somit hast du von überall Zugriff auf alle Blöcke über die entsprechenden Getter-Methoden.
     *
     * @param line Eine Zeile aus der LST-Datei
     * @return Eine neu erstellte DataRow mit den extrahierten Blöcken
     */
    public static DataRow parseLineToDataRow(String line) {
        String block0 = "";
        String block1 = line.substring(0, 4).trim();
        String block2 = line.substring(5, 10).trim();
        String block3 = line.substring(20, 25).trim();
        String block4 = line.substring(27, 36).trim();
        String block5 = "";
        String block6 = "";
        if (line.length() > 36) {
            if (line.charAt(36) != ';') {
                if (line.contains(";")) {
                    block5 = line.substring(36, line.indexOf(';')).trim();
                } else {
                    block5 = line.substring(36).trim();
                }
            }
            if (line.contains(";")) {
                block6 = line.substring(line.indexOf(';')).trim();
            }
        }
        return new DataRow(block0, block1, block2, block3, block4, block5, block6);
    }

    /**
     * Die DataRow-Klasse modelliert eine einzelne Zeile der LST-Datei.
     * Sie beinhaltet String-Properties für die einzelnen Blöcke sowie eine BooleanProperty,
     * die z.B. für einen Breakpoint-Zustand genutzt werden kann.
     */
    public static class DataRow {
        private final SimpleStringProperty block0;
        private final SimpleStringProperty block1;
        private final SimpleStringProperty block2;
        private final SimpleStringProperty block3;
        private final SimpleStringProperty block4;
        private final SimpleStringProperty block5;
        private final SimpleStringProperty block6;

        // Optionale Property für den Breakpoint-Zustand
        private boolean breakpointActive = false;

        public DataRow(String block0, String block1, String block2, String block3, String block4, String block5, String block6) {
            this.block0 = new SimpleStringProperty(block0);
            this.block1 = new SimpleStringProperty(block1);
            this.block2 = new SimpleStringProperty(block2);
            this.block3 = new SimpleStringProperty(block3);
            this.block4 = new SimpleStringProperty(block4);
            this.block5 = new SimpleStringProperty(block5);
            this.block6 = new SimpleStringProperty(block6);
        }

        // Getter, Setter und Property-Methoden für block0
        public String getBlock0() { return block0.get(); }
        public void setBlock0(String value) { block0.set(value); }
        public SimpleStringProperty block0Property() { return block0; }

        // Getter, Setter und Property-Methoden für block1
        public String getBlock1() { return block1.get(); }
        public void setBlock1(String value) { block1.set(value); }
        public SimpleStringProperty block1Property() { return block1; }

        // Getter, Setter und Property-Methoden für block2
        public String getBlock2() { return block2.get(); }
        public void setBlock2(String value) { block2.set(value); }
        public SimpleStringProperty block2Property() { return block2; }

        // Getter, Setter und Property-Methoden für block3
        public String getBlock3() { return block3.get(); }
        public void setBlock3(String value) { block3.set(value); }
        public SimpleStringProperty block3Property() { return block3; }

        // Getter, Setter und Property-Methoden für block4
        public String getBlock4() { return block4.get(); }
        public void setBlock4(String value) { block4.set(value); }
        public SimpleStringProperty block4Property() { return block4; }

        // Getter, Setter und Property-Methoden für block5
        public String getBlock5() { return block5.get(); }
        public void setBlock5(String value) { block5.set(value); }
        public SimpleStringProperty block5Property() { return block5; }

        // Getter, Setter und Property-Methoden für block6
        public String getBlock6() { return block6.get(); }
        public void setBlock6(String value) { block6.set(value); }
        public SimpleStringProperty block6Property() { return block6; }

        // Methoden zum Verwalten der Breakpoint-Property
        public boolean getBreakpointActive() { return breakpointActive; }
        public void toggleBreakpointActive() { breakpointActive = !breakpointActive;}
    }
}
