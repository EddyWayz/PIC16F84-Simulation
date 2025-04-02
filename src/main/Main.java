package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        primaryStage.setTitle("PIC16F84-Simulation");
        primaryStage.setScene(new Scene(root, 1800, 1200));
        primaryStage.show();

/*
        InstructionParser instParser = new InstructionParser(path);
        ArrayList<Integer> list = instParser.parseLinesToInstructions();
        for (Integer i : list) {
            System.out.println(Integer.toHexString(i.intValue()));
        }
        */
    }

    public static void main(String[] args) {
        launch(args);

    }
}
