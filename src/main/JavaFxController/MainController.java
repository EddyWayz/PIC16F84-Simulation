package main.JavaFxController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import main.PIC;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private VBox ioPinsInclude;

    // Öffentliche statische Variable für den PIC
    private static PIC pic;

    public static PortController ioPinsController;
    public static TableLSTController tableLSTController;
    public static RAMTabsLSTController ramTabsLSTController;
    public static RegisterController registerController;
    public static ButtonsController buttonsController;
    public static StackController stackController;
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
        if (buttonsController != null) {
            buttonsController.updatePIC(pic);
        }
        else {
            System.err.println("⚠ ButtonsController ist noch nicht initialisiert!");
        }
        if(registerController != null){
            registerController.updatePIC(pic);
        } else{
            System.out.println("⚠ RegisterController ist noch nicht initialisiert!");
        }
        if(tableLSTController != null){
            tableLSTController.reloadTable(newPath);
        } else{
            System.out.println("⚠ TableLSTController ist noch nicht initialisiert!");
        }
        if(stackController != null) {
            //stackController.reloadTable(newPath);
        } else {
            System.out.println("⚠ StackLSTController ist noch nicht initialisiert!");
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
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
