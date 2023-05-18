package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

/**
 * Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 *
 * @author Yair Stern
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator implements CollisionStrategy {
    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private final WindowController windowController;
    private final GameManager gameManager;

    /**
     * Constructor
     *
     * @param toBeDecorated    - a basic collision strategy (remove), to decorate with mor behaviors.
     * @param windowController - the game window controller.
     * @param gameManager-     the brick game manager.
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                                WindowController windowController, BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange() {
        gameManager.setCamera(null);
    }

    /**
     * Change camera position on collision and delegate to held CollisionStrategy.
     *
     * @param thisObj  - brick
     * @param otherObj - ball (Ball or Puck)
     * @param counter  global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Ball ball = getBall();
        if (gameManager.getCamera() == null && ball != null) {
            //create countdown agent that will tell the system when to return the camera to default
            getGameObjectCollection().addGameObject(
                    new BallCollisionCountdownAgent(ball, this,
                            ball.getCollisionCount() + NUM_BALL_COLLISIONS_TO_TURN_OFF),
                    Layer.BACKGROUND);
            //set the main camera to follow the main ball
            gameManager.setCamera(
                    new Camera(ball, Vector2.ZERO, windowController.getWindowDimensions().mult(1.2f),
                            windowController.getWindowDimensions()));
        }
    }

    //privet method to find the main ball
    private Ball getBall() {
        for (GameObject object : getGameObjectCollection()) {
            if (!(object instanceof Puck) && (object instanceof Ball)) {
                return (Ball) object;
            }
        }
        return null;
    }
}
