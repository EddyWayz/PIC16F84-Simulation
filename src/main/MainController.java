package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox ioPinsInclude;

    // Öffentliche statische Variable für den PIC
    private static PIC pic;

    public static IO_PINS_Controller ioPinsController;  // Referenz setzen bei initialize()
    public static TableLSTController tableLSTController;
    public static RAMTabsLSTController ramTabsLSTController;
    public static PIC getStaticPic() {
        return pic;
    }
    public static void updatePIC(String newPath) {
        pic = new PIC(newPath);
        System.out.println("Neuer PIC geladen: " + newPath);

        if (ioPinsController != null) {
            ioPinsController.updatePIC(pic);
        } else {
            System.out.println("⚠ IO_PINS_Controller ist noch nicht initialisiert!");
        }

        if(ramTabsLSTController != null){
            ramTabsLSTController.updatePIC(pic);
        } else {
            System.out.println("⚠ RAMTabsLSTController ist noch nicht initialisiert!");
        }
    }


    static {
        // Bestimme den Pfad abhängig vom Betriebssystem
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            path = "/default/path/to/file"; // Füge einen sinnvollen Default-Pfad ein
            System.out.println("Running on another OS: " + osName);
        }
        pic = new PIC(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Weitere Initialisierungen, falls nötig
    }
}
