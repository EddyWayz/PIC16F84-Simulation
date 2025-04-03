package main;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import main.tools.Label_Lib;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;

public class TableLSTController implements Initializable {

    // Innere Klasse DataRow – Modelliert eine Zeile in der Tabelle.
    // Wir verwenden block0 als Platzhalter für das Breakpoint-Feld.
    public static class DataRow {
        private final SimpleStringProperty block0;
        private final SimpleStringProperty block1;
        private final SimpleStringProperty block2;
        private final SimpleStringProperty block3;
        private final SimpleStringProperty block4;
        private final SimpleStringProperty block5;
        private final SimpleStringProperty block6;

        // Optionale Property, um den Breakpoint-Zustand zu speichern (falls später benötigt)
        private final BooleanProperty breakpointActive = new SimpleBooleanProperty(false);

        public DataRow(String block0, String block1, String block2, String block3, String block4, String block5, String block6) {
            this.block0 = new SimpleStringProperty(block0);
            this.block1 = new SimpleStringProperty(block1);
            this.block2 = new SimpleStringProperty(block2);
            this.block3 = new SimpleStringProperty(block3);
            this.block4 = new SimpleStringProperty(block4);
            this.block5 = new SimpleStringProperty(block5);
            this.block6 = new SimpleStringProperty(block6);
        }
        public String getBlock0() { return block0.get(); }
        public void setBlock0(String value) { block0.set(value); }
        public String getBlock1() { return block1.get(); }
        public void setBlock1(String value) { block1.set(value); }
        public String getBlock2() { return block2.get(); }
        public void setBlock2(String value) { block2.set(value); }
        public String getBlock3() { return block3.get(); }
        public void setBlock3(String value) { block3.set(value); }
        public String getBlock4() { return block4.get(); }
        public void setBlock4(String value) { block4.set(value); }
        public String getBlock5() { return block5.get(); }
        public void setBlock5(String value) { block5.set(value); }
        public String getBlock6() { return block6.get(); }
        public void setBlock6(String value) { block6.set(value); }

        public boolean isBreakpointActive() {
            return breakpointActive.get();
        }
        public void setBreakpointActive(boolean active) {
            breakpointActive.set(active);
        }
        public BooleanProperty breakpointActiveProperty() {
            return breakpointActive;
        }
    }

    // Statische Referenz, damit MainController darauf zugreifen kann.
    public static TableLSTController instance;

    @FXML
    public Button btnFilePicker;
    @FXML
    private TableView<DataRow> tableViewLST;
    @FXML
    private TableColumn<DataRow, String> columnBlock0;
    @FXML
    private TableColumn<DataRow, String> columnBlock1;
    @FXML
    private TableColumn<DataRow, String> columnBlock2;
    @FXML
    private TableColumn<DataRow, String> columnBlock3;
    @FXML
    private TableColumn<DataRow, String> columnBlock4;
    @FXML
    private TableColumn<DataRow, String> columnBlock5;
    @FXML
    private TableColumn<DataRow, String> columnBlock6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        // Spalten konfigurieren: Hier weisen wir den jeweiligen SimpleStringProperty-Wert zu.
        columnBlock0.setCellValueFactory(cellData -> cellData.getValue().block0);
        columnBlock1.setCellValueFactory(cellData -> cellData.getValue().block1);
        columnBlock2.setCellValueFactory(cellData -> cellData.getValue().block2);
        columnBlock3.setCellValueFactory(cellData -> cellData.getValue().block3);
        columnBlock4.setCellValueFactory(cellData -> cellData.getValue().block4);
        columnBlock5.setCellValueFactory(cellData -> cellData.getValue().block5);
        columnBlock6.setCellValueFactory(cellData -> cellData.getValue().block6);

        // Zugriff auf den PIC (aus MainController) – hier nur zum Testen
        Platform.runLater(() -> {
            if (MainController.pic != null) {
                System.out.println("PIC aus MainController: " + MainController.pic);
            } else {
                System.out.println("PIC ist nicht initialisiert!");
            }
        });

        // Konfiguriere den FilePicker-Button und lade die Tabelle
        filePickerButtonPushed();

        // Standardpfad laden
        String defaultPath;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            defaultPath = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST";
        } else if (osName.contains("mac")) {
            defaultPath = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
        } else {
            defaultPath = "";
        }
        reloadTable(defaultPath);

        tableViewLST.setRowFactory(tv -> new TableRow<DataRow>() {
            @Override
            protected void updateItem(DataRow item, boolean empty) {
                super.updateItem(item, empty);
                // Entferne zunächst die Klasse, damit alte Werte nicht hängen bleiben
                getStyleClass().remove("highlight-row");
                if (!empty && item != null) {
                    // Beispiel: Hol den aktuellen Programmzähler aus einer anderen Klasse
                    String currentPC = MainController.pic.memory.convertPCLTo4BitsString();
                    if (item.getBlock1().equals(currentPC)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });

        // In der Spalte "Breakp" (block0) setzen wir eine CellFactory, die beim Klicken einen roten Kreis toggelt
        columnBlock0.setCellFactory(col -> {
            TableCell<DataRow, String> cell = new TableCell<DataRow, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    // Standardzustand: keine Grafik, evtl. Text leer
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Standardmäßig nichts anzeigen
                        setText("");
                        // Wenn bereits ein Breakpoint gesetzt wurde, könnte hier der Kreis schon da sein
                        if (getGraphic() instanceof Circle) {
                            // Grafik bleibt erhalten
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };

            // Beim Klicken soll der rote Kreis ein- bzw. wieder ausgeblendet werden
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    // Prüfe, ob bereits ein roter Kreis gesetzt ist
                    if (cell.getGraphic() == null) {
                        // Erzeuge einen roten Kreis
                        Circle circle = new Circle(7, Color.RED);
                        cell.setGraphic(circle);
                    } else {
                        // Entferne den Kreis
                        cell.setGraphic(null);
                    }
                }
            });

            return cell;
        });
    }

    public void refreshView() {
        tableViewLST.refresh();
    }

    private void reloadTable(String path) {
        FileLineParser flParser = new FileLineParser(path);
        ArrayList<DataRow> dataRows = new ArrayList<>();
        try {
            ArrayList<String> lines = flParser.parseFileToLines();
            for (String line : lines) {
                DataRow row = parseLineToDataRow(line);
                dataRows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<DataRow> data = FXCollections.observableArrayList(dataRows);
        tableViewLST.setItems(data);
    }

    private DataRow parseLineToDataRow(String line) {
        // Hier werden die einzelnen Blöcke extrahiert. block0 ist hier initialisiert mit "t"
        // (in deinem Beispiel als Platzhalter für das Breakpoint-Feld)
        String block0 = "", block1 = "", block2 = "", block3 = "", block4 = "", block5 = "", block6 = "";
        block1 = line.substring(0, 4).trim();
        block2 = line.substring(5, 10).trim();
        block3 = line.substring(20, 25).trim();
        block4 = line.substring(27, 36).trim();
        if (line.length() > 36) {
            if (line.charAt(36) != ';') {
                if (line.contains(";")) {
                    block5 = line.substring(36, line.indexOf(';')).trim();
                } else {
                    block5 = line.substring(36).trim();
                }
            }
            if (line.contains(";")) {
                block6 = line.substring(line.indexOf(';')).trim();
            }
        }
        return new DataRow(block0, block1, block2, block3, block4, block5, block6);
    }

    private void filePickerButtonPushed() {
        if (btnFilePicker != null) {
            btnFilePicker.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                String osName = System.getProperty("os.name").toLowerCase();
                File initialDir;
                if (osName.contains("win")) {
                    initialDir = new File("C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files");
                } else if (osName.contains("mac")) {
                    initialDir = new File("/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files");
                } else {
                    initialDir = new File("C:");
                }
                if (initialDir.exists() && initialDir.isDirectory()) {
                    fileChooser.setInitialDirectory(initialDir);
                } else {
                    System.out.println("Das angegebene Verzeichnis existiert nicht oder ist kein Ordner.");
                }
                File selectedFile = fileChooser.showOpenDialog(btnFilePicker.getScene().getWindow());
                if (selectedFile != null) {
                    System.out.println("Ausgewählte Datei: " + selectedFile.getAbsolutePath());
                    // Neuinitialisieren des PIC mit dem neuen File
                    MainController.pic = new PIC(selectedFile.getAbsolutePath());
                    // Danach die GUI (z. B. Tabelle) aktualisieren:
                    reloadTable(selectedFile.getAbsolutePath());
                }
            });
        } else {
            System.out.println("btnFilePicker wurde nicht gefunden!");
        }
    }
}
