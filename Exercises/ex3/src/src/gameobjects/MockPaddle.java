package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class for extra paddle on the screen that removed after some ball collisions.
 *
 * @author Yair Stern
 */
public class MockPaddle extends Paddle {

    /**
     * isInstantiated - a static variable that when there is a mock paddle on the screen
     * is true and false otherwise so that will never be 2 mock paddles at the same time.
     */
    public static boolean isInstantiated;

    /////// privet ///////
    private final GameObjectCollection gameObjects;
    private int numCollisionsToDisappear;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner            - Position of the object, in window coordinates (pixels).
     *                                 Note that (0,0) is the top-left corner of the window.
     * @param dimensions               - Width and height in window coordinates.
     * @param renderable               - The renderable representing the object. Can be null, in which case
     * @param inputListener            - listener object for user input.
     * @param windowDimensions         - dimensions of game window.
     * @param minDistanceFromEdge      - border for paddle movement.
     * @param numCollisionsToDisappear - after this number disappear from the mock paddle
     */
    public MockPaddle(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      UserInputListener inputListener,
                      Vector2 windowDimensions,
                      GameObjectCollection gameObjects,
                      int minDistanceFromEdge,
                      int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjects = gameObjects;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        isInstantiated = true;

    }

    /**
     * after numCollisionsToDisappear collisions remove the mock paddle
     *
     * @param other     - game object
     * @param collision - Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball || other instanceof StatusDefiner) {
            numCollisionsToDisappear--;
            if (numCollisionsToDisappear == 0) {
                gameObjects.removeGameObject(this);
                isInstantiated = false;
            }
        }
    }
}
