package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ReduceAndExpandStrategy;

/**
 * A status definer, fall from a brick straight down if it is hits a paddle something happens and
 * the status removed.
 *
 * @author Yair Stern
 */
public class StatusDefiner extends GameObject {
    private static final float STATUS_DEFINER_Y_VAL = 160;
    private final ReduceAndExpandStrategy owner;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner - Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    - Width and height in window coordinates.
     * @param renderable    - The renderable representing the object. Can be null, in which case
     * @param gameObjects   - game objects collection.
     */
    public StatusDefiner(ReduceAndExpandStrategy owner, Vector2 topLeftCorner,
                         Vector2 dimensions,
                         Renderable renderable,
                         GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.owner = owner;
        this.gameObjects = gameObjects;
        this.setVelocity(Vector2.DOWN.mult(STATUS_DEFINER_Y_VAL));
    }

    /**
     * Determines which objects to collide with and which to pass through.
     *
     * @param other - game object
     * @return - true if it should collide whit and false otherwise
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        //status collide only whit paddle
        return other instanceof Paddle;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        owner.doStatusOperation(other);
    }
}
