// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.RemovableObject;

import java.awt.*;
import java.util.Random;

/**
 * Responsible for the creation and the management of leaves.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Leaf extends GameObject implements RemovableObject {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final float FADEOUT_TIME = 10;
    private static final int FALLING_SPEED = 40;
    private static final int BOUND_DURATION_LIFE = 100;
    private static final int BOUND_DEATH_TIME = 20;
    private static final float TRANSITION_TIME = 0.5f;
    private static final int ACCELERATION_VALUE = 50;
    private static final float INIT_VALUE_SHAKING = -2f;
    private static final float FINAL_VALUE_SHAKING = 2f;
    private static final float FACTOR_FOR_DIMENSIONS = 0.9f;
    private static final float INIT_VALUE_FALLING = -20f;
    private static final float FINAL_VALUE_FALLING = 20f;

    private Transition<Float> horizontalTransition;
    private static final Color BASE_LEAF_COLOR = new Color(189, 243, 208);
    private static final Vector2 originalDimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final RectangleRenderable leafRenderable =
            new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
    private final Random random;

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String LEAF = "leaf";
    private int currentLeafLayer;
    private final Vector2 placeForLeaf;
    private final GameObjectCollection gameObjects;
    private final int leafLayerBeforeFalling;
    private final int leafLayerWhenFalling;
    private final int layerLeavesShouldCollide;
    private Transition<Vector2> dimensionsTransition;
    private Transition<Float> angleTransition;

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new GameObject instance - the leaf.
     * @param placeForLeaf place for locate the leaf
     * @param gameObjects The collection of all participating game objects.
     * @param leafLayerBeforeFalling Layer of the leaf which before falling
     * @param leafLayerWhenFalling Layer of the leaf which during falling
     * @param layerLeavesShouldCollide Layer of the object should collide with the leaf
     * @param random random
     */
    public Leaf(Vector2 placeForLeaf, GameObjectCollection gameObjects, int leafLayerBeforeFalling,
                int leafLayerWhenFalling, int layerLeavesShouldCollide, Random random) {
        super(placeForLeaf, originalDimensions, leafRenderable);
        this.placeForLeaf = placeForLeaf;
        this.gameObjects = gameObjects;
        this.leafLayerBeforeFalling = leafLayerBeforeFalling;
        this.leafLayerWhenFalling = leafLayerWhenFalling;
        this.layerLeavesShouldCollide = layerLeavesShouldCollide;
        this.currentLeafLayer = leafLayerBeforeFalling;
        this.random = random;
        setTag(LEAF);
        createLeavesBehavior();
    }

    /*
     Create the behavior of the leaves.
     */
    private void createLeavesBehavior() {
        new ScheduledTask(this, (float) this.random.nextDouble(),
                false, this::leavesShaking);

        int durationLife = this.random.nextInt(BOUND_DURATION_LIFE);
        new ScheduledTask(this, durationLife, false, this::leavesFalling);
    }

    /*
    Responsible for the shaking of the leaves.
     */
    private void leavesShaking() {
        // angle transition
        this.angleTransition = new Transition<Float>(this,
                renderer()::setRenderableAngle, INIT_VALUE_SHAKING, FINAL_VALUE_SHAKING,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        // dimensions transition
        this.dimensionsTransition = new Transition<Vector2>(this, this::setDimensions,
                originalDimensions, originalDimensions.mult(FACTOR_FOR_DIMENSIONS),
                Transition.CUBIC_INTERPOLATOR_VECTOR, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /*
    Responsible for the falling of the leaves.
     */
    private void leavesFalling() {
        handleLeafLayer();

        // start falling
        transform().setVelocityY(FALLING_SPEED);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);

        // horizontal transition
        this.horizontalTransition = new Transition<Float>(this,
                transform()::setVelocityX, INIT_VALUE_FALLING, FINAL_VALUE_FALLING,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        // fade out
        renderer().fadeOut(FADEOUT_TIME, () -> {
            int deathTime = this.random.nextInt(BOUND_DEATH_TIME);
            renderer().fadeOut(deathTime, this::renewLeaf);
        });
    }

    /*
    Handles the leaf layer during the leaves falling.
     */
    private void handleLeafLayer() {
        this.gameObjects.removeGameObject(this, this.leafLayerBeforeFalling);
        this.gameObjects.addGameObject(this, this.leafLayerWhenFalling);
        this.currentLeafLayer = this.leafLayerWhenFalling;
        // the leaves should collide with the ground
        this.gameObjects.layers().shouldLayersCollide(this.layerLeavesShouldCollide, this.currentLeafLayer,
                true);
    }

    /*
    Responsible for renewing a leaf on the top of the tree.
     */
    private void renewLeaf() {
        this.gameObjects.removeGameObject(this, this.leafLayerWhenFalling);
        transform().setAccelerationY(0);
        setVelocity(Vector2.ZERO);
        setTopLeftCorner(this.placeForLeaf);
        renderer().fadeIn(0);
        this.gameObjects.addGameObject(this, this.leafLayerBeforeFalling);
        createLeavesBehavior();
    }

    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(this.angleTransition);
        removeComponent(this.dimensionsTransition);
        removeComponent(this.horizontalTransition);
        transform().setAccelerationY(ACCELERATION_VALUE);
    }


    /**
     * Getter.
     * @return the layer of the object.
     */
    @Override
    public int getLayer() {
        return this.currentLeafLayer;
    }
}