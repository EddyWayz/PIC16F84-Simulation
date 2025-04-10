package main;
import main.FileLineParser.DataRow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;

public class TableLSTController implements Initializable {

    // Statische Referenz, um von anderen Klassen (z. B. MainController) darauf zugreifen zu können.
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

        // Konfiguration der Spalten: Hier weisen wir die zugehörigen Property Accessoren zu.
        columnBlock0.setCellValueFactory(cellData -> cellData.getValue().block0Property());
        columnBlock1.setCellValueFactory(cellData -> cellData.getValue().block1Property());
        columnBlock2.setCellValueFactory(cellData -> cellData.getValue().block2Property());
        columnBlock3.setCellValueFactory(cellData -> cellData.getValue().block3Property());
        columnBlock4.setCellValueFactory(cellData -> cellData.getValue().block4Property());
        columnBlock5.setCellValueFactory(cellData -> cellData.getValue().block5Property());
        columnBlock6.setCellValueFactory(cellData -> cellData.getValue().block6Property());

        // FilePicker-Button konfigurieren und Tabelle laden.
        filePickerButtonPushed();

        // Standardpfad laden (je nach Betriebssystem)
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

        // Anpassen der RowFactory: Hervorheben der Zeile anhand des Programmzählers (PC).
        tableViewLST.setRowFactory(tv -> new TableRow<DataRow>() {
            @Override
            protected void updateItem(DataRow item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("highlight-row");
                if (!empty && item != null) {
                    // Beispiel: Abruf des aktuellen Programmzählers aus einer anderen Klasse
                    String currentPC = MainController.pic.memory.convertPCLTo4BitsString();
                    if (item.getBlock1().equals(currentPC)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });

        // In der Spalte "Breakp" (block0) setzen wir eine CellFactory ein, die beim Klicken einen roten Kreis toggelt.
        columnBlock0.setCellFactory(col -> {
            TableCell<DataRow, String> cell = new TableCell<DataRow, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    DataRow dataRow = getTableRow() == null ? null : getTableRow().getItem();
                    if (empty || dataRow == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText("");
                        if (dataRow.isBreakpointActive()) {
                            Circle circle = new Circle(7, Color.RED);
                            setGraphic(circle);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };

            // Beim Klicken toggeln wir den Breakpoint-Zustand und fordern die Aktualisierung der gesamten TableView an.
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    DataRow dataRow = cell.getTableRow().getItem();
                    if (dataRow != null) {
                        dataRow.setBreakpointActive(!dataRow.isBreakpointActive());
                        tableViewLST.refresh();
                    }
                }
            });

            return cell;
        });
    }

    public void refreshView() {
        tableViewLST.refresh();
    }

    /**
     * Liest die Datei ein und füllt die Tabelle.
     *
     * @param path Pfad zur LST-Datei
     */
    private void reloadTable(String path) {
        FileLineParser flParser = new FileLineParser(path);
        ArrayList<DataRow> dataRows = new ArrayList<>();
        try {
            ArrayList<String> lines = flParser.parseFileToLines();
            for (String line : lines) {
                DataRow row = FileLineParser.parseLineToDataRow(line);
                dataRows.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<DataRow> data = FXCollections.observableArrayList(dataRows);
        tableViewLST.setItems(data);
    }

    /**
     * Konfiguriert den File-Picker-Button, sodass ein neues LST-File ausgewählt werden kann.
     */
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
                    // Neuinitialisieren des PIC mit der ausgewählten Datei
                    MainController.pic = new PIC(selectedFile.getAbsolutePath());
                    // Danach die Tabelle mit den neuen Daten aktualisieren
                    reloadTable(selectedFile.getAbsolutePath());
                }
            });
        } else {
            System.out.println("btnFilePicker wurde nicht gefunden!");
        }
    }
}
