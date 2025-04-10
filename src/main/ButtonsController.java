package main;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
                if (TableLSTController.instance != null && MainController.pic != null) {
                    // Starte einen neuen Thread, um den GUI-Thread nicht zu blockieren.
                    new Thread(() -> {
                        boolean breakpointErreicht = false;
                        while (!breakpointErreicht) {
                            // Einen Step ausführen
                            MainController.pic.step();

                            // Aktualisiere die Tabelle in den JavaFX-Thread
                            Platform.runLater(this::refreshView);

                            // Aktualisierte den aktuellen PC-Wert
                            String currentPC = MainController.pic.memory.convertPCLTo4BitsString();
                            // Finde die Zeile mit dem entsprechenden PC
                            FileLineParser.DataRow currentRow = null;
                            ObservableList<FileLineParser.DataRow> rows = TableLSTController.instance.tableViewLST.getItems();
                            for (FileLineParser.DataRow row : rows) {
                                if (row.getBlock1().equals(currentPC)) {
                                    currentRow = row;
                                    break;
                                }
                            }
                            if (currentRow != null && currentRow.getBreakpointActive()) {
                                breakpointErreicht = true;
                                System.out.println("Breakpoint erreicht an PC: " + currentPC);
                            }
                            // Kurze Pause einfügen, um z.B. eine hohe CPU-Last zu vermeiden
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                }
            });

        } else {
            System.out.println("btnRun wurde nicht gefunden!");
        }

        // Zugriff auf den STEP-Button
        if (btnStep != null) {
            btnStep.setOnAction(e -> {
                MainController.pic.step();
                // Aktualisiere die Tabelle, indem du die statische Instanz verwendest
                refreshView();
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

    private void refreshView() {
        if (TableLSTController.instance != null) {
            TableLSTController.instance.refreshView();
        }
        if (TableLSTController.instance != null) {
            RAMTabsLSTController.instance.refreshView();
        }
    }
}
