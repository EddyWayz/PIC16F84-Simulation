package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class ButtonsController implements Initializable {

    public Button btnRun;
    public Button btnStep;
    public Button btnStop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Beispielhafter Button-Zugriff
        if (btnRun != null) {
            btnRun.setOnAction(e -> {
                System.out.println("RUN Button wurde gedrückt!");
                // Weitere Logik hier
            });
        } else {
            System.out.println("btnRun wurde nicht gefunden!");
        }

        // Zugriff auf den STEP-Button
        if (btnStep != null) {
            btnStep.setOnAction(e -> {
                MainController.pic.step();
                // Aktualisiere die Tabelle, indem du die statische Instanz verwendest
                if (TableLSTController.instance != null) {
                    TableLSTController.instance.refreshView();
                }
            });
        } else {
            System.out.println("btnStep wurde nicht gefunden!");
        }

        // Zugriff auf den STOP-Button
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
