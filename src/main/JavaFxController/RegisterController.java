package main.JavaFxController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.PIC;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the RegisterView FXML, now reloadable when a new PIC is loaded.
 */
public class RegisterController implements Initializable {
    public static RegisterController instance;

    @FXML private Label statusLabel;
    @FXML private Label W;
    @FXML private Label RP0;
    @FXML private Label Z;
    @FXML private Label DC;
    @FXML private Label C;
    @FXML private Label WValue;
    @FXML private Label PCL;
    @FXML private Label PCLLabel;
    @FXML private Label cell41;

    private PIC pic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Register this controller as the central instance
        MainController.registerController = this;
        instance = this;
        // Get the currently loaded PIC
        this.pic = MainController.getStaticPic();
        // Build the UI based on the PIC state
        buildUI();
    }

    /**
     * Called by MainController when a new PIC is loaded.
     */
    public void updatePIC(PIC newPic) {
        this.pic = newPic;
        buildUI();
    }

    /**
     * Updates all labels according to the current PIC's registers.
     */
    private void buildUI() {
        if (pic == null) {
            // Clear or reset to defaults
            W.setText("W: ");
            WValue.setText("");
            RP0.setText("RP0: ");
            Z.setText("Z: ");
            PCL.setText("PCL: ");
            PCLLabel.setText("");
            DC.setText("DC: ");
            C.setText("C: ");
            cell41.setText("");
            return;
        }

        // Example getters on PIC; adapt to your PIC API
        W.setText("W: ");
        WValue.setText(String.format("0x%02X", pic.getW()));
        RP0.setText("RP0: " + String.format("0x%02X", pic.memory.getRP0()));
        Z.setText("Z: " + pic.memory.get_Z());
        PCL.setText("PCL: ");
        PCLLabel.setText(String.format("0x%02X", pic.memory.getPC()));
        DC.setText("DC: " + pic.memory.get_DC());
        C.setText("C: " + pic.memory.get_C());
    }
}