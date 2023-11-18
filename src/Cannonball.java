import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Cannonball Class
public class Cannonball {
    private static final double SPEED = 400;
    private double x, y;
    private double dx, dy;

    //Constructor to set the Cannon Ball
    public Cannonball(double startX, double startY, double angle) {
        this.x = startX;
        this.y = startY;
        this.dx = SPEED * Math.cos(angle);
        this.dy = SPEED * Math.sin(angle);
    }

    //Update function to move the cannon ball
    public void update(double elapsedTime) {
        x += dx * elapsedTime;
        y -= dy * elapsedTime;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x - 5, y - 5, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 5, y - 5, 10, 10);
    }

    //This function reflects the cannon ball on hitting the blocker
    public void reflect(Bounds bounds) {
        if (x - 5 < bounds.getMinX() || x + 5 > bounds.getMaxX()) {
            dx = -dx;
        }
        if (y - 5 < bounds.getMinY() || y + 5 > bounds.getMaxY()) {
            dy = -dy;
        }
    }
}