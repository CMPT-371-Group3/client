import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.canvas.*;
//import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.*;

import java.io.IOException;

public class SceneController {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
//    private Canvas canvas;

    //adding a listener to be able to draw on the canvas
    //called automatically by the fxml loader
//    public void initialize() {
//        GraphicsContext g = canvas.getGraphicsContext2D();
//        canvas.setHeight(20);

//        canvas.setOnMouseDragged(e -> {
//            double size = 5.0;
//            double x = e.getX() - size / 2;
//            double y = e.getY() - size / 2;
//
//            g.setFill(Color.RED);
//            g.fillRect(x, y, size, size);
//        });
//    }

    public void switchToMainPage(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/Main.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHostWaiting(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/Host_Waiting_Screen.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Deny and Conquer: Waiting Room");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEndPage(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/End_Screen.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToGameBoard(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/Game_Board.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

}
