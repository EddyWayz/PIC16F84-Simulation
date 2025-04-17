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
    TableView<RAMRow> RAMTabsLST;
    @FXML
    private TableColumn<RAMRow, String> address;
    @FXML
    private TableColumn<RAMRow, String> valueH;
    @FXML
    private TableColumn<RAMRow, String> valueB;

    private PIC pic;  // eigene Referenz auf den aktuellen PIC

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.ramTabsLSTController = this;
        instance = this;
        this.pic = MainController.getStaticPic();
        setupColumns();
        buildUI();
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
