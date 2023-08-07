import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private PrintWriter output;
    private BufferedReader input;
    private int colorNumber;
    private Color color;
    private String ipAddress;
    private int portNumber;
    private boolean gameStarted;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Client object;
    private Client() {
        this.ipAddress = "";
        this.portNumber = 0;
    }

    public static Client getInstance() {
        if (object == null) {
            synchronized (Client.class) {
                if (object == null) {
                    object = new Client();
                }
            }
        }
        return object;
    }

    public void makeConnection() throws IOException {
        this.socket = new Socket(this.ipAddress, portNumber);
        this.os = socket.getOutputStream();
        this.is = socket.getInputStream();
        this.output = new PrintWriter(os, true);
        this.input = new BufferedReader(new InputStreamReader(is));
        this.gameStarted = false;
    }

    public void sendMessage(String payload) {
        try {
            // Write to the server
            this.output.println(payload);
            this.output.flush();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    public void threadedListening() {
        new Thread(()-> {
            try {
                String serverMessage;
                while((serverMessage = input.readLine()) != null){
                    System.out.println(serverMessage);
                    String[] tokens = serverMessage.split("/");
                    switch (tokens[0]) {
                        case "EXIT":
                            return;
                        case "START":
                            System.out.println("SERVER START");
                            gameStarted = true;
                            GameBoardController.getInstance();
                            GameBoardController.getInstance().letUserPlay();
                            break;
                        case "LOCK":
                            System.out.println("SERVER LOCK");
                            String[] coordinates = tokens[1].split(",");
                            String owner = tokens[2];
                            int x = Integer.parseInt(coordinates[0]);
                            int y = Integer.parseInt(coordinates[1]);
                            GameBoardController.getInstance().lockCell(x, y, Integer.parseInt(owner));
                            break;
                        case "UNLOCK":
                            System.out.println("SERVER UNLOCK");
                            coordinates = tokens[1].split(",");
                            x = Integer.parseInt(coordinates[0]);
                            y = Integer.parseInt(coordinates[1]);
                            GameBoardController.getInstance().unlockCell(x, y);
                            break;
                        case "STOP":
                            if (tokens[1].length() > 1) {
                                String[] winners = tokens[1].split(",");
                                int[] winnersArray = new int[winners.length];
                                for (int i=0; i<winners.length; i++) {
                                    System.out.println("winner " + i + ": " + Integer.parseInt(winners[i]));
                                    winnersArray[i] = Integer.parseInt(winners[i]);
                                }
                                GameBoardController.getInstance().setWinners(winnersArray);
                            } else {
                                int winner = Integer.parseInt(tokens[1]);
                                GameBoardController.getInstance().setWinner(winner);
                            }
                            break;
                        case "FILL":
                            System.out.println("SERVER FILL");
                            coordinates = tokens[1].split(",");
                            owner = tokens[2];
                            x = Integer.parseInt(coordinates[0]);
                            y = Integer.parseInt(coordinates[1]);
                            GameBoardController.getInstance().fillCell(x, y, Integer.parseInt(owner));
                            break;
                        case "PLAYER_NUMBER":
                            String colorMsg = tokens[1];
                            this.colorNumber = Integer.parseInt(colorMsg);
                            switch (this.colorNumber) {
                                case 1 -> this.color = Color.RED;
                                case 2 -> this.color = Color.BLUE;
                                case 3 -> this.color = Color.GREEN;
                                case 4 -> this.color = Color.PURPLE;
                            }
                            System.out.println("Player number is: " + this.colorNumber);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String addressWithPort;
    private Boolean msgSent;

    public Color getColor() {
        return color;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public Color getColorOf(int owner){
        switch (owner) {
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.PURPLE;
            default:
                return Color.BLACK;
        }
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public PrintWriter getOutput() {
        return this.output;
    }

    public BufferedReader getInput() {
        return this.input;
    }

    public void setAddressWithPort(String address) {
        this.addressWithPort = address;
    }

    public String getAddressWithPort() {
        return this.addressWithPort;
    }

    public void setMsgSent(Boolean b) {
        this.msgSent = b;
    }

    public Boolean getMsgSent() {
        return this.msgSent;
    }

}