import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Class that allows switching to the Join Game screen
public class MainScreenController {

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    // JavaFX code that switches the scene to the Join Game page, linked to a button click event
    public void switchToJoinGame(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/JoinGame.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Join Game");
        stage.setScene(scene);
        stage.show();
    }
}
