// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a numeric object on the game window showing a numeric count of score.
 * @author Hilla Heimberg, Yair Shtern
 */
public class ScoreCounter extends GameObject {
    // -------------------------------------- PRIVATE -------------------------------------
    private final TextRenderable textImage;
    private final Counter scoreCounter;

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String COUNTER_STRING = "Current score: %d";

    // -------------------------------------- METHODS --------------------------------------
    /**
     * Constructor. Construct a new GameObject instance - ScoreCounter.
     * @param scoreCounter score counter
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created score counter should be added.
     */
    public ScoreCounter(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection
            gameObjects,Counter scoreCounter, int layer) {
        super(topLeftCorner, dimensions, null);
        this.textImage = new TextRenderable(String.format(COUNTER_STRING, scoreCounter.value()));
        this.scoreCounter = scoreCounter;
        this.renderer().setRenderable(this.textImage);
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(this, layer);
    }

    /**
     * Should be called once per frame.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // set the score counter string to the current value
        this.textImage.setString(String.format(COUNTER_STRING, this.scoreCounter.value()));
        this.renderer().setRenderable(this.textImage);
    }
}