package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;
import src.gameobjects.Paddle;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces extra paddle to game window which remains until colliding
 * NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 *
 * @author Yair Stern
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator implements CollisionStrategy {
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 20;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final int NUM_COLLISION_TO_DISAPPEAR = 3;
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;

    /**
     * Constructor
     *
     * @param toBeDecorated    - a basic collision strategy (remove), to decorate with mor behaviors.
     * @param imageReader      - image reader to display on screen.
     * @param inputListener    - input from user (can be a mouse or keyboard act).
     * @param windowDimensions - the game window dimensions.
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                             UserInputListener inputListener,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
    }

    /**
     * func that called when ball collide white a brick.
     *
     * @param thisObj  - a brick.
     * @param otherObj - some ball.
     * @param counter  - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        //call to basic behavior
        super.onCollision(thisObj, otherObj, counter);
        if (!MockPaddle.isInstantiated) {
            //if there is no mock paddle add mock paddle
            Renderable peddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
            Paddle paddle = new MockPaddle(
                    Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                    peddleImage, inputListener, windowDimensions,
                    getGameObjectCollection(), MIN_DISTANCE_FROM_SCREEN_EDGE, NUM_COLLISION_TO_DISAPPEAR);
            paddle.setCenter(windowDimensions.mult(0.5f));
            getGameObjectCollection().addGameObject(paddle);
        }
    }
}
