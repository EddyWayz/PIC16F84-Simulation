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
    private PIC pic;
    private int size;
    private Label[] portALabels;
    private Label[] portBLabels;

    @FXML public Label colorDescriptionLabel1;
    @FXML private GridPane gridPane;

    @FXML
    public void initialize() {
        MainController.portController = this;
        instance = this;

        this.pic  = MainController.getStaticPic();
        this.size = pic.memory.PortA.pins.length;

        // set up columns size + 1
        for (int col = 0; col <= size; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / (size + 1));
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        // set up rows: header A, Port A, header B, Port B
        for (int row = 0; row < 4; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(25.0);
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
        }

        portALabels = new Label[size];
        portBLabels = new Label[size];

        buildStaticUI();
        buildUI();
    }

    private void buildStaticUI() {
        gridPane.add(createLabel("", "port-header"), 0, 0);

        // RA7 … RA0 headers
        for (int bit = 0; bit < size; bit++) {
            String text = "RA" + bit;
            int col = size - bit;
            gridPane.add(createLabel(text, "port-header"), col, 0);
        }

       // Port A label in row 1
        gridPane.add(createLabel("Port A", "port-header"), 0, 1);

        // Port A data cells
        for (int bit = 0; bit < size; bit++) {
            final int idx = bit;
            Label cell = createLabel("", "port-cell");
            cell.setOnMouseClicked(_ -> {
                if (pic.memory.readBit_bank(5, idx, 1) == 1) {
                    pic.memory.PortA.pins[idx].toggleValue();
                    updateCell(cell,
                            pic.memory.PortA.pins[idx].getValue(),
                            pic.memory.readBit_bank(5, idx, 1));
                }
            });
            portALabels[idx] = cell;
            gridPane.add(cell, size - bit, 1);
        }

        // empty cell for RB headers row
        gridPane.add(createLabel("", "port-header"), 0, 2);

        // RB7 … RB0 headers
        for (int bit = 0; bit < size; bit++) {
            String text = "RB" + bit;
            int col = size - bit;
            gridPane.add(createLabel(text, "port-header"), col, 2);
        }

        // Port B label in row 3, col 0
        gridPane.add(createLabel("Port B", "port-header"), 0, 3);

        // Port B data cells
        for (int bit = 0; bit < size; bit++) {
            final int idx = bit;
            Label cell = createLabel("", "port-cell");
            cell.setOnMouseClicked(_ -> {
                // toggle only if pin is configured as input
                if (pic.memory.readBit_bank(6, idx, 1) == 1) {
                    pic.memory.PortB.pins[idx].toggleValue();
                    updateCell(cell,
                            pic.memory.PortB.pins[idx].getValue(),
                            pic.memory.readBit_bank(6, idx, 1));
                }
            });
            portBLabels[idx] = cell;
            gridPane.add(cell, size - bit, 3);
        }
    }

    /**
     * Called when a new PIC instance is loaded.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    /**
     * Refreshes all data cells with current pin values and input/output status.
     */
    public void buildUI() {
        gridPane.setCache(true);
        gridPane.setCacheHint(CacheHint.SPEED);

        for (int i = 0; i < size; i++) {
            updateCell(portALabels[i],
                    pic.memory.PortA.pins[i].getValue(),
                    pic.memory.readBit_bank(5, i, 1));
            updateCell(portBLabels[i],
                    pic.memory.PortB.pins[i].getValue(),
                    pic.memory.readBit_bank(6, i, 1));
        }

        gridPane.setCache(false);
    }

    /**
     * Factory method to create a Label with a CSS class and grow constraints.
     *
     * @param text     the label text (e.g. "RA0" or "")
     * @param cssClass the CSS class to apply ("port-header" or "port-cell")
     * @return a configured Label instance
     */
    private Label createLabel(String text, String cssClass) {
        Label lbl = new Label(text);
        lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        lbl.getStyleClass().add(cssClass);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        GridPane.setVgrow(lbl, Priority.ALWAYS);
        return lbl;
    }

    /**
     * Updates the label text (0/1) and applies the appropriate CSS class
     * for input (yellow) or output (green) based on the TRIS bit.
     *
     * @param lbl   the Label to update
     * @param value the current pin value (true = 1, false = 0)
     * @param tris  the TRIS bit (0 = output, 1 = input)
     */
    private void updateCell(Label lbl, boolean value, int tris) {
        lbl.setText(value ? "1" : "0");
        lbl.getStyleClass().removeAll("port-input", "port-output");
        lbl.getStyleClass().add(tris == 0 ? "port-output" : "port-input");
    }
}
