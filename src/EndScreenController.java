import javafx.fxml.FXML;
import javafx.scene.control.Label;

// Class that handles the final screen when the game is over.
// Displays the winner(s) of the game
public class EndScreenController {

    public static EndScreenController object;
    private static volatile int winner = -3;
    private static volatile int[] winnersArray = {};
    private static volatile boolean multipleWinners = false;
    @FXML
    private Label winnerText;

    public EndScreenController() {
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

    public String determineColor(int color) {
        return switch (color) {
            case 1 -> "RED";
            case 2 -> "BLUE";
            case 3 -> "GREEN";
            case 4 -> "PURPLE";
            default -> "NONE";
        };
    }

    public void initialize(){
        if (multipleWinners) {
            String resultText = "There was a tie! The winners are: ";
            for (int winner: winnersArray) {
                resultText += determineColor(winner);
                resultText += ", ";
            }
            winnerText.setText(resultText);
        } else {
            winnerText.setText("Winner is: " + determineColor(winner));
        }

    }

    public void setWinners(int[] winners) {
        multipleWinners = true;
        winnersArray = winners;
    }

    public void setWinner(int winnerNumber){
        winner = winnerNumber;

    }

}
