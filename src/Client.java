import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;


// The Client class, handles opening the socket, sending the server messages, and receiving messages
// from the server. Also opens another thread so that the client can listen for broadcast messages
// coming from the other players.
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

    //function that starts a new thread, starts listening to the server, and deals with the
    // token-based messages
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

    public Color getColorOf(int owner){
        return switch (owner) {
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.PURPLE;
            default -> Color.BLACK;
        };
    }
}