package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import main.tools.Label_Lib;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.io.IOException;

public class TabelleLSTController implements Initializable {

    // Innere Klasse DataRow – wie bisher
    public static class DataRow {
        private final SimpleStringProperty block1;
        private final SimpleStringProperty block2;
        private final SimpleStringProperty block3;
        private final SimpleStringProperty block4;
        private final SimpleStringProperty block5;
        private final SimpleStringProperty block6;

        public DataRow(String block1, String block2, String block3, String block4, String block5, String block6) {
            this.block1 = new SimpleStringProperty(block1);
            this.block2 = new SimpleStringProperty(block2);
            this.block3 = new SimpleStringProperty(block3);
            this.block4 = new SimpleStringProperty(block4);
            this.block5 = new SimpleStringProperty(block5);
            this.block6 = new SimpleStringProperty(block6);
        }

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
    }

    @FXML
    public Button btnFilePicker;
    @FXML
    private TableView<DataRow> tableViewLST;
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
        // Spalten konfigurieren
        columnBlock1.setCellValueFactory(cellData -> cellData.getValue().block1);
        columnBlock2.setCellValueFactory(cellData -> cellData.getValue().block2);
        columnBlock3.setCellValueFactory(cellData -> cellData.getValue().block3);
        columnBlock4.setCellValueFactory(cellData -> cellData.getValue().block4);
        columnBlock5.setCellValueFactory(cellData -> cellData.getValue().block5);
        columnBlock6.setCellValueFactory(cellData -> cellData.getValue().block6);

        // Zugriff auf PIC über MainController
        Platform.runLater(() -> {
            // Jetzt ist sicher, dass der MainController initialisiert wurde.
            if (MainController.pic != null) {
                System.out.println("PIC aus MainController: " + MainController.pic);
                // Verwende MainController.pic, z. B. um den aktuellen PC abzurufen:
                // int currentPC = MainController.pic.getPC();
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
                    String currentPC = String.valueOf("00003");
                    if (item.getBlock3().equals(currentPC)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });
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
        String block1 = "", block2 = "", block3 = "", block4 = "", block5 = "", block6 = "";
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
        return new DataRow(block1, block2, block3, block4, block5, block6);
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
                    reloadTable(selectedFile.getAbsolutePath());
                }
            });
        } else {
            System.out.println("btnFilePicker wurde nicht gefunden!");
        }
    }
}
