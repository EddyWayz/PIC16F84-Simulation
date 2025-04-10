package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class RAMTabsLSTController implements Initializable {
    public static RAMTabsLSTController instance;
    @FXML
    private TableView<RAMRow> RAMTabsLST;

    @FXML
    private TableColumn<RAMRow, String> address;

    @FXML
    private TableColumn<RAMRow, String> valueH;

    @FXML
    private TableColumn<RAMRow, String> valueB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        // Spalten binden: Konvertiere die Integer-Werte in formatierten String (HEX oder Binary).
        address.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("0x%02X", cellData.getValue().getAddress()))
        );
        valueH.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("0x%02X", cellData.getValue().getValue()))
        );
        valueB.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        // Konvertiert in Binärstring, füllt mit führenden Nullen bis 8 Zeichen.
                        String.format("%8s", Integer.toBinaryString(cellData.getValue().getValue())).replace(' ', '0')
                )
        );

        // Erstelle eine Liste von RAMRow-Einträgen für Adressen von 0x0C bis 0x4F.
        ObservableList<RAMRow> rows = FXCollections.observableArrayList();

        if (MainController.pic != null) {
            for (int addr = 0x0C; addr <= 0x4F; addr++) {
                int value = MainController.pic.memory.read(addr);
                rows.add(new RAMRow(addr, value));
            }
        } else {
            System.err.println("MainController.pic ist null – Tabelle wird nicht befüllt.");
            // Hier könntest du alternativ Standardwerte setzen oder die Tabelle leer lassen.
        }
        RAMTabsLST.setItems(rows);
    }

    public void refreshView() {
        // Neue Datensätze berechnen und in die Tabelle einfügen
        ObservableList<RAMRow> newRows = updateData();
        RAMTabsLST.setItems(newRows);
        // Optional: Tabelle neu zeichnen, falls erforderlich.
        RAMTabsLST.refresh();
    }

    private ObservableList<RAMRow> updateData() {
        ObservableList<RAMRow> rows = FXCollections.observableArrayList();
        if (MainController.pic != null) {
            for (int addr = 0x0C; addr <= 0x4F; addr++) {
                int value = MainController.pic.memory.read(addr);
                rows.add(new RAMRow(addr, value));
            }
        } else {
            System.err.println("MainController.pic ist null – Tabelle wird nicht aktualisiert.");
        }
        return rows;
    }

    /**
     * Modellklasse für eine Zeile in der RAM-Tabelle.
     * - address: die Adresse als Integer (0x0C bis 0x4F)
     * - value: der zugehörige Wert als Integer
     */
    public static class RAMRow {
        private final SimpleIntegerProperty address;
        private final SimpleIntegerProperty value;

        public RAMRow(int address, int value) {
            this.address = new SimpleIntegerProperty(address);
            this.value = new SimpleIntegerProperty(value);
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
