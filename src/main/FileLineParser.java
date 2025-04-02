package main;

import java.io.*;
import java.util.ArrayList;

/**
 * Klasse zum Einlesen einer Datei in Zeilen.
 */
public class FileLineParser {
    private ArrayList<String> fileAsLines = new ArrayList<>();
    private File input;  // Als Feld speichern

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
     * @return ArrayList mit den Zeilen der Datei
     * @throws IOException
     */
    public ArrayList<String> parseFileToLines() throws IOException {
        if (!fileAsLines.isEmpty()) {
            // Datei wurde bereits eingelesen, also einfach zurückgeben
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
            throw new RuntimeException(e);
        }
    }
}
