package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IO_PINS_Controller {

    @FXML
    private GridPane gridPane;

    PIC pic = MainController.pic;
    int width = pic.IO_Pins.ioPins.length;

    @FXML
    public void initialize() {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                // Aktuellen Wert holen
                boolean value = pic.IO_Pins.ioPins[row][col].getValue();

                // Rechteck erstellen
                Label label = new Label(value ? "1" : "0");
                label.setMinSize(30, 30); // feste Größe
                label.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-font-size: 14;");

                final int r = row;  // notwendig wegen Lambda
                final int c = col;

                // Click-Event zum Umschalten
                label.setOnMouseClicked(event -> {
                    pic.IO_Pins.ioPins[r][c].toggleValue();; // Toggle-Wert setzen
                    label.setText(pic.IO_Pins.ioPins[r][c].getValue() ? "1" : "0");
                });

                // Zum Grid hinzufügen
                gridPane.add(label, col, row);
            }
        }
    }
}
