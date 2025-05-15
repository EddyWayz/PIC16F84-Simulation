package main.JavaFxController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import main.PIC;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StackController implements Initializable {
    public static StackController instance;

    @FXML
    private TableView<StackRow> stackTable;
    @FXML
    private TableColumn<StackRow, String> columnValue;

    private PIC pic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.stackController = this;
        instance = this;
        this.pic = MainController.getStaticPic();

        columnValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // Factory einmal setzen – noch bevor du Items hinzufügst:
        stackTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(StackRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-background-color: white;");
                    getStyleClass().remove("highlight-row");
                }
                getStyleClass().removeAll("highlight-row");
                if (!empty) {
                    int pointer = pic.getStack().getStack_pointer();
                    // getIndex() gibt den Zeilen-Index im TableView zurück:
                    if (getIndex() == (7 - pointer)) {
                        getStyleClass().add("highlight-row");
                    }
                }
            }
        });

        buildUI();
    }

    public void buildUI() {
        List<StackRow> temp = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            temp.add(new StackRow(String.format("[%d] 0x%04X", i, pic.getStack().getVal(i))));
        }
        stackTable.setItems(FXCollections.observableArrayList(temp));
    }

    /**
     * Updates the PIC instance used by the StackController and rebuilds the UI to reflect the new PIC state.
     *
     * @param newPic The new PIC instance to be used.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    /**
     * Represents a row in the stack table with a single value property that is a string.
     */
    public static class StackRow {
        private final SimpleStringProperty value;
        /**
         * Constructs a StackRow with the specified value.
         *
         * @param value The string value to be stored in this StackRow.
         */
        public StackRow(String value) {
            this.value = new SimpleStringProperty(value);
        }
        public SimpleStringProperty valueProperty() {
            return value;
        }
    }
}
