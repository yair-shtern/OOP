package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.carriableObjects.HandleCarriableObjects;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;

/**
 * The main class of the simulator.
 * @author Hilla Heimberg, Yair Shtern
 */
public class PepseGameManager extends GameManager {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final int SEED = 75;
    private static final int CYCLE_LENGTH = 30;
    private static final int GOAL_SCORE = 20;
    private static final int GAP_FROM_BORDER = 8;
    private static final int LOSING_VALUE = -1;
    private static final int SCORE_SIZE = 20;
    private static final int VALUE_FOR_CORNER = 20;
    private static final int GAP_FROM_EDGE = 160;
    private static final int Y_VALUE = 10;
    private static final int X_DIMENSION = 150;
    private static final int Y_DIMENSION = 30;

    // LAYERS FOR THE GAME
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int HALO_LAYER = Layer.BACKGROUND + 2;
    private static final int TREES_LAYER = Layer.BACKGROUND + 3;
    private static final int LEAF_LAYER = Layer.BACKGROUND + 4;
    private static final int LEAVES_LAYER_WHEN_FALLING = Layer.BACKGROUND + 5;
    private static final int CARRIABLE_OBJECT_LAYER = Layer.BACKGROUND + 6;
    private static final int SCORE_COUNTER_LAYER = Layer.BACKGROUND + 7;
    private static final int ENERGY_BAR_LAYER = Layer.BACKGROUND + 8;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int AVATAR_LAYER = Layer.DEFAULT;

    private Avatar avatar;
    private Terrain terrain;
    private Tree tree;
    private Counter scoreCounter;
    private HandleCarriableObjects handleCarriableObjects;
    private WindowController windowController;
    private Sound gameSound;
    private Sound soundForLosing;
    private Sound soundForWinning;
    private float windowXCenter;
    private float currentRightEdge;
    private float currentLeftEdge;
    private boolean firstGame = true;
    private static final Color HALO_SUN_COLOR = new Color(255, 255, 0, 20);

    // --------------------------------- MESSAGES FOR USER ---------------------------------
    private static final String LOSING_MESSAGE = "You lose! :(   ";
    private static final String WINNING_MESSAGE = "You won! :)   ";
    private static final String PLAY_AGAIN_MESSAGE = " Play again?";
    private static final String OPENING_GAME_MESSAGE = "Welcome to our game!\n" +
            "\nYou should gain %d points by picking up fish and " +
            "coins. \n Watch out from the toxic fish. \n Be aware of your energy and charge yourself on " +
            "the ground. \n *press space+shift for flying. \n *press space for jumping. \n Do you want to" +
            " play? :D \n\n@COPYRIGHT - Hilla Heimberg & Yair Shtern";

    // ------------------------------- ADDRESSES FOR SOUNDS ---------------------------------
    private static final String ASSETS_FAILING_SOUND_WAV = "assets/failingSound.wav";
    private static final String ASSETS_WINNING_SOUND_WAV = "assets/winningSound.wav";
    private static final String ASSETS_GAME_SOUND_WAV = "assets/gameSound.wav";
    private Vector2 windowDimensions;

    // -------------------------------------- METHODS --------------------------------------

    /**
     * The main function which runs the game, namely the entry point for game.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     * @see ImageReader
     * @see SoundReader
     * @see UserInputListener
     * @see WindowController
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // initialize game variables that should update only once
        if (this.firstGame) {
            // representing the opening game message
            if (!(windowController.openYesNoDialog(String.format(OPENING_GAME_MESSAGE, GOAL_SCORE)))) {
                windowController.closeWindow();
                return;
            }
            initializeForFirstGame(soundReader, windowController);
        }

        //initialize borders
        this.currentRightEdge = (int) (windowDimensions.x() / Block.SIZE) * Block.SIZE + GAP_FROM_BORDER
                * Block.SIZE;
        this.currentLeftEdge = -GAP_FROM_BORDER * Block.SIZE;
        this.windowXCenter = this.currentRightEdge / 2;

        // initialize score counter
        this.scoreCounter = new Counter();

        // create sky
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);

        // create terrain
        this.terrain = new Terrain(gameObjects(), TERRAIN_LAYER, windowDimensions, SEED);
        this.terrain.createInRange((int) this.currentLeftEdge, (int) this.currentRightEdge);

        // create night
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, CYCLE_LENGTH);

        // create sun
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, CYCLE_LENGTH);

        // create sunHalo
        SunHalo.create(gameObjects(), HALO_LAYER, sun, HALO_SUN_COLOR);

        // create trees
        this.tree = new Tree(gameObjects(), terrain::groundHeightAt, TREES_LAYER, LEAF_LAYER,
                LEAVES_LAYER_WHEN_FALLING, TERRAIN_LAYER, AVATAR_LAYER, SEED);
        this.tree.createInRange((int) this.currentLeftEdge, (int) this.currentRightEdge);

        // create avatar
        this.avatar = Avatar.create(gameObjects(), AVATAR_LAYER, Vector2.ZERO, inputListener, imageReader);
        Vector2 avatarLocation = Vector2.RIGHT.mult(windowDimensions.x() / 2);
        this.avatar.setCenter(avatarLocation);

        // set camera to follow the avatar
        Vector2 locationToFollow = avatarLocation.subtract(this.avatar.getCenter());
        setCamera(new Camera(avatar, locationToFollow, windowDimensions, windowDimensions));

        // create carriable object
        this.handleCarriableObjects = new HandleCarriableObjects(gameObjects(), imageReader, soundReader,
                CARRIABLE_OBJECT_LAYER, SEED, terrain::groundHeightAt ,AVATAR_LAYER, this.scoreCounter);
        this.handleCarriableObjects.createInRange((int) this.currentLeftEdge, (int) this.currentRightEdge);

        // create game score counter
        new ScoreCounter(Vector2.ONES.mult(VALUE_FOR_CORNER),
                Vector2.ONES.mult(SCORE_SIZE), gameObjects(), this.scoreCounter, SCORE_COUNTER_LAYER);

        // create energy progress bar
        new Energy(new Vector2(windowDimensions.x() - GAP_FROM_EDGE, Y_VALUE),
                new Vector2(X_DIMENSION, Y_DIMENSION), gameObjects(), imageReader, ENERGY_BAR_LAYER,
                this.avatar::getEnergyValue);

        // play game music
        this.gameSound.playLooped();
    }

    /*
    Responsible for initialize game variable that need to update only once
    */
    private void initializeForFirstGame(SoundReader soundReader, WindowController windowController) {
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.soundForLosing = soundReader.readSound(ASSETS_FAILING_SOUND_WAV);
        this.soundForWinning = soundReader.readSound(ASSETS_WINNING_SOUND_WAV);
        this.gameSound = soundReader.readSound(ASSETS_GAME_SOUND_WAV);
        this.firstGame = false;
    }

    /*
    Checks if the game ended and presents informative message for the player.
     */
    private void checkForGameEnd() {
        String prompt = "";
        if (this.scoreCounter.value() <= LOSING_VALUE) {
            this.soundForLosing.play();
            prompt = LOSING_MESSAGE;
        }
        if (this.scoreCounter.value() >= GOAL_SCORE) {
            this.soundForWinning.play();
            prompt = WINNING_MESSAGE;
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_MESSAGE;
            this.gameSound.stopAllOccurences();
            if (this.windowController.openYesNoDialog(prompt)) {
                this.windowController.resetGame();
            }
            else {
                this.windowController.closeWindow();
            }
        }
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * Note that the time that passes between subsequent calls to this method is not constant.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float avatarXCenter = this.avatar.getCenter().x();

        if (avatarXCenter - this.windowXCenter > 0) { // if the avatar crosses the right border
            createInfiniteWorld((int) this.currentRightEdge, (int) this.currentRightEdge + Block.SIZE);
            removeObjectsInDeadZone((int) this.currentLeftEdge, (int) this.currentLeftEdge + Block.SIZE);
            updateEdges( Block.SIZE);
        }
        else if (avatarXCenter - this.windowXCenter < -Block.SIZE) { // if the avatar crosses the left border
            createInfiniteWorld((int) this.currentLeftEdge - Block.SIZE, (int) this.currentLeftEdge);
            removeObjectsInDeadZone((int) this.currentRightEdge - Block.SIZE, (int) this.currentRightEdge);
            updateEdges(-Block.SIZE);
        }
        checkForGameEnd();
    }

    /*
    Create infinite world, according to the avatar advance.
     */
    private void createInfiniteWorld(int minX, int maxX) {
        this.terrain.createInRange(minX, maxX);
        this.tree.createInRange(minX, maxX);
        this.handleCarriableObjects.createInRange(minX, maxX);
    }

    /*
    Remove objects from the dead zone during avatar walking in the game.
     */
    private void removeObjectsInDeadZone(int minX, int maxX) {
        this.terrain.removeInRange(minX, maxX);
        this.tree.removeInRange(minX, maxX);
        this.handleCarriableObjects.removeInRange(minX, maxX);
    }

    /*
    Update the edges of the screen game according to the avatar advance.
     */
    private void updateEdges(int valueForUpdate) {
        this.currentRightEdge += valueForUpdate;
        this.currentLeftEdge += valueForUpdate;
        this.windowXCenter += valueForUpdate;
    }

}