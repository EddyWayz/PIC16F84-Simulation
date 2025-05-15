package main.JavaFxController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.PIC;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StackController implements Initializable {
    // Damit JavaFX per Reflection die Getter findet, muss StackRow öffentlich sein.
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
        // Hier verbinden wir mit Lambda direkt an die Property in StackRow
        columnValue.setCellValueFactory(cellData ->
                cellData.getValue().valueProperty()
        );
        buildUI();
    }

    public void buildUI() {
        List<StackRow> temp = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            temp.add(new StackRow(
                    String.format("%04X",
                            MainController.getStaticPic().getStack().getVal(i)
                    )
            ));
        }
        stackTable.setItems(FXCollections.observableArrayList(temp));
    }


    /** Datenmodell für eine Zeile mit genau einem String-Wert */
    public static class StackRow {
        private final SimpleStringProperty value;
        public StackRow(String value) {
            this.value = new SimpleStringProperty(value);
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }

    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }
}
