import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private PrintWriter output;
    private BufferedReader input;

    public static Client object;
    private Client() {
        try {
            this.socket = new Socket("localhost", 6000);
            this.os = socket.getOutputStream();
            this.is = socket.getInputStream();
            this.output = new PrintWriter(os, true);
            this.input = new BufferedReader(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Instance created");
    }

//    public void sendMessage(String msg) {
//        System.out.println("Send message method initiated");
//    }

    public void sendMessage(String payload) {
        try {
            // Write to the server
            this.output.println(payload);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    private String ipAddress;
    private int portNumber;
//    private Socket socket;
//    private OutputStream os;
//    private InputStream is;
//    private PrintWriter output;
//    private BufferedReader input;
    private String addressWithPort;
    private Scanner sc;
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

    public String getMessage() {
        return sc.nextLine();
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
