package main;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private VBox buttonsInclude; // entspricht dem fx:id im fx:include der Buttons.fxml

    @FXML
    private VBox ioPinsInclude;

    @FXML
    public void initialize() {
        // Zugriff auf den RUN-Button (falls benötigt)
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
