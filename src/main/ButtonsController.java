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
    private volatile boolean stopButtonPushed = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (btnRun != null) {
            btnRun.setOnAction(e -> {
                if (TableLSTController.instance != null && MainController.pic != null) {
                    stopButtonPushed = false; // sicherstellen, dass beim Start zurückgesetzt wird

                    new Thread(() -> {
                        boolean breakpointErreicht = false;
                        while (!breakpointErreicht && !stopButtonPushed) {
                            MainController.pic.step();

                            Platform.runLater(this::refreshView);

                            String currentPC = MainController.pic.memory.convertPCLTo4BitsString();
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

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        // Optional: Wenn der Stopp durch Benutzer kam, etwas anzeigen
                        if (stopButtonPushed) {
                            System.out.println("Ausführung durch Stop-Button gestoppt.");
                        }
                    }).start();
                }
            });
        }

        if (btnStep != null) {
            btnStep.setOnAction(e -> {
                MainController.pic.step();
                refreshView();
            });
        }

        if (btnStop != null) {
            btnStop.setOnAction(e -> {
                stopButtonPushed = true;
            });
        }
    }

    private void refreshView() {
        if (TableLSTController.instance != null) {
            TableLSTController.instance.refreshView();
        }
        if (RAMTabsLSTController.instance != null) {
            RAMTabsLSTController.instance.refreshView();
        }
    }
}

