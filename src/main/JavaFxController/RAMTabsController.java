package main.JavaFxController;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.text.Text;
import javafx.scene.control.TableView;
import main.PIC;
import java.net.URL;
import java.util.ResourceBundle;

public class RAMTabsController implements Initializable {
    public static RAMTabsController instance;

    @FXML
    TableView<RAMRow> RAMTabsLST;
    @FXML
    private TableColumn<RAMRow, String> address;
    @FXML
    private TableColumn<RAMRow, String> valueH;
    @FXML
    private TableColumn<RAMRow, String> valueB;

    @FXML
    private TableView<SFRRow> SFRTable;
    @FXML
    private TableColumn<SFRRow, String> addressSFR;
    @FXML
    private TableColumn<SFRRow, String> nameSFR;
    @FXML
    private TableColumn<SFRRow, String> valueHexSFR;
    @FXML
    private TableColumn<SFRRow, String> valueBinSFR;

    private PIC pic;  // eigene Referenz auf den aktuellen PIC

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.ramTabsController = this;
        instance = this;

        RAMTabsLST.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(RAMRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-background-color: white;");
                }
            }
        });

        SFRTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(SFRRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-background-color: white;");
                }
            }
        });


        // Ensure columns exactly fill the table width without extra blank space
        RAMTabsLST.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        SFRTable .setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.pic = MainController.getStaticPic();
        setupColumns();
        buildUI();
        setupSFR();
        buildSFR();
        autoResizeColumns(RAMTabsLST);
        autoResizeColumns(SFRTable);
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
                        String.format("0b%8s", Integer.toBinaryString(cellData.getValue().getValue()))
                                .replace(' ', '0')
                )
        );
    }

    /**
     * Baut die TableView neu zusammen anhand der aktuellen PIC-Instanz.
     */
    public void buildUI() {
        if (pic == null) {
            RAMTabsLST.getItems().clear();
            SFRTable.getItems().clear();
            return;
        }

        ObservableList<RAMRow> rows = FXCollections.observableArrayList();
        for (int addr = 0x0C; addr <= 0x4F; addr++) {
            int value = pic.memory.read(addr);
            rows.add(new RAMRow(addr, value));
        }

        RAMTabsLST.setItems(rows);
        RAMTabsLST.refresh();
        buildSFR();
    }

    /**
     * Passt die Spaltenbreiten automatisch an:
     * Alle Spalten auf ihre maximale Breite beschränken,
     * die letzte Spalte nimmt den übrigen Platz ein.
     */
    private <T> void autoResizeColumns(TableView<T> table) {
        int columnCount = table.getColumns().size();
        double[] maxWidths = new double[columnCount];
        // Ermitteln der maximalen Breite für Header und Zellen
        for (int i = 0; i < columnCount; i++) {
            TableColumn<T, ?> column = table.getColumns().get(i);
            Text header = new Text(column.getText());
            double maxWidth = header.getLayoutBounds().getWidth();
            for (T row : table.getItems()) {
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
            TableColumn<T, ?> column = table.getColumns().get(i);
            column.prefWidthProperty().unbind();
            column.setPrefWidth(maxWidths[i]);
            totalFixedWidth += maxWidths[i];
        }
        double remainingWidth = tableWidth - totalFixedWidth;
        TableColumn<T, ?> lastColumn = table.getColumns().get(columnCount - 1);
        lastColumn.prefWidthProperty().unbind();
        lastColumn.setPrefWidth(Math.max(remainingWidth, maxWidths[columnCount - 1]));
    }

    /**
     * Konfiguriert die Spalten im Test-Tab.
     */
    private void setupSFR() {
        // Configure SFR table columns
        addressSFR.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                String.format("0x%02X", cellData.getValue().getAddress())
            )
        );
        nameSFR.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                cellData.getValue().getName()
            )
        );
        valueHexSFR.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                String.format("0x%02X", cellData.getValue().getValue())
            )
        );
        valueBinSFR.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                String.format("0b%8s", Integer.toBinaryString(cellData.getValue().getValue()))
                    .replace(' ', '0')
            )
        );
    }

    /**
     * Baut das Test-Tab mit Beispiel-Daten auf.
     */
    private void buildSFR() {
        ObservableList<SFRRow> SFRRows = FXCollections.observableArrayList();
        PIC pic = MainController.getStaticPic();
        SFRRows.add(new SFRRow(0, "Indirect Address", 0));
        SFRRows.add(new SFRRow(1, "TMR0", pic.memory.read_bank(1, 0)));
        SFRRows.add(new SFRRow(2, "PCL", pic.memory.read_bank(2, 0)));
        SFRRows.add(new SFRRow(3, "STATUS", pic.memory.read_bank(3, 0)));
        SFRRows.add(new SFRRow(4, "FSR", pic.memory.read_bank(4, 0)));
        SFRRows.add(new SFRRow(5, "PORTA", pic.memory.read_bank(5, 0)));
        SFRRows.add(new SFRRow(6, "PORTB", pic.memory.read_bank(6, 0)));
        SFRRows.add(new SFRRow(8, "EEDATA", pic.memory.read_bank(8, 0)));
        SFRRows.add(new SFRRow(9, "EEADR", pic.memory.read_bank(9, 0)));
        SFRRows.add(new SFRRow(10, "PCLATCH", pic.memory.read_bank(10, 0)));
        SFRRows.add(new SFRRow(11, "INTCON", pic.memory.read_bank(11, 0)));
        SFRRows.add(new SFRRow(1, "OPTION", pic.memory.read_bank(1, 1)));
        SFRRows.add(new SFRRow(5, "TRISA", pic.memory.read_bank(5, 1)));
        SFRRows.add(new SFRRow(6, "TRISB", pic.memory.read_bank(6, 1)));
        SFRRows.add(new SFRRow(8, "EECON1", pic.memory.read_bank(8, 1)));
        SFRRows.add(new SFRRow(9, "EECON2", pic.memory.read_bank(9, 1)));
        SFRTable.setItems(SFRRows);
        SFRTable.refresh();
    }

    /**
     * Datenmodell für eine Zeile in der Test-Tabelle.
     */
    public static class SFRRow {
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty value;

        public SFRRow(int address, String name, int value) {
            this.address = new SimpleIntegerProperty(address);
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleIntegerProperty(value);
        }

        public int getAddress() { return address.get(); }

        public String getName() { return name.get(); }

        public int getValue() { return value.get(); }
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

        public int getValue() {
            return value.get();
        }
    }
}
