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

public class GameBoardController{
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static volatile int winner = -1;
//    private Scene givenScene;

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

    public void linkCanvas(Scene givenScene) {
//        System.out.println("IN the initialize function");
        this.winner = -1;
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                scene = givenScene;
                String canvasID = x + "," + y;
                Canvas currentCanvas =  (Canvas) scene.lookup("#" + canvasID);
                Cell cell = new Cell(currentCanvas, Color.TRANSPARENT, false, x, y, false);
                cells[x][y] = cell;
                System.out.println("BEFORE 47 " + cells[x][y].getCanvas().getHeight());
                cells[x][y].getCanvas().setOnMouseDragged(this::handleMouseDragged);
            }
        }
    }


    // **********************************************************************
    // these functions are to handle broadcast messages that come in from the server
    // i.e. when OTHER players lock, unlock, fill a cell
    public void lockCell(int x, int y, int owner) {
        cells[x][y].setLocked(true);
        cells[x][y].setOwner(owner);
        System.out.println("lockCell method called-  " + x + " " + y + " is locked: " + cells[x][y].isLocked());

        Canvas c = cells[x][y].getCanvas();
        GraphicsContext gc = c.getGraphicsContext2D();

        if(owner != Client.getInstance().getColorNumber()){
            gc.setFill(Color.DIMGREY);
            gc.fillRect(0,0, c.getWidth(), c.getHeight());
        }
    }

    public void unlockCell(int x, int y) {
        cells[x][y].setLocked(false);
        System.out.println("unlocked " + x + " " + y + " is locked: " + cells[x][y].isLocked());

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
    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        Canvas currentCanvas = (Canvas) mouseEvent.getSource();
        String[] coordinates = currentCanvas.getId().split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        Cell currentCell = cells[x][y];
//        System.out.println("TAKEN OVER? " + x + " " + y + currentCell.isTakenOver());
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
        double percentage = (double) ((pixelsDrawn/totalPixels) * 100);
//        System.out.println(percentage);
        return percentage;
    }

    public void releasedDrag(MouseEvent e) {
        Canvas currentCanvas = (Canvas) e.getSource();
        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        double percentageFilled = calculatePercentageDrawn(currentCanvas);

        String[] coordinates = currentCanvas.getId().split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        //**
        Cell currentCell = cells[x][y];
        if (!currentCell.isTakenOver()) {
            if (!currentCell.isLocked() || currentCell.getOwner()==Client.getInstance().getColorNumber()) {
                if (percentageFilled < 50) {
                    String unlockMsg = "UNLOCK/" + x + "," + y;
                    Client.getInstance().sendMessage(unlockMsg);
                    cells[x][y].setLocked(false);
                    g.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                } else {
//            g.setFill(Color.CORAL); // we can set this to the user's color later, for testing purposes it's black
                    g.setFill(Client.getInstance().getColor());
                    System.out.println("Fill color: " + Client.getInstance().getColor());
                    g.fillRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                    cells[x][y].setTakenOver(true);
                    String fillMsg = "FILL/" + x + "," + y;
                    Client.getInstance().sendMessage(fillMsg);
                }
            }
        }

    }

    public void switchToMainPage(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scenes/End_Screen.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Image icon = new Image("Scenes/garfield_deny.jpg");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEndScreen(ActionEvent e) throws IOException {
        if (winner != -1) {
//            EndScreenController.getInstance().setWinner(winner);
            root = FXMLLoader.load(getClass().getResource("Scenes/End_Screen.fxml"));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            Image icon = new Image("Scenes/garfield_deny.jpg");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void setWinner(int newWinner) {
        this.winner = newWinner;
        EndScreenController.getInstance().setWinner(newWinner);
    }

//    @FXML
//    public void switchToEndScreen2(int winner) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scenes/End_Screen.fxml"));
//        Parent newRoot = loader.load();
//        stage = (Stage) scene.getWindow();
//        scene = new Scene(newRoot);
//        Image icon = new Image("Scenes/garfield_deny.jpg");
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
////        EndScreenController.getInstance().setWinner(winner);
//        stage.show();
////        EndScreenController.getInstance().setText();
//
////        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scenes/End_Screen.fxml"));
////        Parent newRoot;
////        try {
////            root = (Parent) loader.load();
////            EndScreenController controller = (EndScreenController) loader.getController();
////            controller.
////        } catch (IOException e)
//    }
}