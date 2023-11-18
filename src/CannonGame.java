import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//CannonGame class
public class CannonGame extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    private static final double CANNON_SIZE = 50;
    private static final int TIME_LIMIT = 10;

    private Cannon cannon;
    private List<Target> targets;
    private Blocker blocker;
    private double timeRemaining = TIME_LIMIT;
    private int shotsFired = 0;
    private long lastUpdateTime = -1;
    private Label time= new Label();
    AnimationTimer gameLoop;
    
    public static void main(String[] args) {
        launch(args);
    }

    //Start function to set the Canvas and initialize the game elements and animation timer
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        time.setMinSize(100,100);
        time.setLayoutX(5);
        time.setLayoutY(5);
        root.getChildren().addAll(canvas,time);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        initializeGameElements();

          gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == -1) {
                	lastUpdateTime = now;
                }
                
                double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = now;
                timeRemaining -= elapsedTime;
                
                if (timeRemaining <= 0) {
                    try {
                        gameOver(false);
                    } 
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            
                updateGameElements(elapsedTime);
                checkCollisions();
                drawGameElements(gc);

                if (targets.isEmpty()) {
                    try {
                        gameOver(true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        
        gameLoop.start(); //Start the Animation timer

        //Mouse click event to fire the cannon ball
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Point2D clickPoint = new Point2D(event.getX(), event.getY());
            cannon.aimAndFire(clickPoint);
            shotsFired++;
        } );

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cannon Game");
        primaryStage.show();
    }

    //This function initializes the cannon, blocker and 9 targets
    private void initializeGameElements() {
    	cannon = new Cannon(0, 300, CANNON_SIZE);
        blocker = new Blocker(WIDTH / 2, HEIGHT / 2, 20, 150);

        targets = new ArrayList<>();
        targets.add(new Target(450, 350, 20, 100,160));
        targets.add(new Target(480, 250, 20, 100,150));
        targets.add(new Target(510, 300, 20, 100,160));
        targets.add(new Target(540, 250, 20, 100,150));
        targets.add(new Target(570, 400, 20, 100,160));
        targets.add(new Target(600, 320, 20, 100,150));
        targets.add(new Target(630, 220, 20, 100,160));
        targets.add(new Target(660, 310, 20, 100,150));
        targets.add(new Target(690, 270, 20, 100,150));

    }
    
    //This function updates the time, score and checks the blocker/targets hit and position
    private void updateGameElements(double elapsedTime) {
        cannon.update(elapsedTime);
        blocker.update(elapsedTime);
        time.setText("Time Remaining: "+String.format("%.2f", timeRemaining)+" Seconds");
        targets = targets.stream().filter(Target::isAlive).collect(Collectors.toList());
        targets.forEach(target -> target.update(elapsedTime));
    }

    //This function is to check whether the cannon ball hit a target or a blocker
    private void checkCollisions() {
        for (Target target : targets) {
            if (cannon.cannonballIntersects(target.getBounds())) {
                target.hit();
                targetHitSound();
                timeRemaining += 3;              
            }
        }

        if (cannon.cannonballIntersects(blocker.getBounds())) {
            cannon.reflectCannonball(blocker.getBounds());
            blockerHitSound();
            timeRemaining -= 3;
        }
    }

    private void drawGameElements(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        cannon.draw(gc);
        blocker.draw(gc);
        for (int i = 0; i < targets.size(); i++) {
            if (i % 2 == 0) {
                targets.get(i).draw(gc, Color.BLUE);
            } else {
                targets.get(i).draw(gc, Color.YELLOW);
            }
        }
    }

    //This function is to display the alerts when the game is over
    private void gameOver(boolean won) {
        double time;
    	gameLoop.stop();
        
    	if((TIME_LIMIT - timeRemaining) < 10)
    		time = TIME_LIMIT;
    	else
    		time = TIME_LIMIT - timeRemaining;
    	
        Alert alert = new Alert(won ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle("Cannon Game");
        alert.setHeaderText(won ? "You won!" : "You lost!");
        alert.setContentText("Shots fired: " + shotsFired + "\nTime elapsed: " + (int) (time) + " seconds");
        Platform.runLater(() -> {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        });

    }
    
    //Functions to add and play audio files
    public static void cannonFireSound()
    {
    	AudioClip cannonSound = new AudioClip(CannonGame.class.getResource("/cannon_fire.wav").toString());
        cannonSound.play();
    }
    
    private void targetHitSound()
    {
    	AudioClip targetSound = new AudioClip(getClass().getResource("/target_hit.wav").toString());
        targetSound.play();
    }
    
    private void blockerHitSound()
    {
    	AudioClip blockerSound = new AudioClip(getClass().getResource("/blocker_hit.wav").toString());
        blockerSound.play();
    }
}