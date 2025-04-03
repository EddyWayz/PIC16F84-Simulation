package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));

        // Hole die Abmessungen des primären Bildschirms
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Erstelle eine Scene, die den gesamten Bildschirm ausfüllt
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        primaryStage.setTitle("PIC16F84-Simulation");
        primaryStage.setScene(scene);

        // Setze die Fensterposition und -größe so, dass sie 100% des Bildschirms einnimmt
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // primaryStage.setFullScreen(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
