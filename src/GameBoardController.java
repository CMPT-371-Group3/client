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

// The GameBoardController handles the front-end game logic. Any game board updates from the other players
// are reflected in the front-end using this class. This also holds the 2D array of Cells
public class GameBoardController{
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static volatile int winner = -1;
    private static volatile boolean everyoneInGame;

    public static volatile Cell[][] cells = new Cell[8][8];
    public static GameBoardController object;

    public GameBoardController() {
    }

    public static GameBoardController getInstance() {
        if (object == null) {
            synchronized (Client.class) {
                if (object == null) {
                    object = new GameBoardController();
                }
            }
        }
        return object;
    }

    // Links every canvas in the scene to a Canvas object in the GameBoardController
    public void linkCanvas(Scene givenScene) {
        winner = -1;
        everyoneInGame = false;

        // initializing all the Cells
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                scene = givenScene;
                String canvasID = x + "," + y;
                Canvas currentCanvas =  (Canvas) scene.lookup("#" + canvasID);
                Cell cell = new Cell(currentCanvas, Color.TRANSPARENT, false, x, y, false);
                cells[x][y] = cell;
                cells[x][y].getCanvas().setOnMouseDragged(this::handleMouseDragged);
            }
        }
        Client.getInstance().sendMessage("READY");
    }

    public void letUserPlay(){
        everyoneInGame = true;
    }

    // **********************************************************************
    // these functions are to handle broadcast messages that come in from the server
    // i.e. when OTHER players lock, unlock, fill a cell
    public void lockCell(int x, int y, int owner) {
        cells[x][y].setLocked(true);
        cells[x][y].setOwner(owner);

        Canvas c = cells[x][y].getCanvas();
        GraphicsContext gc = c.getGraphicsContext2D();

        if(owner != Client.getInstance().getColorNumber()){
            gc.setFill(Color.DIMGREY);
            gc.fillRect(0,0, c.getWidth(), c.getHeight());
        }
    }

    public void unlockCell(int x, int y) {
        cells[x][y].setLocked(false);

        Canvas c = cells[x][y].getCanvas();
        GraphicsContext gc = c.getGraphicsContext2D();

        gc.clearRect(0,0, c.getWidth(), c.getHeight());
    }

    public void fillCell(int x, int y, int owner) throws IOException {
        cells[x][y].setTakenOver(true);
        cells[x][y].setOwner(owner);

        Canvas c = cells[x][y].getCanvas();
        GraphicsContext gc = c.getGraphicsContext2D();

        gc.setFill(Client.getInstance().getColorOf(owner));
        gc.fillRect(0,0, c.getWidth(), c.getHeight());
    }
    // ***********************************************************************

    //Deals with the actual drawing on the cells, displaying these changes and sending lock messages to the server
    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        if(everyoneInGame) {
            Canvas currentCanvas = (Canvas) mouseEvent.getSource();
            String[] coordinates = currentCanvas.getId().split(",");

            //identifying the cell that is currently being drawn in
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            Cell currentCell = cells[x][y];
            if (!currentCell.isTakenOver()) {
                if (!currentCell.isLocked()) {
                    currentCell.setLocked(true);
                    String lockMsg = "LOCK/" + x + "," + y;
                    Client.getInstance().sendMessage(lockMsg);
                }
                if (currentCell.getOwner() == Client.getInstance().getColorNumber()) {
                    GraphicsContext g = currentCanvas.getGraphicsContext2D();
                    double size = 5;
                    double xCoord = mouseEvent.getX() - size;
                    double yCoord = mouseEvent.getY() - size;
                    g.setFill(Client.getInstance().getColor());
                    g.fillRect(xCoord, yCoord, size, size);
                }
            }
        }
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
                if (color.equals(Client.getInstance().getColor())) {
                    pixelsDrawn++;
                }
            }
        }
        double totalPixels = height * width;
        double percentage = (pixelsDrawn/totalPixels) * 100;
        return percentage;
    }

    // When a player releases their mouse after drawing, it checks how much they drew in and either locks or unlocks the cell
    public void releasedDrag(MouseEvent e) {
        if(everyoneInGame) {
            Canvas currentCanvas = (Canvas) e.getSource();
            GraphicsContext g = currentCanvas.getGraphicsContext2D();
            double percentageFilled = calculatePercentageDrawn(currentCanvas);

            String[] coordinates = currentCanvas.getId().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            Cell currentCell = cells[x][y];
            if (!currentCell.isTakenOver()) {
                if (!currentCell.isLocked() || currentCell.getOwner() == Client.getInstance().getColorNumber()) {
                    if (percentageFilled < 50) {
                        String unlockMsg = "UNLOCK/" + x + "," + y;
                        Client.getInstance().sendMessage(unlockMsg);
                        cells[x][y].setLocked(false);
                        g.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                    } else {
                        g.setFill(Client.getInstance().getColor());
                        g.fillRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                        cells[x][y].setTakenOver(true);
                        String fillMsg = "FILL/" + x + "," + y;
                        Client.getInstance().sendMessage(fillMsg);
                    }
                }
            }
        }
    }

    // JavaFX code that switches the scene to the End Screen, linked to a button click event
    public void switchToEndScreen(ActionEvent e) throws IOException {
        if (winner != -1) {
            root = FXMLLoader.load(getClass().getResource("Scenes/End_Screen.fxml"));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            Image icon = new Image("Scenes/garfield_deny.jpg");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void setWinners(int[] winners) {
        winner = winners[0];
        EndScreenController.getInstance().setWinners(winners);
    }

    public void setWinner(int newWinner) {
        winner = newWinner;
        EndScreenController.getInstance().setWinner(newWinner);
    }
}