package main.JavaFxController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class StackController implements Initializable {
    // Damit JavaFX per Reflection die Getter findet, muss StackRow öffentlich sein.
    public static StackController instance;

    @FXML
    private TableView<StackRow> stackTable;
    @FXML
    private TableColumn<StackRow, String> columnValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.stackController = this;
        instance = this;

        // Hier verbinden wir mit Lambda direkt an die Property in StackRow
        columnValue.setCellValueFactory(cellData ->
                cellData.getValue().valueProperty()
        );

        // Beispiel: In der Tabelle nur eine Zeile mit "42"
        stackTable.setItems(FXCollections.observableArrayList(
                new StackRow("42")
        ));

    }

    public void refreshView() {
        stackTable.refresh();
    }

    /** Datenmodell für eine Zeile mit genau einem String-Wert */
    public static class StackRow {
        private final SimpleStringProperty value;
        public StackRow(String value) {
            this.value = new SimpleStringProperty(value);
        }
        public String getValue() {
            return value.get();
        }
        public SimpleStringProperty valueProperty() {
            return value;
        }
    }
}
