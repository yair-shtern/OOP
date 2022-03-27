package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.StatusDefiner;

import java.util.Random;

/**
 * Collision strategy that creates a game status that reduce or expend the paddle
 *
 * @author Yair Stern
 */
public class ReduceAndExpandStrategy extends RemoveBrickStrategyDecorator implements CollisionStrategy {
    private static final float REDUCE_BY = 0.8f;
    private static final float EXPAND_BY = 1.2f;
    private static final String NARROW_IMAGE_PATH = "assets/buffNarrow.png";
    private static final String EXPEND_IMAGE_PATH = "assets/buffWiden.png";
    private final ImageReader imageReader;
    private final Vector2 windowDimensions;
    private boolean contraction;

    /**
     * Constructor
     *
     * @param toBeDecorated    - a basic collision strategy (remove), to decorate with mor behaviors.
     * @param imageReader      - image reader to display on screen.
     * @param windowDimensions - the game window dimensions.
     */
    public ReduceAndExpandStrategy(
            CollisionStrategy toBeDecorated, ImageReader imageReader, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        contraction = false;

    }

    /**
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable image;
        Random random = new Random();
        //choose randomly to create reduce or expend status
        if (random.nextBoolean()) {
            image = imageReader.readImage(NARROW_IMAGE_PATH, true);
            this.contraction = true;

        } else {
            image = imageReader.readImage(EXPEND_IMAGE_PATH, true);
            this.contraction = false;
        }
        //create status
        StatusDefiner buff = new StatusDefiner(
                this, Vector2.ZERO, thisObj.getDimensions(), image, getGameObjectCollection());
        buff.setCenter(thisObj.getCenter());
        getGameObjectCollection().addGameObject(buff);
    }

    /**
     * reduce or expend the paddle
     *
     * @param other - paddle
     */
    public void doStatusOperation(GameObject other) {
        if (contraction) {
            other.setDimensions(new Vector2(
                    other.getDimensions().x() * REDUCE_BY, other.getDimensions().y()));
        }
        if (other.getDimensions().x() < windowDimensions.x() / 2) {//max size
            other.setDimensions(new Vector2(
                    other.getDimensions().x() * EXPAND_BY, other.getDimensions().y()));
        }
    }
}
