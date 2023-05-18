package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Strategy class for double behavior.
 *
 * @author Yair Stern
 */
public class DoubleBehavior implements CollisionStrategy {

    private final CollisionStrategy[] behaviors;
    private final int behaviorsNum;

    /**
     * Constructor
     *
     * @param behaviors    - an array of 2 or 3 behaviors to implement on collision
     * @param behaviorsNum - 2 or 3
     */
    public DoubleBehavior(CollisionStrategy[] behaviors, int behaviorsNum) {
        this.behaviors = behaviors;
        this.behaviorsNum = behaviorsNum;
    }

    /**
     * implements all the behaviors in the array
     *
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        for (CollisionStrategy behavior : behaviors) {
            behavior.onCollision(thisObj, otherObj, counter);
        }
        //increase counter, because decrement append multiple times
        counter.increaseBy(behaviorsNum - 1);
    }

    /**
     * @return - game abject collection
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return behaviors[0].getGameObjectCollection();
    }
}
