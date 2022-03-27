// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.carriableObjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.RemovableObject;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation of all the CarriableObjects in the game.
 *
 * @author Hilla Heimberg, Yair Shtern
 */
public class HandleCarriableObjects {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final int CARRIABLE_OBJECT_SIZE = 30;
    private static final int GAP_FROM_GROUND = 2;
    private static final int BOUND_RANDOM_TYPE_OBJECT = 4;
    private static final int BOUND_RANDOM_FOR_CREATE = 9;

    private final int objectLayer;
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Function<Integer, Float> groundHeightAt;
    private final int avatarLayer;
    private final Counter scoreCounter;
    private final Random random = new Random();
    // mapping xCoordinate to carriable objects which related to this location
    private static final HashMap<Integer, RemovableObject> locationToObjMap = new HashMap<>();

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new GameObject instance - CreateCarriableObjects.
     *
     * @param gameObjects    The collection of all participating game objects.
     * @param imageReader    Contains a single method: readImage, which reads an image from disk.
     *                       See its documentation for help.
     * @param soundReader    Contains a single method: readSound, which reads a wav file from
     *                       disk. See its documentation for help.
     * @param objectLayer    The number of the layer to which the current object should be added.
     * @param seed           seed for the carriable objects.
     * @param groundHeightAt groundHeightAt function
     * @param avatarLayer    avatar's layer.
     * @param scoreCounter   score counter.
     */
    public HandleCarriableObjects(GameObjectCollection gameObjects, ImageReader imageReader,
                                  SoundReader soundReader, int objectLayer, int seed,
                                  Function<Integer, Float> groundHeightAt,
                                  int avatarLayer, Counter scoreCounter) {
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.objectLayer = objectLayer;
        this.groundHeightAt = groundHeightAt;
        this.avatarLayer = avatarLayer;
        this.scoreCounter = scoreCounter;
        this.seed = seed;
    }

    /**
     * This method creates carriable objects in a given range of x-values.
     *
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int xPosition = (minX / Block.SIZE) * Block.SIZE;
        for (; xPosition < maxX; xPosition += Block.SIZE) {
            this.random.setSeed(xPosition + this.seed);

            // decide (in randomly way) if to put object in the game or not - probability of 1/9 to put
            if (this.random.nextInt(BOUND_RANDOM_FOR_CREATE) <= 1) {
                Vector2 placeForObject = new Vector2(xPosition, this.groundHeightAt.apply(xPosition) -
                        GAP_FROM_GROUND * Block.SIZE);

                chooseObject(placeForObject);

                // the avatar should collide with the carriable objects
                this.gameObjects.layers().shouldLayersCollide(this.avatarLayer, this.objectLayer,
                        true);
            }
        }
    }

    /*
    This method responsible for randomize which carriable object will be added to the game and to add them
    as well.
     */
    private void chooseObject(Vector2 placeForObject) {
        CarriableObject object;
        if (this.random.nextInt(BOUND_RANDOM_TYPE_OBJECT) == 0) { // probability of 1/4 to put negative object
            object = new ToxicFish(placeForObject, Vector2.ONES.mult(CARRIABLE_OBJECT_SIZE), this.gameObjects,
                    this.soundReader, this.imageReader, this.scoreCounter, this.objectLayer);
        }
        else { // probability of 3/4 to put positive object
            if (this.random.nextBoolean()) { // probability of 1/2 to put coin
                object = new Coin(placeForObject, Vector2.ONES.mult(CARRIABLE_OBJECT_SIZE), this.gameObjects,
                        this.soundReader, this.imageReader, this.scoreCounter, this.objectLayer);
            }
            else {  // probability of 1/2 to put fish
                object = new Fish(placeForObject, Vector2.ONES.mult(CARRIABLE_OBJECT_SIZE),
                        this.gameObjects, this.soundReader, this.imageReader, this.scoreCounter,
                        this.objectLayer, this.random);
            }
        }
        locationToObjMap.put((int) placeForObject.x(), object); // add the carriable object to the HashMap
    }

    /**
     * Removes the carriable objects which exists in the dead zone from the game and from the map.
     * @param minX The lower bound of the given range.
     * @param maxX The upper bound of the given range.
     */
    public void removeInRange(int minX, int maxX) {
        for (int location = (minX / Block.SIZE) * Block.SIZE; location < maxX; location++) {
            if (locationToObjMap.containsKey(location)) {
                this.gameObjects.removeGameObject((GameObject) locationToObjMap.get(location),
                        locationToObjMap.get(location).getLayer());
                locationToObjMap.remove(location);
            }
        }
    }
}