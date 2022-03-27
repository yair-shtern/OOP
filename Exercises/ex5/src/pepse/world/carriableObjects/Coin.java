// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.carriableObjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Responsible for the creation and management of the coin.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Coin extends CarriableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final double TIME_BETWEEN_CLIPS = 0.5;
    private static boolean initialized = false;
    private final int layer;

    private final GameObjectCollection gameObjects;
    private static AnimationRenderable coinRenderable;
    private static Sound sound;
    private final Counter scoreCounter;

    // ------------------------------- ADDRESSES FOR IMAGES ---------------------------------
    private static final String COIN_1_PNG = "assets/coin1.png";
    private static final String COIN_2_PNG = "assets/coin2.png";
    private static final String COIN_3_PNG = "assets/coin3.png";
    private static final String COIN_4_PNG = "assets/coin4.png";
    private static final String COIN_5_PNG = "assets/coin5.png";
    private static final String COIN_6_PNG = "assets/coin6.png";

    // ------------------------------- ADDRESSES FOR SOUND ---------------------------------
    private static final String COIN_SOUND_WAV = "assets/coinSound.wav";

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String COIN = "coin";

    // -------------------------------------- METHODS --------------------------------------
    /**
     * Construct a new GameObject instance - Coin.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param gameObjects The collection of all participating game objects.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param scoreCounter score counter for the game.
     * @param layer The number of the layer to which the created coin should be added.
     */
    public Coin(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects,
                SoundReader soundReader,ImageReader imageReader, Counter scoreCounter, int layer) {
        super(topLeftCorner, dimensions, null,gameObjects,layer);
        this.gameObjects = gameObjects;
        this.scoreCounter = scoreCounter;
        this.layer = layer;
        setTag(COIN);

        if (!initialized){
            coinRenderable = new AnimationRenderable(new ImageRenderable[]
                    {imageReader.readImage(COIN_1_PNG, true),
                            imageReader.readImage(COIN_2_PNG, true),
                            imageReader.readImage(COIN_3_PNG, true),
                            imageReader.readImage(COIN_4_PNG, true),
                            imageReader.readImage(COIN_5_PNG, true),
                            imageReader.readImage(COIN_6_PNG, true)
                    }, TIME_BETWEEN_CLIPS);
            sound = soundReader.readSound(COIN_SOUND_WAV);
            initialized = true;
        }
        renderer().setRenderable(coinRenderable);
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
        this.gameObjects.removeGameObject(this, layer);
        this.scoreCounter.increment();
    }
}