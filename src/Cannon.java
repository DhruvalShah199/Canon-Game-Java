import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Cannon class
public class Cannon {
    private double x, y;
    private double size;
    private Cannonball cannonball;
    private double barrelAngle;

    //Constructor to set the Cannon
    public Cannon(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.barrelAngle = Math.PI / 4;

    }

    //Update function to move the cannon
    public void update(double elapsedTime) {
        if (cannonball != null) {
            cannonball.update(elapsedTime);
        }
    }

    public void draw(GraphicsContext gc) {

        gc.setFill(Color.BLACK);
        gc.fillOval(x - size / 2, y - size / 2, size, size);

        double barrelLength = size * 1.5;
        double barrelWidth = size / 2;
        double barrelEndX = x + barrelLength * Math.cos(barrelAngle);
        double barrelEndY = y - barrelLength * Math.sin(barrelAngle);
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(barrelWidth);
        gc.strokeLine(x, y, barrelEndX, barrelEndY);

        if (cannonball != null) {
            cannonball.draw(gc);
        }
    }

    //This function gets the mouse click point and fires the cannon in that direction
    public void aimAndFire(Point2D clickPoint) {
        barrelAngle = Math.atan2(y - clickPoint.getY(), clickPoint.getX() - x);
        cannonball = new Cannonball(x, y, barrelAngle);
        
        CannonGame.cannonFireSound();
    }

    //This function checks if the cannon ball intersects the targets/blocker
    public boolean cannonballIntersects(Rectangle target) {
        if (cannonball == null) {
            return false;
        }

        return cannonball.getBounds().intersects(target.getBoundsInParent());
    }

    //This function blocks the cannon ball on hitting
    public void reflectCannonball(Rectangle blocker) {
        if (cannonball == null) {
            return;
        }
        cannonball.reflect(blocker.getBoundsInParent());
    }
}