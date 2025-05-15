package main.JavaFxController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.FileLineParser;
import main.FileLineParser.DataRow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;

public class TableLSTController implements Initializable {

    // Statische Referenz, um von anderen Klassen (z.B. MainController) darauf zugreifen zu können.
    public static TableLSTController instance;

    @FXML
    public Button btnFilePicker;
    @FXML
    public Label runtimeCounter;
    @FXML
    public Label quarzFrequenz;
    @FXML
    public Button btnDocs;

    @FXML
    private void onDocsButtonClicked() {
        try {
            URL pdfUrl = getClass().getResource("../../resources/docs/Dokumentation PIC16F84 Simulator.pdf");
            if (pdfUrl == null) {
                System.err.println("PDF nicht gefunden: ../../resources/docs/Dokumentation PIC16F84 Simulator.pdf");
                return;
            }
            File pdfFile = new File(pdfUrl.toURI());
            Desktop.getDesktop().open(pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public TableView<DataRow> tableViewLST;
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
        MainController.tableLSTController = this;
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

        SetRuntimeText();
        SetQuarzFrequenzText();

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
        if (defaultPath.isEmpty()) {
            System.err.println("⚠ Standardpfad ist leer, Datei wird nicht geladen.");
        } else {
            reloadTable(defaultPath);
        }

        // Anpassen der RowFactory: Hervorheben der Zeile anhand des Programmzählers (PC).
        tableViewLST.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(DataRow item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("highlight-row");

                if (empty || item == null) {
                    setStyle("-fx-background-color: white;");
                    getStyleClass().remove("highlight-row");
                }
                if (!empty && item != null) {
                    // Beispiel: Abruf des aktuellen Programmzählers aus einer anderen Klasse
                    String currentPC = MainController.getStaticPic().memory.convertPCLTo4BitsString();
                    if (item.getBlock1().equals(currentPC)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });

        // In der Spalte "Breakp" (block0) setzen wir eine CellFactory ein, die beim Klicken einen roten Kreis toggelt.
        columnBlock0.setCellFactory(_ -> {
            TableCell<DataRow, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    DataRow dataRow = getTableRow() == null ? null : getTableRow().getItem();
                    if (empty || dataRow == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText("");
                        if (dataRow.getBreakpointActive()) {
                            Circle circle = new Circle(7, Color.RED);
                            setGraphic(circle);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };

            // Beim Klicken toggeln wir den Breakpoint-Zustand und fordern die Aktualisierung der gesamten TableView an.
            cell.setOnMouseClicked(_ -> {
                if (!cell.isEmpty()) {
                    DataRow dataRow = cell.getTableRow().getItem();
                    if (dataRow != null) {
                        dataRow.toggleBreakpointActive();
                        tableViewLST.refresh();
                    }
                }
            });

            return cell;
        });
    }

    private void SetRuntimeText() {
        runtimeCounter.setText(String.format("%05.2f µs", MainController.getStaticPic().getRuntimeCounter()));
    }

    private void SetQuarzFrequenzText() {
        quarzFrequenz.setText(String.format("%04.2f MHz", MainController.getStaticPic().getQuarz_frequenzy()));
    }

    public void refreshView() {
        tableViewLST.refresh();
        SetRuntimeText();
        SetQuarzFrequenzText();
    }

    /**
     * Liest die Datei ein und füllt die Tabelle.
     *
     * @param path Pfad zur LST-Datei
     */
    void reloadTable(String path) {
        if (path == null || path.isEmpty()) {
            System.err.println("⚠ Kein Pfad angegeben, Tabelle wird nicht geladen.");
            return;
        }
        FileLineParser flParser = new FileLineParser(path);
        ArrayList<DataRow> dataRows = new ArrayList<>();
        try {
            ArrayList<String> lines = flParser.parseFileToLines();
            for (String line : lines) {
                DataRow row = FileLineParser.parseLineToDataRow(line);
                dataRows.add(row);
            }
        } catch (IOException e) {
            System.err.println("⚠ Fehler beim Einlesen der LST-Datei '" + path + "': " + e.getMessage());
            e.printStackTrace();
        }
        if (dataRows.isEmpty()) {
            System.err.println("⚠ Keine Datenzeilen gefunden für Datei: " + path);
        }
        ObservableList<DataRow> data = FXCollections.observableArrayList(dataRows);
        tableViewLST.setItems(data);
        Platform.runLater(() -> autoResizeColumns(tableViewLST));
    }

    /**
     * Konfiguriert den File-Picker-Button, sodass ein neues LST-File ausgewählt werden kann.
     */
    private void filePickerButtonPushed() {
        if (btnFilePicker != null) {
            btnFilePicker.setOnAction(_ -> {
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
                    MainController.updatePIC(selectedFile.getAbsolutePath());
                }
                else {
                    System.out.println("⚠ Keine Datei ausgewählt.");
                }
            });
        } else {
            System.out.println("btnFilePicker wurde nicht gefunden!");
        }
    }
    private void autoResizeColumns(TableView<DataRow> table) {
        int columnCount = table.getColumns().size();
        double[] maxWidths = new double[columnCount];
        // Ermitteln der maximalen Breite für Header und Zellen
        for (int i = 0; i < columnCount; i++) {
            TableColumn<DataRow, ?> column = table.getColumns().get(i);
            Text header = new Text(column.getText());
            double maxWidth = header.getLayoutBounds().getWidth();
            for (DataRow row : table.getItems()) {
                Object cellData = column.getCellData(row);
                if (cellData != null) {
                    Text cellText = new Text(cellData.toString());
                    double cellWidth = cellText.getLayoutBounds().getWidth();
                    if (cellWidth > maxWidth) {
                        maxWidth = cellWidth;
                    }
                }
            }
            maxWidths[i] = maxWidth + 20; // Puffer hinzufügen
        }
        // Gesamtsumme der berechneten Breiten
        double totalWidth = 0;
        for (double w : maxWidths) totalWidth += w;
        // Set fixed widths for all columns except the 7th, and let the 7th fill remaining space
        double tableWidth = table.getWidth();
        double totalFixedWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            TableColumn<DataRow, ?> column = table.getColumns().get(i);
            column.prefWidthProperty().unbind();
            if (i != 6) {
                column.setPrefWidth(maxWidths[i]);
                totalFixedWidth += maxWidths[i];
            }
        }
        double remainingWidth = tableWidth - totalFixedWidth;
        TableColumn<DataRow, ?> column7 = table.getColumns().get(6);
        column7.prefWidthProperty().unbind();
        column7.setPrefWidth(Math.max(remainingWidth, maxWidths[6]));
    }
}
