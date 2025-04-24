package main;

import main.libraries.Label_Lib;
import main.libraries.register_libraries.STATUS_lib;

/**
 * class to test backend functions
 */
public class TestForBackend {
    public static void main(String[] args) {
        try {
            String path = "";
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim4.LST";
                System.out.println("Running on Windows");
            } else if (osName.contains("mac")) {
                path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
                System.out.println("Running on Mac");
            } else {
                System.out.println("Running on another OS: " + osName);
            }

            if (path == null || path.isEmpty()) {
                System.err.println("⚠ Kein gültiger Pfad für LST-Datei gesetzt.");
                return;
            }

            PIC pic = new PIC(path);

            for(int x = 0; x < 0x17; x++) {
                pic.step();
                System.out.println("W: " + Integer.toHexString(pic.getW()));
                System.out.println("Wert1: " + Integer.toHexString(pic.memory.read(0xC)) + " ");
                System.out.println("Wert2: " + Integer.toHexString(pic.memory.read(0xD)) + " ");
                System.out.print("C: " + pic.memory.readBit(Label_Lib.STATUS, STATUS_lib.carry) + " ");
                System.out.print("DC: " + pic.memory.readBit(Label_Lib.STATUS, STATUS_lib.digitcarry) + " ");
                System.out.println("Z: " + pic.memory.readBit(Label_Lib.STATUS, STATUS_lib.zeroflag) + " \n");
            }
        } catch (Exception e) {
            System.err.println("⚠ Fehler in TestForBackend.main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
