// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Responsible for the creation and management of the terrain in the game.
 *
 * @author Hilla Heimberg, Yair Shtern
 */
public class Terrain {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final int TERRAIN_DEPTH = 20;
    private static final int NOISE_VALUE = 5;
    private static final int UPPER_LAYER_VALUE = 1;
    private static final float VALUE_FOR_GROUND_HIGH = 2 / 3f;

    private static final Color BASE_GROUND_COLOR = new Color(212, 224, 229);
    private final int groundUpperLayer;
    private final float groundHeightAtX0;
    private final PerlinNoise perlinNoise;
    private final GameObjectCollection gameObjects;
    // mapping xCoordinate to set of objects which related to this location
    private static final HashMap<Integer, HashSet<RemovableObject>> locationToObjsMap = new HashMap<>();

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String GROUND = "ground";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new terrain instance.
     *
     * @param gameObjects      The collection of all participating game objects.
     * @param groundLayer      The number of the layer to which the created ground objects should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param seed             A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundUpperLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * VALUE_FOR_GROUND_HIGH;
        this.perlinNoise = new PerlinNoise();
        this.perlinNoise.setSeed(seed);
    }

    /**
     * This method return the ground height at a given location.
     *
     * @param x A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        double perlinNoiseResult = (NOISE_VALUE * this.perlinNoise.noise(x / Block.SIZE) * Block.SIZE) +
                this.groundHeightAtX0;
        return (float) ((int) perlinNoiseResult / Block.SIZE) * Block.SIZE;
    }

    /**
     * This method creates terrain in a given range of x-values.
     *
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int xCoordinate = (minX / Block.SIZE) * Block.SIZE;
        while (xCoordinate < maxX) {
            int yCoordinate = (int) groundHeightAt(xCoordinate);
            // for each set which represents specific XLocation we added the objects which exists there
            HashSet<RemovableObject> blocksForLocationSet = new HashSet<>();
            for (int high = 0; high < TERRAIN_DEPTH; high++) {
                createTerrainBlocks(xCoordinate, yCoordinate, high, blocksForLocationSet);
            }
            locationToObjsMap.put(xCoordinate, blocksForLocationSet);
            xCoordinate += Block.SIZE;
        }
    }

    /*
    Create the terrain blocks and updated for each one its compatible layer.
     */
    private void createTerrainBlocks(int xCoordinate, int yCoordinate, int high,
                                     HashSet<RemovableObject> blocksForLocationSet) {
        RectangleRenderable groundRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

        // we take care that the leaves will collide with just the two upper layers of the terrain
        int blockLayer = this.groundUpperLayer;
        if (high > UPPER_LAYER_VALUE) {
            blockLayer += 1;
        }

        RemovableBlock block = new RemovableBlock(new Vector2(xCoordinate, yCoordinate + high *
                Block.SIZE), groundRenderable, blockLayer);

        block.setTag(GROUND);
        this.gameObjects.addGameObject(block, blockLayer);
        blocksForLocationSet.add(block); // add the block to the HashSet
    }

    /**
     * Removes the terrain blocks which exists in the dead zone from the game and from the map.
     * @param minX The lower bound of the given range.
     * @param maxX The upper bound of the given range.
     */
    public void removeInRange(int minX, int maxX) {
        for (int location = (minX / Block.SIZE) * Block.SIZE; location < maxX; location += Block.SIZE) {
            if (locationToObjsMap.containsKey(location)) {
                for (RemovableObject object : locationToObjsMap.get(location)) {
                    this.gameObjects.removeGameObject((GameObject) object, object.getLayer());
                }
                locationToObjsMap.remove(location);
            }
        }
    }
}