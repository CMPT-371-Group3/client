import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

// Deals with switching between some scenes and the Ready button
public class SceneController {
    @FXML
    private static volatile Stage stage;
    private static volatile Scene scene;
    private static volatile Parent root;
    @FXML
    private Button readyBtn;

    public static SceneController object;

    public SceneController() {
    }

    public static SceneController getInstance() {
        if (object == null) {
            synchronized (Client.class) {
                if (object == null) {
                    object = new SceneController();
                }
            }
        }
        return object;
    }

    public void initialize() {
        Client.getInstance().threadedListening();
    }

    // JavaFX code that switches the scene to the Main Page, linked to a button click event
    public void switchToMainPage(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/Main.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    // JavaFX code that switches the scene to the Game page, linked to a button click event
    public synchronized void readyButtonPressed(ActionEvent e) throws IOException {
        readyBtn.setDisable(true);
        readyBtn.setText("Waiting");

        root = FXMLLoader.load(getClass().getResource("Scenes/Game_Board.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        GameBoardController.getInstance().linkCanvas(scene);
        stage.show();
    }


}
