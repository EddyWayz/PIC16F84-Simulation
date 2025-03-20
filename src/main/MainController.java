package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private Button myButton;

    @FXML
    private void initialize() {
        // Beispiel: Aktion definieren
        myButton.setOnAction(e -> System.out.println("Button clicked in MainApp!"));
    }
}
