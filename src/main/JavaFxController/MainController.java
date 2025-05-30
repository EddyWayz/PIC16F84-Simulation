package main.JavaFxController;

import javafx.fxml.Initializable;
import main.PIC;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    // Öffentliche statische Variable für den PIC
    private static PIC pic;

    public static PortController portController;
    public static TableLSTController tableLSTController;
    public static RAMTabsController ramTabsController;
    public static RegisterController registerController;
    public static ButtonsController buttonsController;
    public static StackController stackController;

    /**
     * Retrieves the static PIC instance.
     *
     * @return The current static PIC instance.
     */
    public static PIC getStaticPic() {
        return pic;
    }

    /**
     * Updates the static PIC instance with a new path and notifies all controllers.
     * <p>
     * This method creates a new PIC instance using the provided path and updates
     * all associated controllers with the new PIC instance. If a controller is not
     * initialized, a warning message is printed to the console.
     *
     * @param newPath The file path to initialize the new PIC instance.
     */
    public static void updatePIC(String newPath) {
        pic = new PIC(newPath);
        LOGGER.log(Level.INFO, "Neuer PIC geladen: {0}", newPath);

        if (portController != null) {
            portController.updatePIC(pic);
        } else {
            System.out.println("IO_PINS_Controller ist noch nicht initialisiert!");
        }
        if(ramTabsController != null){
            ramTabsController.updatePIC(pic);
        } else {
            System.out.println("RAMTabsController ist noch nicht initialisiert!");
        }
        if (buttonsController != null) {
            buttonsController.updatePIC(pic);
        }
        else {
            System.err.println("ButtonsController ist noch nicht initialisiert!");
        }
        if(registerController != null){
            registerController.updatePIC(pic);
        } else{
            System.out.println("RegisterController ist noch nicht initialisiert!");
        }
        if(tableLSTController != null){
            tableLSTController.reloadTable(newPath);
        } else{
            System.out.println("TableLSTController ist noch nicht initialisiert!");
        }
        if(stackController != null) {
            stackController.updatePIC(pic);
        } else {
            System.out.println("StackLSTController ist noch nicht initialisiert!");
        }
    }


    static {
        // Bestimme den Pfad abhängig vom Betriebssystem
        String path;
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
