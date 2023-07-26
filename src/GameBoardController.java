import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;

public class GameBoardController {
//    private ArrayList<Cell> listOfCells = new ArrayList<>();
    @FXML
//    private Canvas canvas;
    private Parent root;
    private Stage stage;
    private Scene scene;

    private Canvas[][] canvases = new Canvas[2][2];
    private GraphicsContext graphicsContext;

    public void initialize() {
        for(int row = 0; row <2; row++){
            for(int col = 0; col < 2; col++) {
                canvases[row][col] = new Canvas();
                canvases[row][col].setOnMouseDragged(this::handleMouseDragged);
            }
        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        Canvas currentCanvas = (Canvas) mouseEvent.getSource();
        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        double size = 2.5;
        double x = mouseEvent.getX() - size/2;
        double y = mouseEvent.getY() - size/2;

        g.setFill(Color.BLACK);
        g.fillRect(x, y, size, size);

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

    public double calculatePercentageDrawn(Canvas canvas) {
        int height = (int) canvas.getWidth();
        int width = (int) canvas.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        PixelReader pixelReader = writableImage.getPixelReader();

        int pixelsDrawn = 0;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Color color = pixelReader.getColor(x,y);
                if (color.equals(Color.BLACK)) {
                    pixelsDrawn++;
                }
            }
        }
        int totalPixels = width * height;
        double percentage = (double) pixelsDrawn/totalPixels * 100;
        return percentage;
    }

    public void releasedDrag(MouseEvent e) {
        Canvas currentCanvas = (Canvas) e.getSource();
        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        double percentageFilled = calculatePercentageDrawn(currentCanvas);
        if (percentageFilled < 50) {
            g.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
        } else {
            // will need to send a message to server saying this user has taken ownership
            // of this cell
            System.out.println("User drew more than 50%!");
            g.setFill(Color.BLACK); // we can set this to the user's color later, for testing purposes it's black
            g.fillRect(0,0, currentCanvas.getWidth(), currentCanvas.getHeight());
        }
    }
}
