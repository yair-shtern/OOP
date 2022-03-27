package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Brick object for the game
 *
 * @author Yair Stern
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;
    private final Counter bricksCounter;
    private boolean thereWasCollision;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     * @param collisionStrategy The strategy on collision.
     * @param bricksCounter     - counter object that count the bricks
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, CollisionStrategy collisionStrategy, Counter bricksCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.bricksCounter = bricksCounter;
        thereWasCollision = false;
    }

    /**
     * Override onCollisionEnter when the brick collide withe other object,
     * the function implements the onCollision of the brick strategy
     *
     * @param other     - The GameObject with which a collision occurred.
     * @param collision – Information regarding this collision. A reasonable elastic behavior
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!thereWasCollision) {//for multiple collisions on the same time with the same brick
            collisionStrategy.onCollision(this, other, bricksCounter);
            thereWasCollision = true;
        }
    }
}
