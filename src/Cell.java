import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

// The Cell class handles all the operations and data of a single cell on the board
public class Cell {
    private Canvas canvas;
    private Color color;
    private boolean isTakenOver;
    private final int x;
    private final int y;
    private boolean isLocked;
    private int owner;

    public Cell(Canvas canvas, Color color, boolean isTakenOver, int x, int y, boolean isLocked) {
        this.canvas = canvas;
        this.color = color;
        this.isTakenOver = isTakenOver;
        this.x = x;
        this.y = y;
        this.isLocked = false;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void setTakenOver(boolean takenOver) {
        isTakenOver = takenOver;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public boolean isTakenOver() {
        return isTakenOver;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}