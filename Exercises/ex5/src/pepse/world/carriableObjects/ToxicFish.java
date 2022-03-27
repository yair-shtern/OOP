// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.carriableObjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Responsible for the creation and management of the toxic fish.
 * @author Hilla Heimberg, Yair Shtern
 */
public class ToxicFish extends CarriableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    private static boolean initialized = false;
    private final int layer;
    private final GameObjectCollection gameObjects;
    private final Counter scoreCounter;
    private static ImageRenderable toxicRenderable;
    private static Sound sound;

    // ------------------------------- ADDRESSES FOR IMAGES ---------------------------------
    private static final String TOXIC_FISH_PNG = "assets/toxicFish.png";
    private static final String BITE_SOUND_WAV = "assets/biteSound.wav";

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String TOXIC_FISH = "toxicFish";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new GameObject instance - ToxicFish.
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions  Width and height in window coordinates.
     * @param gameObjects The collection of all participating game objects.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param scoreCounter score counter for the game.
     * @param layer The number of the layer to which the created toxic fish should be added.
     */
    public ToxicFish(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects,
                     SoundReader soundReader, ImageReader imageReader, Counter scoreCounter, int layer) {
        super(topLeftCorner, dimensions, null, gameObjects, layer);
        this.gameObjects = gameObjects;
        this.scoreCounter = scoreCounter;
        this.layer = layer;
        setTag(TOXIC_FISH);

        if (!initialized) {
            toxicRenderable = imageReader.readImage(TOXIC_FISH_PNG, true);
            sound = soundReader.readSound(BITE_SOUND_WAV);
            initialized = true;
        }
        renderer().setRenderable(toxicRenderable);
    }

    /**
     * Called on the first frame of a collision.
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        sound.play();
        this.gameObjects.removeGameObject(this, this.layer);
        this.scoreCounter.decrement();
    }
}