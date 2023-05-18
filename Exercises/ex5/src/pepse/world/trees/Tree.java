package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.RemovableBlock;
import pepse.world.RemovableObject;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 *
 * @author Hilla Heimberg, Yair Shtern
 */
public class Tree {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final int BOUND_VALUE_FOR_TREES = 15;
    private static final int RANGE_VALUE_FOR_PLACING_LEAF = 10;
    private static final int BOUND_VALUE_FOR_PLACING_LEAF = 8;
    private static final int BOUND_VALUE_FOR_TREE_HIGH = 6;
    private static final int MIN_TREE_HIGH = 5;
    private static final float FACTOR_FOR_TOP_SIZE = 4 / 5f;

    private final int treesLayer;
    private final int leavesLayer;
    private final int layerLeavesShouldCollide;
    private final int leavesLayerWhenFalling;
    private final int layerTreeShouldCollide;
    private final int seed;
    private static final Color BASE_TREE_COLOR = new Color(100, 50, 20);
    private final GameObjectCollection gameObjects;
    private final Function<Integer, Float> groundHeightAt;
    private final Random random = new Random();
    // mapping xCoordinate to set of objects which related to this location
    private static final HashMap<Integer, HashSet<RemovableObject>> locationToObjsMap = new HashMap<>();

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String STEM = "stem";
    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new tree instance.
     *
     * @param gameObjects              The collection of all participating game objects.
     * @param groundHeightAt           groundHeightAt function.
     * @param treesLayer               layer of the trees
     * @param leavesLayer              layer of the leaves
     * @param leavesLayerWhenFalling   layer of the falling leaves
     * @param layerLeavesShouldCollide the layer of the object which should collide with the leaves - terrain
     *                                 layer.
     * @param layerTreeShouldCollide   the layer of the object which should collide with the tree - avatar
     *                                 layer
     * @param seed                     seed
     */
    public Tree(GameObjectCollection gameObjects, Function<Integer, Float> groundHeightAt,
                int treesLayer, int leavesLayer, int leavesLayerWhenFalling, int layerLeavesShouldCollide,
                int layerTreeShouldCollide, int seed) {
        this.groundHeightAt = groundHeightAt;
        this.gameObjects = gameObjects;
        this.treesLayer = treesLayer;
        this.leavesLayer = leavesLayer;
        this.leavesLayerWhenFalling = leavesLayerWhenFalling;
        this.layerLeavesShouldCollide = layerLeavesShouldCollide; // terrain layer
        this.layerTreeShouldCollide = layerTreeShouldCollide; // avatar layer
        this.seed = seed;
    }


    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int treeXCoordinate = (minX / Block.SIZE) * Block.SIZE;
        for (; treeXCoordinate < maxX; treeXCoordinate += Block.SIZE) {
            this.random.setSeed(Objects.hash(treeXCoordinate, this.seed));

            if (this.random.nextInt(BOUND_VALUE_FOR_TREES) == 0) {
                Vector2 placeForTree = new Vector2(treeXCoordinate,
                        this.groundHeightAt.apply(treeXCoordinate) - Block.SIZE);

                // for each set which represents specific XLocation we added the objects which exists there
                HashSet<RemovableObject> objectsForLocationSet = new HashSet<>();
                int treeHigh = random.nextInt(BOUND_VALUE_FOR_TREE_HIGH) + MIN_TREE_HIGH;
                createStem(placeForTree, treeHigh, this.gameObjects, objectsForLocationSet); // the stem
                createLeaves(placeForTree, treeHigh, objectsForLocationSet); // the leaves

                locationToObjsMap.put(treeXCoordinate, objectsForLocationSet);

                // the avatar should collide with the trees
                this.gameObjects.layers().shouldLayersCollide(this.treesLayer, this.layerTreeShouldCollide,
                        true);
            }
        }
    }

    /*
    Create stem for a current tree.
    */
    private void createStem(Vector2 topLeftCorner, int treeHigh, GameObjectCollection gameObjects,
                            HashSet<RemovableObject> objectsForLocationSet) {
        for (int high = 0; high < treeHigh; high++) {
            RectangleRenderable treeRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR));
            Vector2 placeForStem = new Vector2(topLeftCorner.x(), topLeftCorner.y() - high * Block.SIZE);
            RemovableBlock stemBlock = new RemovableBlock(placeForStem, treeRenderable, this.treesLayer);
            stemBlock.setTag(STEM);
            gameObjects.addGameObject(stemBlock, this.treesLayer);
            objectsForLocationSet.add(stemBlock); // add the block stem to the HashSet
        }
    }

    /*
    Create leaves for a current tree.
     */
    private void createLeaves(Vector2 treeCoordinates, int treeHigh, HashSet<RemovableObject> objectsForLocationSet) {
        int topOfTheTreeSize = (int) (treeHigh * FACTOR_FOR_TOP_SIZE);

        Vector2 leavesTopLeftCorner = new Vector2((float) (treeCoordinates.x() - (topOfTheTreeSize / 2) * Block.SIZE),
                (float) (treeCoordinates.y() - (topOfTheTreeSize / 2 + treeHigh) * Block.SIZE));

        for (int row = 0; row < topOfTheTreeSize; row++) {
            for (int column = 1; column <= topOfTheTreeSize; column++) {
                if (this.random.nextInt(RANGE_VALUE_FOR_PLACING_LEAF) <= BOUND_VALUE_FOR_PLACING_LEAF) {
                    Vector2 placeForLeaf = new Vector2(leavesTopLeftCorner.x() + row * Block.SIZE,
                            leavesTopLeftCorner.y() + column * Block.SIZE);

                    // create leaf
                    Leaf leaf = new Leaf(placeForLeaf, this.gameObjects, this.leavesLayer,
                            this.leavesLayerWhenFalling, this.layerLeavesShouldCollide, this.random);
                    this.gameObjects.addGameObject(leaf, this.leavesLayer);
                    objectsForLocationSet.add(leaf); // add the leaf to the hashSet
                }
            }
        }
    }

    /**
     * Removes the tree (stem + leaves) which exists in the dead zone from the game and from the map.
     *
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