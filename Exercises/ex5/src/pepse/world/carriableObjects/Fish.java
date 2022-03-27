// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.carriableObjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.util.Random;

/**
 * Responsible for the creation and management of the fish.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Fish extends CarriableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final float INITIAL_VALUE = -5f;
    private static final float FINAL_VALUE = 5f;
    private static final float TRANSITION_TIME = 0.5f;
    private static final int AMOUNT_OF_FISH = 4;

    private final int layer;
    private static boolean initialized = false;
    private final GameObjectCollection gameObjects;
    private static ImageRenderable[] fishRenderable;
    private static Sound sound;
    private final Counter scoreCounter;

    // ------------------------------- ADDRESSES FOR IMAGES ---------------------------------
    private static final String FISH_1_PNG = "assets/fish1.png";
    private static final String FISH_2_PNG = "assets/fish2.png";
    private static final String FISH_3_PNG = "assets/fish3.png";
    private static final String FISH_4_PNG = "assets/fish4.png";
    private static final String BITE_SOUND_WAV = "assets/biteSound.wav";

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String FISH = "fish";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Construct a new GameObject instance - a fish.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created fish should be added.
     */
    public Fish(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects,
                SoundReader soundReader, ImageReader imageReader, Counter scoreCounter, int layer,
                Random random) {
        super(topLeftCorner, dimensions, null, gameObjects, layer);
        this.gameObjects = gameObjects;
        this.scoreCounter = scoreCounter;
        this.layer = layer;

        if (!initialized) {
            fishRenderable = new ImageRenderable[]{
                    imageReader.readImage(FISH_1_PNG, true),
                    imageReader.readImage(FISH_2_PNG, true),
                    imageReader.readImage(FISH_3_PNG, true),
                    imageReader.readImage(FISH_4_PNG, true)};
            sound = soundReader.readSound(BITE_SOUND_WAV);
            initialized = true;
        }
        new Transition<Float>(this, this.renderer()::setRenderableAngle, INITIAL_VALUE,
                FINAL_VALUE, Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        setTag(FISH);
        renderer().setRenderable(fishRenderable[random.nextInt(AMOUNT_OF_FISH)]);
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