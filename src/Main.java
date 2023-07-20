//package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Objects;

public class Main extends Application {
    public  static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sceneBuilderFile.fxml")));

//        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);

        stage.setTitle("Stage demo");

        stage.setScene(scene);
        stage.show();
    }
}