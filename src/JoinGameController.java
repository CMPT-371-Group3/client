import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//import java.awt.*;
//join game
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class JoinGameController {

    @FXML
    private TextField inputText;
    @FXML
    private TextField portInput;
    @FXML
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

    public void joinBtnClicked(ActionEvent e) throws IOException {
        String ip = inputText.getText();
        String portNumber = portInput.getText();

        Client.getInstance().setIpAddress(ip);
        Client.getInstance().setPortNumber(Integer.parseInt(portNumber));

        Client.getInstance().makeConnection();
        Client.getInstance().sendMessage("JOIN");

        SceneController.getInstance();
        root = FXMLLoader.load(getClass().getResource("Scenes/Host_Waiting_Screen.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Deny and Conquer: Waiting Room");
        stage.setScene(scene);
        stage.show();
    }

}
