import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Blocker class
public class Blocker {
	private double x, y;
	private double width, height;
	private double dy;

	//Constructor to set the blocker 
	public Blocker(double x, double y, double width, double height) {
		this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dy = 100;  
	}
	
	//Update function to move the blocker vertically
	public void update(double elapsedTime) {
		y += dy * elapsedTime;

        if (y < 0 || y + height > 600) {
        	dy = -dy;
        }    
	}

	public void draw(GraphicsContext gc) {
		gc.setFill(Color.GRAY);
        gc.fillRect(x - width / 2, y - height / 2, width, height);    
	}

	public Rectangle getBounds() {
		return new Rectangle(x - width / 2, y - height / 2, width, height);
		}
}