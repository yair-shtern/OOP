package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;


/**
 * Brick strategy that removes holding brick on collision.
 *
 * @author Yair Stern
 */
public class RemoveBrickStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructor
     *
     * @param gameObjectCollection - the game objects collection
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }


    /**
     * Removes brick from game object collection on collision.
     *
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        gameObjectCollection.removeGameObject(thisObj);
        counter.decrement();
    }


    /**
     * All collision strategy objects should hold a reference
     * to the global game object collection and be able to return it.
     *
     * @return game object collection
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }

}
