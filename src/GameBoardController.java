import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
        double size = 10;
        double x = mouseEvent.getX() - size/2;
        double y = mouseEvent.getY() - size/2;

        g.setFill(Color.BLACK);
        g.fillRect(x, y, size, size);

    }

//    public void initialize() {
//        GraphicsContext g = canvas.getGraphicsContext2D();
//        canvas.setOnMouseDragged(e -> {
//            double size = 10;
//            double x = e.getX() - size/2;
//            double y = e.getY() - size/2;
//
//            g.setFill(Color.BLACK);
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

    public void dragCellStart(MouseEvent e) {
        System.out.println("start drag");

        //check which grid this is and send this data
    }

    public void dragCellEnd(MouseEvent e) {
        System.out.println("Done drag");
        //calculate how much of the grid has been colored in, if more than 50 fill it in but otherwise dont do anything
    }
}








//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.image.Image;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//import javafx.scene.input.MouseEvent;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class GameBoardController {
////    private ArrayList<Cell> listOfCells = new ArrayList<>();
//    @FXML
//    private Canvas canvas;
//    private Parent root;
//    private Stage stage;
//    private Scene scene;
//
////    public void initializeCells() {
////        for (int i=0; i<64; i++) {
////            Cell cell = new Cell(i);
////            listOfCells.add(cell);
////            cell.draw();
////        }
////    }
//
////    public void callDraw() {
////        listOfCells.get(0).draw();
////    }
//    public void initialize() {
//        GraphicsContext g = canvas.getGraphicsContext2D();
//        canvas.setOnMouseDragged(e -> {
//            double size = 10;
//            double x = e.getX() - size/2;
//            double y = e.getY() - size/2;
//
//            g.setFill(Color.BLACK);
//            g.fillRect(x, y, size, size);
//        });
//    }
//
//    public void switchToMainPage(ActionEvent e) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("Scenes/Main.fxml"));
//        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        Image icon = new Image("Scenes/garfield_deny.jpg");
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void dragCellStart(MouseEvent e) {
//        System.out.println("start drag");
//
//        //check which grid this is and send this data
//    }
//
//    public void dragCellEnd(MouseEvent e) {
//        System.out.println("Done drag");
//        //calculate how much of the grid has been colored in, if more than 50 fill it in but otherwise dont do anything
//    }
//}
