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
//    private Scene givenScene;
//    private Parent givenRoot;
//    private Stage givenStage;

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
//                            this.switchToGameBoard();
//                            SceneController.getInstance().switchToGameBoard2();
                            gameStarted = true;
//                            SceneController.getInstance().notify();
//                            notify();
                            GameBoardController.getInstance().letUserPlay();
                            break;
                        case "LOCK":
                            System.out.println("SERVER LOCK");
                            String[] coordinates = tokens[1].split(",");
                            String owner = tokens[2];
                            int x = Integer.parseInt(coordinates[0]);
                            int y = Integer.parseInt(coordinates[1]);
//                            System.out.println("LOCK message from server for " + x + " " + y);
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
                            int winner = Integer.parseInt(tokens[1]);
                            GameBoardController.getInstance().setWinner(winner);
//                            GameBoardController.getInstance().switchToEndScreen2(winner);
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
//    private static final Object lock = new Object();

    //create a client object in gameboard, create a new thread

//    public Client(String ipAddress, int portNumber) {
//        try {
//            // Store any information needed then create the Socket, and the I/O
//            this.ipAddress = ipAddress;
//            this.portNumber = portNumber;
//            this.socket = new Socket(ipAddress, portNumber);
//            InputStream inputStream = socket.getInputStream();
//            OutputStream outputStream = socket.getOutputStream();
//            this.output = new PrintWriter(outputStream, true);
//            this.input = new BufferedReader(new InputStreamReader(inputStream));
//            this.addressWithPort = "";
//            this.sc = new Scanner(System.in);
//            this.msgSent = false;
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }


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

//    public void setGameBoardVars(Parent root,Stage stage,  Scene scene) {
//        this.givenRoot = root;
//        this.givenStage = stage;
//        this.givenScene = scene;
//    }

//    public void switchToGameBoard() throws IOException {
////        root = FXMLLoader.load(getClass().getResource("Scenes/Game_Board.fxml"));
////        stage = (Stage)((Node)givenEvent.getSource()).getScene().getWindow();
////        scene = new Scene(root);
//        Image icon = new Image("Scenes/garfield_deny.jpg");
//        givenStage.getIcons().add(icon);
//        givenStage.setScene(givenScene);
//        GameBoardController.getInstance().linkCanvas(givenScene);
//        givenStage.show();
//    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String listenForMessage() {
        try {
            // Listen for inputs and then pass them up
            return this.input.readLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return  null;
        }
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

//    public Status disconnect() {
//        try {
//            // Close the connection
//            this.input.close();
//            this.output.close();
//            this.socket.close();
//            return Status.SUCCESS;
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//            return Status.FAILURE;
//        }
//    }



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

    // @Override
    // public void run() {
    //     try {
    //         // listen for messages and pass them to the server
    //         while (true) {
    //             String message = this.Input.readLine();
    //             this.Server.handleClientMessage(message, this);
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error: " + e.getMessage());
    //     }
    // }

    public void setMsgSent(Boolean b) {
        this.msgSent = b;
    }

    public Boolean getMsgSent() {
        return this.msgSent;
    }

//    public class ClientThread implements Runnable {
//
//        private Client client;
//
//        public ClientThread(Client c) {
//            this.client = c;
//        }
//
//        public void run() {
//            try {
//                String serverMessage;
//                while((serverMessage = client.getInput().readLine()) != null) {
//                    switch (serverMessage) {
//                        case "EXIT":
//                            return;
//                        case "MESSAGE":
//                            System.out.println("\nIncoming: MESSAGE");
//                            String message = client.getInput().readLine();
//                            System.out.print(message);
//                            // Scanner scanner = new Scanner(System.in);
//                            message = sc.nextLine();
//                            System.out.println("message: " + message + " length: " + message.length());
//                            client.getOutput().println(message);
//                            client.getOutput().flush();
//                            break;
//                        default:
//                    }
//                    System.out.println("\nSelect an option:\nEXIT\nMESSAGE");
//                    System.out.print("Selection: ");
//                }
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}