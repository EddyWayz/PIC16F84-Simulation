package main.JavaFxController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import main.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonsController implements Initializable {
    private PIC pic;
    public Button btnRun;
    public Button btnStep;
    public Button btnStop;
    public Button btnMCLR;

    @FXML
    private CheckBox activateWatchdogCheckbox;
    private volatile boolean stopButtonPushed = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.buttonsController = this;
        this.pic = MainController.getStaticPic();

        if (btnRun != null) {
            btnRun.setOnAction(e -> {
                if (TableLSTController.instance != null && MainController.getStaticPic() != null) {
                    stopButtonPushed = false; // sicherstellen, dass beim Start zurückgesetzt wird

                    new Thread(() -> {
                        boolean breakpointErreicht = false;
                        while (!breakpointErreicht && !stopButtonPushed) {
                            MainController.getStaticPic().step();

                            Platform.runLater(this::refreshView);

                            String currentPC = MainController.getStaticPic().memory.convertPCLTo4BitsString();
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
                } else {
                    System.err.println("⚠ TableLSTController oder PIC nicht initialisiert!");
                }
            });
        }
        else {
            System.err.println("⚠ btnRun wurde nicht initialisiert!");
        }

        if (activateWatchdogCheckbox != null) {
            activateWatchdogCheckbox.setOnAction(e -> {
                pic.prescaler.WDT.active = activateWatchdogCheckbox.isSelected();
            });
        }
        else {
            System.err.println("⚠ activateWatchdogCheckbox wurde nicht initialisiert!");
        }

        if (btnStep != null) {
            btnStep.setOnAction(e -> {
                MainController.getStaticPic().step();
                refreshView();
            });
        }
        else {
            System.err.println("⚠ btnStep wurde nicht initialisiert!");
        }

        if (btnStop != null) {
            btnStop.setOnAction(e -> {
                stopButtonPushed = true;
            });
        }

        if(btnMCLR != null) {
            btnMCLR.setOnAction(e ->{
                MainController.getStaticPic().MCLR();
                refreshView();
            });
        }
        else {
            System.err.println("⚠ btnMCLR wurde nicht initialisiert!");
        }
    }

    private void refreshView() {
        try {
            if (TableLSTController.instance != null) {
                TableLSTController.instance.refreshView();
            }
            if (RAMTabsLSTController.instance != null) {
                RAMTabsLSTController.instance.buildUI();
            }
            if (RegisterController.instance != null) {
                RegisterController.instance.buildUI();
            }
            if (PortController.instance != null) {
                PortController.instance.buildUI();
            }

            if (StackController.instance != null) {
                StackController.instance.buildUI();
            }
        } catch (Exception e) {
            System.err.println("⚠ Fehler beim Aktualisieren der Ansicht: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updatePIC(PIC newPic) {
        boolean wasActive = activateWatchdogCheckbox != null && activateWatchdogCheckbox.isSelected();
        this.pic = newPic;
        if (activateWatchdogCheckbox != null) {
            pic.prescaler.WDT.active = wasActive;
            activateWatchdogCheckbox.setSelected(wasActive);
        }
        refreshView();
    }
}

