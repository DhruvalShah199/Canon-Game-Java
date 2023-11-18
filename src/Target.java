import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Target class
public class Target {
    private double x, y;
    private double width, height;
    private double dy;
    private boolean alive = true;
    private Color color;

    //Constructor to set the Targets
    public Target(double x, double y, double width, double height, double dy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dy = dy;
    }

    //Update function to move the targets
    public void update(double elapsedTime) {

        y += dy * elapsedTime;

        if (y < 0 || y + height > 700) {
            dy = -dy;
        }
    }

    public void draw(GraphicsContext gc, Color color) {
        if (alive) {
            gc.setFill(color);
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        }
    }

    //Boolean function to check if the target is hit
    public boolean isAlive() {
        return alive;
    }
    
    //This function sets the alive variable to false if the target is hit 
    public void hit() {
        alive = false;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}