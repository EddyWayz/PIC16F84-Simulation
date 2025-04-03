package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox buttonsInclude;
    @FXML
    private VBox ioPinsInclude;

    // Öffentliche statische Variable für den PIC
    public static PIC pic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisiere den PIC – passe den Pfad an deine Bedürfnisse an
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }
        pic = new PIC(path);

        // Beispielhafter Button-Zugriff
        Button btnRun = (Button) buttonsInclude.lookup("#btnRun");
        if (btnRun != null) {
            btnRun.setOnAction(e -> {
                System.out.println("RUN Button wurde gedrückt!");
                // Weitere Logik hier
            });
        } else {
            System.out.println("btnRun wurde nicht gefunden!");
        }

        // Zugriff auf den LOAD-Button
        Button btnLoad = (Button) buttonsInclude.lookup("#btnLoad");
        if (btnLoad != null) {
            btnLoad.setOnAction(e -> {
                System.out.println("LOAD Button wurde gedrückt!");
                // Weitere Logik hier
            });
        } else {
            System.out.println("btnLoad wurde nicht gefunden!");
        }

        // Zugriff auf den CTOP-Button
        Button btnCTOP = (Button) buttonsInclude.lookup("#btnCTOP");
        if (btnCTOP != null) {
            btnCTOP.setOnAction(e -> {
                System.out.println("CTOP Button wurde gedrückt!");
                // Weitere Logik hier
            });
        } else {
            System.out.println("btnCTOP wurde nicht gefunden!");
        }

        // Zugriff auf den STOP-Button
        Button btnStop = (Button) buttonsInclude.lookup("#btnStop");
        if (btnStop != null) {
            btnStop.setOnAction(e -> {
                System.out.println("STOP Button wurde gedrückt!");
                // Weitere Logik hier
            });
        } else {
            System.out.println("btnStop wurde nicht gefunden!");
        }
    }
}
