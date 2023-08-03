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

public class GameBoardController {
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;

//    private Canvas[][] canvases = new Canvas[8][8];
    private final Cell[][] cells = new Cell[8][8];

    public void initialize() {
        for (int row=0; row<8; row++) {
            for (int col=0; col<8; col++) {
                Canvas c = new Canvas();
                Cell cell = new Cell(c, Color.TRANSPARENT, false, row, col);
                cells[row][col] = cell;
                cells[row][col].getCanvas().setOnMouseDragged(this::handleMouseDragged);
            }
        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        Canvas currentCanvas = (Canvas) mouseEvent.getSource();
        String[] coordinates = currentCanvas.getId().split(",");
        int row = Integer.parseInt(coordinates[0]);
        int col = Integer.parseInt(coordinates[1]);
        // construct a LOCK/x,y String to send to server
        String lockMsg = "LOCK/" + row + "," + col;
//        Client.getInstance().sendMessage(lockMsg);
//        System.out.println(lockMsg);

        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        double size = 2;
        double x = mouseEvent.getX() - size;
        double y = mouseEvent.getY() - size;
        g.setFill(Color.LIMEGREEN);
        g.fillRect(x, y, size, size);
    }

    public double calculatePercentageDrawn(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        double pixelsDrawn = 0;

        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        PixelReader pixelReader = writableImage.getPixelReader();

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Color color = pixelReader.getColor(x, y);
                if (color.equals(Color.LIMEGREEN)) {
                    pixelsDrawn++;
                }
            }
        }
        double totalPixels = height * width;
        double percentage = (double) ((pixelsDrawn/totalPixels) * 100);
//        System.out.println(percentage);
        return percentage;
    }

    public void releasedDrag(MouseEvent e) {
        Canvas currentCanvas = (Canvas) e.getSource();
        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        double percentageFilled = calculatePercentageDrawn(currentCanvas);

        String[] coordinates = currentCanvas.getId().split(",");
        int row = Integer.parseInt(coordinates[0]);
        int col = Integer.parseInt(coordinates[1]);
        if (percentageFilled < 50) {
            String unlockMsg = "UNLOCK/" + row + "," + col;
//            Client.getInstance().sendMessage(unlockMsg);
            System.out.println(unlockMsg);
            g.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
        } else {
            g.setFill(Color.CORAL); // we can set this to the user's color later, for testing purposes it's black
            g.fillRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
            cells[row][col].setTakenOver(true);
            // send message to server after constructing a string "FILL/X,Y"
            String fillMsg = "FILL/" + row + "," + col;
//            Client.getInstance().sendMessage(fillMsg);
            System.out.println(fillMsg);
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
}
