// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;
import java.util.function.Supplier;

/**
 * Display a graphic object on the game window showing an energy progress bar for the avatar.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Energy extends GameObject {
    // -------------------------------------- PRIVATE -------------------------------------
    private final ImageRenderable[] imageRenderable;
    private final Supplier<Float> getEnergyValue;

    // ----------------------------- ADDRESSES FOR IMAGES ---------------------------------
    private static final String ENERGY_100_PNG = "assets/energy100.png";
    private static final String ENERGY_90_PNG = "assets/energy90.png";
    private static final String ENERGY_80_PNG = "assets/energy80.png";
    private static final String ENERGY_70_PNG = "assets/energy70.png";
    private static final String ENERGY_60_PNG = "assets/energy60.png";
    private static final String ENERGY_50_PNG = "assets/energy50.png";
    private static final String ENERGY_40_PNG = "assets/energy40.png";
    private static final String ENERGY_30_PNG = "assets/energy30.png";
    private static final String ENERGY_20_PNG = "assets/energy20.png";
    private static final String ENERGY_10_PNG = "assets/energy10.png";
    private static final String ENERGY_0_PNG = "assets/energy0.png";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new GameObject instance - Energy progress bar.
     *
     * @param topLeftCorner  position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param gameObjects    The collection of all participating game objects.
     * @param imageReader    Contains a single method: readImage, which reads an image from disk.
     *                       See its documentation for help.
     * @param layer          The number of the layer to which the created energy progress bar should be added.
     * @param getEnergyValue getEnergyValue function.
     */
    public Energy(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects, ImageReader
            imageReader, int layer, Supplier<Float> getEnergyValue) {
        super(topLeftCorner, dimensions, null);
        this.imageRenderable = new ImageRenderable[]{
                imageReader.readImage(ENERGY_0_PNG, true),
                imageReader.readImage(ENERGY_10_PNG, true),
                imageReader.readImage(ENERGY_20_PNG, true),
                imageReader.readImage(ENERGY_30_PNG, true),
                imageReader.readImage(ENERGY_40_PNG, true),
                imageReader.readImage(ENERGY_50_PNG, true),
                imageReader.readImage(ENERGY_60_PNG, true),
                imageReader.readImage(ENERGY_70_PNG, true),
                imageReader.readImage(ENERGY_80_PNG, true),
                imageReader.readImage(ENERGY_90_PNG, true),
                imageReader.readImage(ENERGY_100_PNG, true)};

        this.getEnergyValue = getEnergyValue;
        this.renderer().setRenderable(imageRenderable[0]);
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(this, layer);
    }

    /**
     * Should be called once per frame.
     *
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
        Float currentEnergy = this.getEnergyValue.get();
        renderer().setRenderable(this.imageRenderable[(int)(currentEnergy/(this.imageRenderable.length-1))]);
    }
}