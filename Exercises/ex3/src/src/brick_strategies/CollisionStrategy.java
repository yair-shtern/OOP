package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Collision strategy interface for the brick object
 *
 * @author Yair Stern
 */
public interface CollisionStrategy {

    /**
     * To be called on brick collision.
     *
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  global brick counter.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    /**
     * All collision strategy objects should hold a reference to the global
     * game object collection and be able to return it.
     *
     * @return global game object collection whose reference is held in object.
     */
    public GameObjectCollection getGameObjectCollection();
}

