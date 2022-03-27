package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Abstract decorator to add functionality to the remove brick strategy,
 * following the decorator pattern. All strategy decorators should inherit from this class.
 *
 * @author Yair Stern
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {

    private final CollisionStrategy toBeDecorated;

    /**
     * Constructor
     *
     * @param toBeDecorated - a basic collision strategy (remove), to decorate with mor behaviors.
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated) {
        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Should delegate to held Collision strategy object.
     *
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  - global brick counter.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        toBeDecorated.onCollision(thisObj, otherObj, counter);
    }

    /**
     * return held reference to global game object. Delegate to held object to be decorated
     *
     * @return the game object collection
     */
    public GameObjectCollection getGameObjectCollection() {

        return toBeDecorated.getGameObjectCollection();
    }
}
