// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * An avatar can move around the world.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Avatar extends GameObject{
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final float ENERGY_VALUE = 100;
    private static final float AVATAR_SIZE = 40;
    private static final int VELOCITY_FACTOR = 250;
    private static final float Y_ACCELERATION_VALUE = 500;
    private static final float ENERGY_FACTOR = 0.5f;
    private static final double TIME_BETWEEN_CLIPS_FALLING = 0.1;
    private static final double TIME_BETWEEN_CLIPS_CRAWLING = 0.4;
    private static final double TIME_BETWEEN_CLIPS = 0.2;
    private static final int FACTOR_JUMP = 40;

    private float energy;
    private static UserInputListener inputListener;
    private static AnimationRenderable staticMoves;
    private static AnimationRenderable jumpingRightMoves;
    private static AnimationRenderable jumpingLeftMoves;
    private static AnimationRenderable flyingRightMoves;
    private static AnimationRenderable flyingLeftMoves;
    private static AnimationRenderable walkingRightMoves;
    private static AnimationRenderable walkingLeftMoves;
    private static AnimationRenderable fallingStraightMoves;
    private static AnimationRenderable fallingRightMoves;
    private static AnimationRenderable fallingLeftMoves;
    private static AnimationRenderable crawlingLeftMoves;
    private static AnimationRenderable crawlingRightMoves;
    private LastDirection lastDirection = LastDirection.STRAIGHT;

    // ------------------------------- ADDRESSES FOR IMAGES ---------------------------------
    private static final String PENGUIN_STATIC_PNG = "assets/penguinStatic.png";
    private static final String PENGUIN_LEFT_LEG_PNG = "assets/penguinLeftLeg.png";

    private static final String PENGUIN_STATIC_PNG1 = "assets/penguinStatic.png";
    private static final String PENGUIN_RIGHT_LEG_PNG = "assets/penguinRightLeg.png";
    private static final String PENGUIN_JUMP_1_R_PNG = "assets/penguinJump1R.png";
    private static final String PENGUIN_JUMP_2_R_PNG = "assets/penguinJump2R.png";
    private static final String PENGUIN_JUMP_3_R_PNG = "assets/penguinJump3R.png";
    private static final String PENGUIN_JUMP_1_L_PNG = "assets/penguinJump1L.png";
    private static final String PENGUIN_JUMP_2_L_PNG = "assets/penguinJump2L.png";
    private static final String PENGUIN_JUMP_3_L_PNG = "assets/penguinJump3L.png";

    private static final String PENGUIN_FLY_RIGHT_PNG = "assets/penguinFlyRight.png";
    private static final String PENGUIN_FLY_R_1_PNG = "assets/penguinFlyR1.png";
    private static final String PENGUIN_FLY_R_2_PNG = "assets/penguinFlyR2.png";
    private static final String PENGUIN_FLY_R_3_PNG = "assets/penguinFlyR3.png";
    private static final String PENGUIN_FLY_R_4_PNG = "assets/penguinFlyR4.png";
    private static final String PENGUIN_FLY_R_5_PNG = "assets/penguinFlyR5.png";
    private static final String PENGUIN_FLY_LEFT_PNG = "assets/penguinFlyLeft.png";
    private static final String PENGUIN_FLY_L_1_PNG = "assets/penguinFlyL1.png";
    private static final String PENGUIN_FLY_L_2_PNG = "assets/penguinFlyL2.png";
    private static final String PENGUIN_FLY_L_3_PNG = "assets/penguinFlyL3.png";
    private static final String PENGUIN_FLY_L_4_PNG = "assets/penguinFlyL4.png";
    private static final String PENGUIN_FLY_L_5_PNG = "assets/penguinFlyL5.png";

    private static final String PENGUIN_STATIC_RIGHT_PNG = "assets/penguinStaticRight.png";
    private static final String PENGUIN_WALK_R_1_PNG = "assets/penguinWalkR1.png";
    private static final String PENGUIN_WALK_R_2_PNG = "assets/penguinWalkR2.png";
    private static final String PENGUIN_WALK_R_3_PNG = "assets/penguinWalkR3.png";
    private static final String PENGUIN_WALK_R_4_PNG = "assets/penguinWalkR4.png";
    private static final String PENGUIN_WALK_R_5_PNG = "assets/penguinWalkR5.png";
    private static final String PENGUIN_WALK_R_6_PNG = "assets/penguinWalkR6.png";

    private static final String PENGUIN_STATIC_LEFT_PNG = "assets/penguinStaticLeft.png";
    private static final String PENGUIN_WALK_L_1_PNG = "assets/penguinWalkL1.png";
    private static final String PENGUIN_WALK_L_2_PNG = "assets/penguinWalkL2.png";
    private static final String PENGUIN_WALK_L_3_PNG = "assets/penguinWalkL3.png";
    private static final String PENGUIN_WALK_L_4_PNG = "assets/penguinWalkL4.png";
    private static final String PENGUIN_WALK_L_5_PNG = "assets/penguinWalkL5.png";
    private static final String PENGUIN_WALK_L_6_PNG = "assets/penguinWalkL6.png";

    private static final String PENGUIN_CRAWLING_R_1_PNG = "assets/penguinCrawlingR1.png";
    private static final String PENGUIN_CRAWLING_R_2_PNG = "assets/penguinCrawlingR2.png";
    private static final String PENGUIN_CRAWLING_L_1_PNG = "assets/penguinCrawlingL1.png";
    private static final String PENGUIN_CRAWLING_L_2_PNG = "assets/penguinCrawlingL2.png";

    private static final String PENGUIN_FALL_R_1_PNG = "assets/penguinFallR1.png";
    private static final String PENGUIN_FALL_R_2_PNG = "assets/penguinFallR2.png";
    private static final String PENGUIN_FALL_R_3_PNG = "assets/penguinFallR3.png";
    private static final String PENGUIN_FALL_R_4_PNG = "assets/penguinFallR4.png";
    private static final String PENGUIN_FALL_L_1_PNG = "assets/penguinFallL1.png";
    private static final String PENGUIN_FALL_L_2_PNG = "assets/penguinFallL2.png";
    private static final String PENGUIN_FALL_L_3_PNG = "assets/penguinFallL3.png";
    private static final String PENGUIN_FALL_L_4_PNG = "assets/penguinFallL4.png";


    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String AVATAR = "avatar";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new GameObject instance - An avatar.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    private Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = ENERGY_VALUE;
        transform().setAccelerationY(Y_ACCELERATION_VALUE);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * The can stand, walk, jump and fly, and never reaches the end of the world.
     * @param gameObjects   The collection of all participating game objects.
     * @param layer         The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader   Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        Avatar.inputListener = inputListener;

        Avatar avatar = new Avatar(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE), null);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.setTag(AVATAR);

        createStaticMoves(imageReader);
        createJumpingAnimation(imageReader);
        createFlyingAnimation(imageReader);
        createWalkingAnimation(imageReader);
        createCrawlingAnimation(imageReader);
        createFallingAnimation(imageReader);

        gameObjects.addGameObject(avatar, layer);
        return avatar;
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
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
            movementDir = avatarFlying(movementDir);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            movementDir = avatarJumping(Vector2.UP.mult(FACTOR_JUMP), jumpingRightMoves, jumpingLeftMoves);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = avatarTurnsInDirection(movementDir, Vector2.RIGHT, walkingRightMoves,
                    LastDirection.RIGHT);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = avatarTurnsInDirection(movementDir, Vector2.LEFT, walkingLeftMoves,
                    LastDirection.LEFT);
        }
        else {
            movementDir = handleAvatarFalling(movementDir);
        }
        if (getVelocity().x() == 0 && getVelocity().y() == 0) {
            movementDir = handleAvatarOnGround(movementDir);
        }
        setVelocity(movementDir.mult(VELOCITY_FACTOR));
    }

    /*
    Handles a situation where the avatar turns right or left.
     */
    private Vector2 avatarTurnsInDirection(Vector2 movementDir, Vector2 direction, AnimationRenderable
            walkingRightMoves, LastDirection lastDirection) {
        if (getVelocity().y() > 0) {
            movementDir = movementDir.add(Vector2.DOWN);
        }
        movementDir = getMovementDirection(movementDir, direction, walkingRightMoves);
        this.lastDirection = lastDirection;
        return movementDir;
    }

    /*
    Handles the avatar while it on the ground (or on top of tree) - namely rest time, and raises energy.
     */
    private Vector2 handleAvatarOnGround(Vector2 movementDir) {
        if (this.energy < ENERGY_VALUE) {
            this.energy += ENERGY_FACTOR;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_DOWN)){
            crawling();
        }
        else{
            movementDir = getMovementDirection(movementDir, Vector2.ZERO, staticMoves);
            this.lastDirection = LastDirection.STRAIGHT;
        }
        return movementDir;
    }

    /*
    Responsible for the crawling of the avatar. It's our addition for the functionality of the avatar - it
     can prostrate on the ground.
     */
    private void crawling() {
        if (this.lastDirection == LastDirection.RIGHT){
            renderer().setRenderable(crawlingRightMoves);
        }
        else {
            renderer().setRenderable(crawlingLeftMoves);
        }
    }

    /*
    Handles the falling of the avatar from above to the ground.
     */
    private Vector2 handleAvatarFalling(Vector2 movementDir) {
        AnimationRenderable fallingDown = getFallingDownMoves();
        movementDir = getMovementDirection(movementDir, Vector2.DOWN, fallingDown);
        return movementDir;
    }

    /*
    Returns the suitable animation renderable according to the last direction of the avatar in case of
    falling.
     */
    private AnimationRenderable getFallingDownMoves() {
        switch (this.lastDirection) {
            case RIGHT:
                return fallingRightMoves;
            case LEFT:
                return fallingLeftMoves;
            case STRAIGHT:
                return fallingStraightMoves;
        }
        return null;
    }


    /*
    Responsible for the jumping act of the avatar.
     */
    private Vector2 avatarJumping(Vector2 movementDir, AnimationRenderable rightMoves,
                                  AnimationRenderable leftMoves) {
        movementDir = movementDir.add(Vector2.UP);
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = getMovementDirection(movementDir, Vector2.RIGHT, rightMoves);
            this.lastDirection = LastDirection.RIGHT;
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = getMovementDirection(movementDir, Vector2.LEFT, leftMoves);
            this.lastDirection = LastDirection.LEFT;
        }
        return movementDir;
    }

    /*
      Responsible for the flying act of the avatar.
     */
    private Vector2 avatarFlying(Vector2 movementDir) {
        if (this.energy > 0) {
            movementDir = avatarJumping(movementDir, flyingRightMoves, flyingLeftMoves);
            this.energy -= ENERGY_FACTOR;
        } else { // empty of energy
            movementDir = handleAvatarFalling(movementDir);
            movementDir = movementDir.add(Vector2.DOWN);
        }
        return movementDir;
    }

    /*
    Returns the movement direction after updating it and renders the suitable animation renderable for the
    avatar.
     */
    private Vector2 getMovementDirection(Vector2 movementDir, Vector2 direction,
                                         AnimationRenderable animationRenderable) {
        movementDir = movementDir.add(direction);
        renderer().setRenderable(animationRenderable);
        return movementDir;
    }

    /**
     * Getter.
     * @return the energy value of the avatar.
     */
    public float getEnergyValue(){
        return this.energy;
    }


    /*
     Create the animated static steps of the avatar.
     */
    private static void createStaticMoves(ImageReader imageReader) {
        staticMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_STATIC_PNG, true),
                        imageReader.readImage(PENGUIN_LEFT_LEG_PNG, true),
                        imageReader.readImage(PENGUIN_STATIC_PNG1, true),
                        imageReader.readImage(PENGUIN_RIGHT_LEG_PNG, true)},
                TIME_BETWEEN_CLIPS);
    }

    /*
     Create the animated fall steps of the avatar.
     */
    private static void createFallingAnimation(ImageReader imageReader) {
        fallingRightMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_FALL_R_1_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_R_2_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_R_3_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_R_4_PNG, true)},
                TIME_BETWEEN_CLIPS_FALLING);

        fallingLeftMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_FALL_L_1_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_L_2_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_L_3_PNG, true),
                        imageReader.readImage(PENGUIN_FALL_L_4_PNG, true)},
                TIME_BETWEEN_CLIPS_FALLING);

        fallingStraightMoves = staticMoves;
    }

    /*
     Create the animated crawling steps of the avatar.
     */
    private static void createCrawlingAnimation(ImageReader imageReader) {
        crawlingRightMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_CRAWLING_R_1_PNG, true),
                        imageReader.readImage(PENGUIN_CRAWLING_R_2_PNG, true)},
                TIME_BETWEEN_CLIPS_CRAWLING);

        crawlingLeftMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_CRAWLING_L_1_PNG, true),
                        imageReader.readImage(PENGUIN_CRAWLING_L_2_PNG, true)},
                TIME_BETWEEN_CLIPS_CRAWLING);
    }

    /*
     Create the animated walking steps of the avatar.
     */
    private static void createWalkingAnimation(ImageReader imageReader) {
        walkingRightMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_STATIC_RIGHT_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_1_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_2_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_3_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_4_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_5_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_R_6_PNG, true)},
                TIME_BETWEEN_CLIPS);

        walkingLeftMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_STATIC_LEFT_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_1_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_2_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_3_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_4_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_5_PNG, true),
                        imageReader.readImage(PENGUIN_WALK_L_6_PNG, true)},
                TIME_BETWEEN_CLIPS);
    }

    /*
     Create the animated flying steps of the avatar.
     */
    private static void createFlyingAnimation(ImageReader imageReader) {
        flyingRightMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_FLY_RIGHT_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_R_1_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_R_2_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_R_3_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_R_4_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_R_5_PNG, true)},
                TIME_BETWEEN_CLIPS);

        flyingLeftMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_FLY_LEFT_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_L_1_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_L_2_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_L_3_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_L_4_PNG, true),
                        imageReader.readImage(PENGUIN_FLY_L_5_PNG, true)},
                TIME_BETWEEN_CLIPS);
    }

    /*
     Create the animated jumping steps of the avatar.
     */
    private static void createJumpingAnimation(ImageReader imageReader) {
        jumpingRightMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_JUMP_1_R_PNG, true),
                        imageReader.readImage(PENGUIN_JUMP_2_R_PNG, true),
                        imageReader.readImage(PENGUIN_JUMP_3_R_PNG, true)},
                TIME_BETWEEN_CLIPS);

        jumpingLeftMoves = new AnimationRenderable(new ImageRenderable[]
                {imageReader.readImage(PENGUIN_JUMP_1_L_PNG, true),
                        imageReader.readImage(PENGUIN_JUMP_2_L_PNG, true),
                        imageReader.readImage(PENGUIN_JUMP_3_L_PNG, true)},
                TIME_BETWEEN_CLIPS);
    }
}
