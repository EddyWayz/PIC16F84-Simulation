package main.JavaFxController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import main.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ButtonsController implements Initializable {
    @FXML
    public Button btnRun;
    @FXML
    public Button btnStep;
    @FXML
    public Button btnStop;
    @FXML
    public Button btnMCLR;
    @FXML
    private Slider speedSlider;
    @FXML
    private CheckBox activateWatchdogCheckbox;

    private static final Logger LOGGER = Logger.getLogger(ButtonsController.class.getName());
    private PIC pic;
    private volatile boolean stopButtonPushed = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.buttonsController = this;
        this.pic = MainController.getStaticPic();

        if (btnRun != null) {
            btnRun.setOnAction(_ -> {
                if (TableLSTController.instance != null && MainController.getStaticPic() != null) {
                    stopButtonPushed = false;

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
                                LOGGER.log(Level.INFO, "Breakpoint erreicht an PC:" + currentPC);
                            }
                            double stepsPerSec = speedSlider.getValue();
                            // Wie lange schlafen, um auf stepsPerSec Schritte pro Sekunde zu kommen:
                            long delay = (long)(1000.0 / stepsPerSec);
                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException e) {
                                LOGGER.log(Level.SEVERE, "Thread unterbrochen beim Sleep", e);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }).start();
                } else {
                    LOGGER.log(Level.SEVERE, "TableLSTController oder PIC wurde nicht initialisiert!");
                }
            });
        }
        else {
            LOGGER.log(Level.WARNING, "btnRun wurde nicht initialisiert!");
            return;
        }

        if (activateWatchdogCheckbox != null) {
            activateWatchdogCheckbox.setOnAction(_ -> {
                pic.prescaler.WDT.active = activateWatchdogCheckbox.isSelected();
            });
        }
        else {
            LOGGER.log(Level.WARNING, "activateWatchdogCheckbox wurde nicht initialisiert!");
        }

        if (btnStep != null) {
            btnStep.setOnAction(_ -> {
                MainController.getStaticPic().step();
                refreshView();
            });
        }
        else {
            LOGGER.log(Level.WARNING, "btnStep wurde nicht initialisiert.");
        }

        if (btnStop != null) {
            btnStop.setOnAction(_ -> {
                stopButtonPushed = true;
            });
        } else {
            LOGGER.log(Level.WARNING, "btnStop wurde nicht initialisiert!");
        }

        if(btnMCLR != null) {
            btnMCLR.setOnAction(_ ->{
                MainController.getStaticPic().MCLR();
                refreshView();
            });
        }
        else {
            LOGGER.log(Level.WARNING, "btnMCLR wurde nicht initialisiert!");
        }
    }

    private void refreshView() {
        try {
            if (TableLSTController.instance != null) {
                TableLSTController.instance.refreshView();
            }
            if (RAMTabsController.instance != null) {
                RAMTabsController.instance.buildUI();
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
            LOGGER.log(Level.SEVERE, "Fehler beim Aktualisieren der Ansicht. refreshView Funktion fehlgeschlagen.", e);
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

