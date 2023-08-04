import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.canvas.*;
//import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class SceneController {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label ipLabel;
    @FXML
    private Button readyBtn;

    public void initialize() {
        Client.getInstance().threadedListening();

        if(ipLabel != null){ //code for the waiting screen and not the main screen
            try {
                String ipAddress = Inet4Address.getLocalHost().getHostAddress();
                ipLabel.setText("Your Host IP Address: " + ipAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                ipLabel.setText("Unable to fetch IP address");
            }
        }
    }

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

    public void switchToJoinGame(ActionEvent e) throws  IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/JoinGame.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Join Game");
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

    public void readyButtonPressed(ActionEvent e) throws IOException {
        readyBtn.setDisable(true);
        readyBtn.setText("Waiting");
    }

    public void switchToGameBoard(ActionEvent e) throws IOException {
        //stop listening
        root = FXMLLoader.load(getClass().getResource("Scenes/Game_Board.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

}
