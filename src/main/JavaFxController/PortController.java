package main.JavaFxController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.CacheHint;
import main.PIC;

public class PortController {
    public static PortController instance;

    @FXML
    private GridPane gridPane;

    private PIC pic;
    private int size;
    private Label[] raHeaders;
    private Label[] portALabels;
    private Label[] portBLabels;

    @FXML
    public void initialize() {
        MainController.ioPinsController = this;
        instance = this;
        this.pic = MainController.getStaticPic();
        this.size = pic.PortA.pins.length;

        // Spalten-Constraints einmalig festlegen
        for (int col = 0; col <= size; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / (size + 1));
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        // Zeilen-Constraints (Header, Port A, Header, Port B)
        for (int row = 0; row < 4; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(25.0);
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
        }

        // Arrays initialisieren
        raHeaders = new Label[size];
        //rbHeaders = new Label[size];
        portALabels = new Label[size];
        portBLabels = new Label[size];

        // Statische UI-Elemente aufbauen
        buildStaticUI();

        // Dynamische Inhalte befüllen
        buildUI();
    }

    private void buildStaticUI() {
        // RA-Header
        for (int i = 0; i < size; i++) {
            Label header = createHeader("RA" + i);
            raHeaders[i] = header;
            gridPane.add(header, i + 1, 0);
        }
        // Port A Label
        Label pa = createHeader("Port A");
        gridPane.add(pa, 0, 1);

        // Port A Pins
        for (int i = 0; i < size; i++) {
            Label lbl = createDataLabel();
            final int idx = i;
            lbl.setOnMouseClicked(event -> {
                pic.PortA.pins[idx].toggleValue();
                updateCell(lbl, pic.PortA.pins[idx].getValue(), pic.memory.readBit_bank(5, idx, 1));
            });
            portALabels[i] = lbl;
            gridPane.add(lbl, i + 1, 1);
        }

        // RB-Header
        for (int i = 0; i < size; i++) {
            Label header = createHeader("RB" + i);
            gridPane.add(header, i + 1, 2);
        }

        // Port B Label
        Label pb = createHeader("Port B");
        gridPane.add(pb, 0, 3);

        // Port B Pins
        for (int i = 0; i < size; i++) {
            Label lbl = createDataLabel();
            final int idx = i;
            lbl.setOnMouseClicked(event -> {
                pic.PortB.pins[idx].toggleValue();
                updateCell(lbl, pic.PortB.pins[idx].getValue(), pic.memory.readBit_bank(6, idx, 1));
            });
            portBLabels[i] = lbl;
            gridPane.add(lbl, i + 1, 3);
        }
    }

    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    public void buildUI() {
        gridPane.setCache(true);
        gridPane.setCacheHint(CacheHint.SPEED);

        for (int i = 0; i < size; i++) {
            updateCell(portALabels[i], pic.PortA.pins[i].getValue(), pic.memory.readBit_bank(5, i, 1));
            updateCell(portBLabels[i], pic.PortB.pins[i].getValue(), pic.memory.readBit_bank(6, i, 1));
        }

        gridPane.setCache(false);
    }

    private Label createHeader(String text) {
        Label lbl = new Label(text);
        lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        lbl.setStyle("-fx-alignment: center;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: black;");
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        GridPane.setVgrow(lbl, Priority.ALWAYS);
        return lbl;
    }

    private Label createDataLabel() {
        Label lbl = new Label();
        lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        lbl.setStyle("-fx-alignment: center;" +
                "-fx-border-color: black;" +
                "-fx-font-size: 14;");
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        GridPane.setVgrow(lbl, Priority.ALWAYS);
        return lbl;
    }

    private void updateCell(Label lbl, boolean value, int tris) {
        lbl.setText(value ? "1" : "0");
        String bg = (tris == 0) ? "green" : "yellow";
        // Stil erneut setzen, um Hintergrund anzupassen
        lbl.setStyle(lbl.getStyle() +
                "-fx-background-color: " + bg + ";");
    }
}