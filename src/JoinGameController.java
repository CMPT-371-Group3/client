import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class JoinGameController {

    @FXML
    private TextField inputText;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMainPage(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/Main.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

//    public void joinGameBTN(ActionEvent e)

    public void switchToHostWaiting(ActionEvent e) throws IOException {

        //HAVE TO DO MORE WORK HERE TO CHECK - IF CAN FIND THE CONNECTION
        String ipAddress = inputText.getText();

        makeConnection(ipAddress);


        root = FXMLLoader.load(getClass().getResource("Scenes/Host_Waiting_Screen.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Deny and Conquer: Waiting Room");
        stage.setScene(scene);
        stage.show();
    }

    public void makeConnection(String ipAddress){
        try {
            Socket MySocket = new Socket(ipAddress, 7070);
//            OutputStream os
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}