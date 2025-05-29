package main.JavaFxController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.FileLineParser;
import main.FileLineParser.DataRow;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableLSTController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(TableLSTController.class.getName());
    public static TableLSTController instance;

    @FXML private Button btnFilePicker;
    @FXML private Button btnDocs;
    @FXML private Label runtimeCounter;
    @FXML private Label quarzFrequenz;
    @FXML
    TableView<DataRow> tableViewLST;
    @FXML private TableColumn<DataRow, String> columnBlock0;
    @FXML private TableColumn<DataRow, String> columnBlock1;
    @FXML private TableColumn<DataRow, String> columnBlock2;
    @FXML private TableColumn<DataRow, String> columnBlock3;
    @FXML private TableColumn<DataRow, String> columnBlock4;
    @FXML private TableColumn<DataRow, String> columnBlock5;
    @FXML private TableColumn<DataRow, String> columnBlock6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        MainController.tableLSTController = this;

        setupColumns();

        configureFilePicker();
        configureDocsButton();

        // Tabelle fuer den Anfang mit Standarddatei füllen
        loadDefaultFile();

        updateRuntimeLabel();
        updateQuarzLabel();

        highlightCurrentPCRow();

        // Breakpoint-Spalte klickbar machen
        enableBreakpointToggle();
    }

    /**
     * Spalten an die DataRow-Eigenschaften binden.
     */
    private void setupColumns() {
        columnBlock0.setCellValueFactory(c -> c.getValue().block0Property());
        columnBlock1.setCellValueFactory(c -> c.getValue().block1Property());
        columnBlock2.setCellValueFactory(c -> c.getValue().block2Property());
        columnBlock3.setCellValueFactory(c -> c.getValue().block3Property());
        columnBlock4.setCellValueFactory(c -> c.getValue().block4Property());
        columnBlock5.setCellValueFactory(c -> c.getValue().block5Property());
        columnBlock6.setCellValueFactory(c -> c.getValue().block6Property());
    }

    /**
     * Oeffnet die PDF-Dokumentation im Standard-PDF-Viewer.
     */
    @FXML
    private void configureDocsButton() {
        btnDocs.setOnAction(_ -> {
            try {
                URL resource = getClass().getResource("../../resources/docs/Dokumentation PIC16F84 Simulator.pdf");
                if (resource == null) {
                    LOGGER.warning("Dokumentation nicht gefunden");
                    return;
                }
                File pdf = new File(resource.toURI());
                Desktop.getDesktop().open(pdf);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fehler beim Oeffnen der Dokumentation", e);
            }
        });
    }


    /**
     * Konfiguriert den Button zum Auswählen einer LST-Datei.
     */
    private void configureFilePicker() {
        btnFilePicker.setOnAction(_ -> {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(determineInitialDirectory());
            File file = chooser.showOpenDialog(btnFilePicker.getScene().getWindow());
            if (file != null) {
                LOGGER.info("Datei gewählt: " + file.getAbsolutePath());
                MainController.updatePIC(file.getAbsolutePath());
            } else {
                LOGGER.warning("Keine Datei ausgewählt");
            }
        });
    }

    /**
     * Ermittelt je nach Betriebssystem einen sinnvollen Standardordner.
     */
    private File determineInitialDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String path;
        if (os.contains("win")) {
            path = "C:/Users/Noah/Desktop/HSO/Prakt Rechnerarchitekturen/PIC Sim/test_files";
        } else {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files";
        }

        return new File(path);
    }

    /**
     * Laedt die Tabelle mit einer Datei aus dem Standardpfad.
     */
    private void loadDefaultFile() {
        String os = System.getProperty("os.name").toLowerCase();
        String defaultPath = os.contains("win") ? "C:/Users/Noah/Desktop/HSO/Prakt Rechnerarchitekturen/PIC Sim/test_files/TPicSim1.LST" : "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";

        reloadTable(defaultPath);
    }

    /**
     * Aktualisiert den Laufzeit-Text.
     */
    public void updateRuntimeLabel() {
        double runtime = MainController.getStaticPic().getRuntimeCounter();
        runtimeCounter.setText(String.format("%05.2f µs", runtime));
    }

    /**
     * Aktualisiert den Quarzfrequenz-Text.
     */
    public void updateQuarzLabel() {
        double freq = MainController.getStaticPic().getQuarz_frequenzy();
        quarzFrequenz.setText(String.format("%04.2f MHz", freq));
    }

    /**
     * Füllt die Tabelle mit Daten aus einer LST-Datei.
     * @param path Dateipfad zur LST-Datei
     */
    void reloadTable(String path) {
        if (path == null || path.isEmpty()) {
            LOGGER.warning("Kein Pfad angegeben, Tabelle bleibt leer");
            return;
        }
        try {
            ArrayList<String> lines = new FileLineParser(path).parseFileToLines();
            ArrayList<DataRow> rows = new ArrayList<>();
            for (String line : lines) {
                rows.add(FileLineParser.parseLineToDataRow(line));
            }
            ObservableList<DataRow> data = FXCollections.observableArrayList(rows);
            tableViewLST.setItems(data);
            Platform.runLater(() -> autoResizeColumns(tableViewLST));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Einlesen der Datei: " + path, e);
        }
    }

    /**
     * Hebt die Zeile hervor, die dem aktuellen Programmzaehler.
     */
    private void highlightCurrentPCRow() {
        tableViewLST.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(DataRow item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("highlight-row");
                if (!empty && item != null) {
                    String currentPC = MainController.getStaticPic()
                            .memory.convertPCLTo4BitsString();
                    if (item.getBlock1().equals(currentPC)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });
    }

    /**
     * Macht die erste Spalte klickbar, um Breakpoints zu toggeln.
     */
    private void enableBreakpointToggle() {
        columnBlock0.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                DataRow row = getTableRow() != null ? getTableRow().getItem() : null;
                if (!empty && row != null && row.getBreakpointActive()) {
                    setGraphic(new Circle(7, Color.RED));
                }
            }

            {
                setOnMouseClicked(_ -> {
                    DataRow row = getTableRow() != null ? getTableRow().getItem() : null;
                    if (row != null) {
                        row.toggleBreakpointActive();
                        tableViewLST.refresh();
                    }
                });
            }
        });
    }

    /**
     * Passt alle Spaltenbreiten automatisch an.
     */
    private void autoResizeColumns(TableView<DataRow> table) {
        int count = table.getColumns().size();
        double totalWidth = table.getWidth();
        double usedWidth = 0;

        double[] widths = new double[count];
        for (int i = 0; i < count; i++) {
            TableColumn<DataRow, ?> column = table.getColumns().get(i);
            double max = new Text(column.getText()).getLayoutBounds().getWidth();
            for (DataRow row : table.getItems()) {
                Object cell = column.getCellData(row);
                if (cell != null) {
                    double w = new Text(cell.toString()).getLayoutBounds().getWidth();
                    if (w > max) max = w;
                }
            }
            widths[i] = max + 20;
            if (i != count - 1) usedWidth += widths[i];
        }

        for (int i = 0; i < count; i++) {
            TableColumn<DataRow, ?> col = table.getColumns().get(i);
            col.prefWidthProperty().unbind();
            if (i < count - 1) {
                col.setPrefWidth(widths[i]);
            } else {
                col.setPrefWidth(Math.max(widths[i], totalWidth - usedWidth));
            }
        }
    }

    /**
     * Aktualisiert Ansicht und Werte, z.B. nach Aenderungen.
     */
    public void refreshView() {
        tableViewLST.refresh();
        updateRuntimeLabel();
        updateQuarzLabel();
    }
}
