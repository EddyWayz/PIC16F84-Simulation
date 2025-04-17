package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class IO_PINS_Controller {

    @FXML
    private GridPane gridPane;

    private PIC pic; // Eigene PIC-Referenz, die austauschbar ist

    @FXML
    public void initialize() {
        MainController.ioPinsController = this;
        // Initiale PIC-Referenz aus dem MainController holen
        this.pic = MainController.getStaticPic();
        buildUI();
    }

    /**
     * Diese Methode wird aufgerufen, wenn eine neue PIC-Instanz geladen wird.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI(); // UI neu aufbauen mit neuer PIC-Instanz
    }

    /**
     * Baut das Grid UI anhand der aktuellen PIC-Instanz neu auf.
     */
    public void buildUI() {
        gridPane.getChildren().clear(); // Grid leeren

        int width = pic.IO_Pins.ioPins.length;

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                boolean value = pic.IO_Pins.ioPins[row][col].getValue();

                Label label = new Label(value ? "1" : "0");
                label.setMinSize(30, 30); // feste Größe
                label.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-font-size: 14;");

                final int r = row;
                final int c = col;

                // Event-Handler verwendet jetzt `this.pic` statt statischen Zugriff
                label.setOnMouseClicked(event -> {
                    pic.IO_Pins.ioPins[r][c].toggleValue();
                    label.setText(pic.IO_Pins.ioPins[r][c].getValue() ? "1" : "0");
                });

                gridPane.add(label, col, row);
            }
        }
    }
}
