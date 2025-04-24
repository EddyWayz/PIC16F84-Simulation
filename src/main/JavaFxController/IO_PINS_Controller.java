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

        // RA header labels (RA7 to RA0)
        for (int i = 0; i < size; i++) {
            int pin = size - 1 - i;
            Label header = new Label("RA" + pin);
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
            boolean value = pic.PortA.ioPins[i].getValue();
            Label label = new Label(value ? "1" : "0");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.prefWidthProperty().bind(label.heightProperty());
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle(
                "-fx-alignment: center;" +
                "-fx-border-color: black;" +
                "-fx-font-size: 14;"
            );
            final int index = i;
            label.setOnMouseClicked(event -> {
                pic.PortA.ioPins[index].toggleValue();
                label.setText(pic.PortA.ioPins[index].getValue() ? "1" : "0");
            });
            gridPane.add(label, i + 1, 1);
        }

        // RB header labels (RB7 to RB0)
        for (int i = 0; i < size; i++) {
            int pin = size - 1 - i;
            Label header = new Label("RB" + pin);
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
            boolean value = pic.PortB.ioPins[i].getValue();
            Label label = new Label(value ? "1" : "0");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.prefWidthProperty().bind(label.heightProperty());
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle(
                "-fx-alignment: center;" +
                "-fx-border-color: black;" +
                "-fx-font-size: 14;"
            );
            final int index = i;
            label.setOnMouseClicked(event -> {
                pic.PortB.ioPins[index].toggleValue();
                label.setText(pic.PortB.ioPins[index].getValue() ? "1" : "0");
            });
            gridPane.add(label, i + 1, 3);
        }
    }
}
