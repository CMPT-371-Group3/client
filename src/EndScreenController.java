import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class EndScreenController {

    public static EndScreenController object;
    private static volatile int winner = -3;
    @FXML
    private Label winnerText;

    public EndScreenController() {
//        winner = -2;
//        System.out.println("CONSTRUCTOR CALLED, winner ="+ winner);
    }

    public static EndScreenController getInstance() {
        if (object == null) {
            synchronized (Client.class) {
                if (object == null) {
                    object = new EndScreenController();
                }
            }
        }
        return object;
    }

    public void initialize(){
//        System.out.println("INITIALIZE IS PRINTING: winner is" + winner);
//        winnerText.setText("Winner = " + winner);
        switch (winner) {
            case 1:
                winnerText.setText("Winner = " + "RED");
                break;
            case 2:
                winnerText.setText("Winner = " + "BLUE");
                break;
            case 3:
                winnerText.setText("Winner = " + "GREEN");
                break;
            case 4:
                winnerText.setText("Winner = " + "PURPLE");
                break;
            default:
                winnerText.setText("Winner = " + "NONE");
                break;
        }

    }

    public void setWinner(int winnerNumber){
//        System.out.println("SETTING WINNER");
        this.winner = winnerNumber;
//        System.out.println("WINNER SET TO " + winner);
//        winnerText.setText("Winner = " + winner);
    }

//    public void setText(){
//        winnerText.setText("Winner = " + winner);
//    }

}
