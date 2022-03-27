package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * One of the main game objects. Repels the ball against the bricks.
 *
 * @author Yair Stern
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistanceFromEdge;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object. Can be null, in which case
     * @param inputListener       The user input.
     * @param minDistanceFromEdge Border for paddle movement
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * Override update and move the paddle according to keyboard press
     *
     * @param deltaTime - The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && getTopLeftCorner().x()
                > minDistanceFromEdge) {//right arrow pressed and it is in the borders
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                windowDimensions.x() - minDistanceFromEdge - getDimensions().x()
                        > getTopLeftCorner().x()) {//left arrow pressed and it is in the borders
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        //set the paddle to the given direction
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
