package main.JavaFxController;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.scene.control.TableView;
import main.PIC;

import java.net.URL;
import java.util.ResourceBundle;

public class RAMTabsLSTController implements Initializable {
    public static RAMTabsLSTController instance;

    @FXML
    TableView<RAMRow> RAMTabsLST;
    @FXML
    private TableColumn<RAMRow, String> address;
    @FXML
    private TableColumn<RAMRow, String> valueH;
    @FXML
    private TableColumn<RAMRow, String> valueB;

    @FXML private TableView<TestRow> testTable;
    @FXML private TableColumn<TestRow, String> col1;
    @FXML private TableColumn<TestRow, String> col2;

    private PIC pic;  // eigene Referenz auf den aktuellen PIC

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.ramTabsLSTController = this;
        instance = this;
        // Ensure columns exactly fill the table width without extra blank space
        RAMTabsLST.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.pic = MainController.getStaticPic();
        setupColumns();
        buildUI();
        setupTestColumns();
        buildTestData();
    }

    /**
     * Wird aufgerufen, wenn MainController einen neuen PIC lädt.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    /**
     * Konfiguriert nur einmal die CellValueFactories – bleibt immer gleich.
     */
    private void setupColumns() {
        address.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        String.format("0x%02X", cellData.getValue().getAddress())
                )
        );
        valueH.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        String.format("0x%02X", cellData.getValue().getValue())
                )
        );
        valueB.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        String.format("%8s", Integer.toBinaryString(cellData.getValue().getValue()))
                                .replace(' ', '0')
                )
        );
    }

    /**
     * Baut die TableView neu zusammen anhand der aktuellen PIC-Instanz.
     */
    private void buildUI() {
        if (pic == null) {
            RAMTabsLST.getItems().clear();
            return;
        }

        ObservableList<RAMRow> rows = FXCollections.observableArrayList();
        // Beispiel: Adressen von 0x0C bis 0x4F
        for (int addr = 0x0C; addr <= 0x4F; addr++) {
            int value = pic.memory.read(addr);
            rows.add(new RAMRow(addr, value));
        }

        RAMTabsLST.setItems(rows);
        RAMTabsLST.refresh();
        autoResizeColumns(RAMTabsLST);
    }

    /**
     * Passt die Spaltenbreiten automatisch an:
     * Alle Spalten auf ihre maximale Breite beschränken,
     * die letzte Spalte nimmt den übrigen Platz ein.
     */
    private void autoResizeColumns(TableView<RAMRow> table) {
        int columnCount = table.getColumns().size();
        double[] maxWidths = new double[columnCount];
        // Ermitteln der maximalen Breite für Header und Zellen
        for (int i = 0; i < columnCount; i++) {
            TableColumn<RAMRow, ?> column = table.getColumns().get(i);
            Text header = new Text(column.getText());
            double maxWidth = header.getLayoutBounds().getWidth();
            for (RAMRow row : table.getItems()) {
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
        // Fixe Breiten für alle Spalten außer der letzten und verbleibenden Platz für die letzte Spalte
        double tableWidth = table.getWidth();
        double totalFixedWidth = 0;
        for (int i = 0; i < columnCount - 1; i++) {
            TableColumn<RAMRow, ?> column = table.getColumns().get(i);
            column.prefWidthProperty().unbind();
            column.setPrefWidth(maxWidths[i]);
            totalFixedWidth += maxWidths[i];
        }
        double remainingWidth = tableWidth - totalFixedWidth;
        TableColumn<RAMRow, ?> lastColumn = table.getColumns().get(columnCount - 1);
        lastColumn.prefWidthProperty().unbind();
        lastColumn.setPrefWidth(remainingWidth > maxWidths[columnCount - 1] ? remainingWidth : maxWidths[columnCount - 1]);
    }

    /**
     * Konfiguriert die Spalten im Test-Tab.
     */
    private void setupTestColumns() {
        col1.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCol1()));
        col2.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCol2()));
    }

    /**
     * Baut das Test-Tab mit Beispiel-Daten auf.
     */
    private void buildTestData() {
        ObservableList<TestRow> testRows = FXCollections.observableArrayList();
        testRows.add(new TestRow("A", "1"));
        testRows.add(new TestRow("B", "2"));
        testRows.add(new TestRow("C", "3"));
        testTable.setItems(testRows);
        testTable.refresh();
    }

    /**
     * Datenmodell für eine Zeile in der Test-Tabelle.
     */
    public static class TestRow {
        private final SimpleStringProperty col1;
        private final SimpleStringProperty col2;

        public TestRow(String col1, String col2) {
            this.col1 = new SimpleStringProperty(col1);
            this.col2 = new SimpleStringProperty(col2);
        }

        public String getCol1() { return col1.get(); }
        public SimpleStringProperty col1Property() { return col1; }

        public String getCol2() { return col2.get(); }
        public SimpleStringProperty col2Property() { return col2; }
    }

    /**
     * Datenmodell für eine Zeile in der RAM-Tabelle.
     */
    public static class RAMRow {
        private final SimpleIntegerProperty address;
        private final SimpleIntegerProperty value;

        public RAMRow(int address, int value) {
            this.address = new SimpleIntegerProperty(address);
            this.value   = new SimpleIntegerProperty(value);
        }

        public int getAddress() {
            return address.get();
        }
        public SimpleIntegerProperty addressProperty() {
            return address;
        }

        public int getValue() {
            return value.get();
        }
        public SimpleIntegerProperty valueProperty() {
            return value;
        }
    }
}
