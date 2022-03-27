// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Additional class for the tree and the terrain in the game.
 * Responsible for returning the layer of the tree "object" and the terrain "object".
 * @author Hilla Heimberg, Yair Shtern
 */
public class RemovableBlock extends Block implements RemovableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    private final int layer;

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Construct a new GameObject instance - RemovableBlock.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public RemovableBlock(Vector2 topLeftCorner, Renderable renderable, int layer) {
        super(topLeftCorner, renderable);
        this.layer = layer;
    }

    /**
     * Getter.
     * @return the layer of the object.
     */
    @Override
    public int getLayer() {
        return layer;
    }
}