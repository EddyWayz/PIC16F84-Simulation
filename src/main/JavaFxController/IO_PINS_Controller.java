package main.JavaFxController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.PIC;

public class IO_PINS_Controller {

    @FXML
    private GridPane gridPane;

    private PIC pic; // Austauschbare PIC-Referenz

    @FXML
    public void initialize() {
        MainController.ioPinsController = this;
        this.pic = MainController.getStaticPic();
        buildUI();
    }

    /**
     * Wird aufgerufen, wenn eine neue PIC-Instanz geladen wird.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    /**
     * Baut das Grid-UI anhand der aktuellen PIC-Instanz neu auf.
     * Nutzt die FXML-Constraints (percentWidth/percentHeight + hgrow/vgrow),
     * damit jedes Label seine Zelle vollständig ausfüllt.
     */
    public void buildUI() {
        gridPane.getChildren().clear();

        int size = pic.PortA.ioPins.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean value = pic.PortA.ioPins[row][col].getValue();
                Label label = new Label(value ? "1" : "0");

                // Zelle komplett ausfüllen lassen
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                label.prefWidthProperty().bind(label.heightProperty());
                GridPane.setHgrow(label, Priority.ALWAYS);
                GridPane.setVgrow(label, Priority.ALWAYS);

                label.setStyle(
                        "-fx-alignment: center;" +
                                "-fx-border-color: black;" +
                                "-fx-font-size: 14;"
                );

                final int r = row, c = col;
                label.setOnMouseClicked(event -> {
                    pic.PortA.ioPins[r][c].toggleValue();
                    label.setText(pic.PortA.ioPins[r][c].getValue() ? "1" : "0");
                });

                gridPane.add(label, col, row);
            }
        }
    }
}
