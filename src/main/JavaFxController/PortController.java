package main.JavaFxController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.PIC;

public class PortController {
    public static PortController instance;
    @FXML
    private GridPane gridPane;

    private PIC pic; // Austauschbare PIC-Referenz

    @FXML
    public void initialize() {
        MainController.ioPinsController = this;
        instance = this;
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

        int size = pic.PortA.pins.length;

        // RA header labels (RA0 to RA7)
        for (int i = 0; i < size; i++) {
            Label header = new Label("RA" + i);
            header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            header.prefWidthProperty().bind(header.heightProperty());
            GridPane.setHgrow(header, Priority.ALWAYS);
            GridPane.setVgrow(header, Priority.ALWAYS);
            header.setStyle(
                "-fx-alignment: center;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: black;"
            );
            gridPane.add(header, i + 1, 0);
        }

        // Port A row
        Label portALabel = new Label("Port A");
        portALabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(portALabel, Priority.ALWAYS);
        GridPane.setVgrow(portALabel, Priority.ALWAYS);
        portALabel.setStyle(
            "-fx-alignment: center;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: black;" +
            "-fx-pref-width: 80px;"
        );
        gridPane.add(portALabel, 0, 1);
        for (int i = 0; i < size; i++) {
            boolean value = pic.PortA.pins[i].getValue();
            Label label = new Label(value ? "1" : "0");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.prefWidthProperty().bind(label.heightProperty());
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            // Hintergrundfarbe abhängig vom TRIS-Bit (0=Output, 1=Input)
            int tris = pic.memory.readBit_bank(5, i, 1);
            String bgColor = (tris == 0) ? "green" : "yellow";
            label.setStyle(
                "-fx-alignment: center;" +
                "-fx-border-color: black;" +
                "-fx-font-size: 14;" +
                "-fx-background-color: " + bgColor + ";"
            );
            final int index = i;
            label.setOnMouseClicked(event -> {
                pic.PortA.pins[index].toggleValue();
                label.setText(pic.PortA.pins[index].getValue() ? "1" : "0");
                // Recompute background based on TRIS after toggle
                int tris2 = pic.memory.readBit_bank(5, index, 1);
                String bgColor2 = (tris2 == 0) ? "green" : "yellow";
                label.setStyle(
                    "-fx-alignment: center;" +
                    "-fx-border-color: black;" +
                    "-fx-font-size: 14;" +
                    "-fx-background-color: " + bgColor2 + ";"
                );
            });
            gridPane.add(label, i + 1, 1);
        }

        // RB header labels (RB0 to RB7)
        for (int i = 0; i < size; i++) {
            Label header = new Label("RB" + i);
            header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            header.prefWidthProperty().bind(header.heightProperty());
            GridPane.setHgrow(header, Priority.ALWAYS);
            GridPane.setVgrow(header, Priority.ALWAYS);
            header.setStyle(
                "-fx-alignment: center;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: black;"
            );
            gridPane.add(header, i + 1, 2);
        }

        // Port B row
        Label portBLabel = new Label("Port B");
        portBLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(portBLabel, Priority.ALWAYS);
        GridPane.setVgrow(portBLabel, Priority.ALWAYS);
        portBLabel.setStyle(
            "-fx-alignment: center;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: black;" +
            "-fx-pref-width: 80px;"
        );
        gridPane.add(portBLabel, 0, 3);
        for (int i = 0; i < size; i++) {
            boolean value = pic.PortB.pins[i].getValue();
            Label label = new Label(value ? "1" : "0");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.prefWidthProperty().bind(label.heightProperty());
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            // Hintergrundfarbe abhängig vom TRIS-Bit (0=Output, 1=Input)
            int tris = pic.memory.readBit_bank(6, i, 1);
            String bgColor = (tris == 0) ? "green" : "yellow";
            label.setStyle(
                "-fx-alignment: center;" +
                "-fx-border-color: black;" +
                "-fx-font-size: 14;" +
                "-fx-background-color: " + bgColor + ";"
            );
            final int index = i;
            label.setOnMouseClicked(event -> {
                pic.PortB.pins[index].toggleValue();
                label.setText(pic.PortB.pins[index].getValue() ? "1" : "0");
                // Recompute background based on TRIS after toggle
                int tris2 = pic.memory.readBit_bank(6, index, 1);
                String bgColor2 = (tris2 == 0) ? "green" : "yellow";
                label.setStyle(
                    "-fx-alignment: center;" +
                    "-fx-border-color: black;" +
                    "-fx-font-size: 14;" +
                    "-fx-background-color: " + bgColor2 + ";"
                );
            });
            gridPane.add(label, i + 1, 3);
        }
    }
}
