package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //TODO: Java doc
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Lade die FXML-Datei aus dem Ressourcenpfad
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/main_view.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("JavaFX FXML main.Main Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    //TODO: Java doc
    // To run the GUI do "launch(args)"
    // To run another main methode do "OtherClass.main(args)"
    public static void main(String[] args) {
        //launch(args);
        Test.main(args);
    }
}
