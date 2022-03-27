// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.carriableObjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.RemovableObject;

/**
 * Responsible for the creation and the removal of the CarriableObjects.
 * @author Hilla Heimberg, Yair Shtern
 */
public class CarriableObject extends GameObject implements RemovableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    private final GameObjectCollection gameObjects;
    private final int layer;

    // -------------------------------------- METHODS --------------------------------------
    /**
     * Construct a new GameObject instance - CarriableObjects.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public CarriableObject(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           GameObjectCollection gameObjects, int layer) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.layer = layer;
        gameObjects.addGameObject(this, layer);
    }

    /**
     * Called on the first frame of a collision.
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.gameObjects.removeGameObject(this, this.layer);
    }

    /**
     * Getter.
     * @return the layer of the object.
     */
    @Override
    public int getLayer() {
        return this.layer;
    }
}
