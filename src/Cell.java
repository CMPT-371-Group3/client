import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Cell {
    private Canvas canvas;
    private Color color;
    private boolean isTakenOver;
    private final int x;
    private final int y;

    public Cell(Canvas canvas, Color color, boolean isTakenOver, int x, int y) {
        this.canvas = canvas;
        this.color = color;
        this.isTakenOver = isTakenOver;
        this.x = x;
        this.y = y;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTakenOver(boolean takenOver) {
        isTakenOver = takenOver;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Color getColor() {
        return color;
    }

    public boolean isTakenOver() {
        return isTakenOver;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
