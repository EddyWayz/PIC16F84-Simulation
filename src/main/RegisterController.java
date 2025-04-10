package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RegisterController {

    @FXML private Label cell00;
    @FXML private Label cell01;
    @FXML private Label cell10;
    @FXML private Label cell11;
    @FXML private Label cell20;
    @FXML private Label cell21;
    @FXML private Label cell30;
    @FXML private Label cell31;
    @FXML private Label cell40;
    @FXML private Label cell41;

    public void initialize() {
        // Beispiel: Den Text und die Hintergrundfarbe einer Zelle zur Laufzeit ändern.
        cell10.setText("Neu konfiguriert");
        cell10.setStyle("-fx-background-color: yellow; -fx-alignment: center;");
    }
}
