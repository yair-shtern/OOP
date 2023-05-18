package src;

import src.brick_strategies.BrickStrategyFactory;
import src.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

import java.util.Random;

/**
 * Game manager - this class is responsible for game initialization,
 * holding references for game objects and calling update methods for every update iteration.
 * Entry point for code should be in a main method in this class.
 *
 * @author Yair Stern
 */
public class BrickerGameManager extends GameManager {

    /**
     * public boarder width.
     */
    public static final int BORDER_WIDTH = 20;

    ////// private //////
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 20;
    private static final float BALL_RADIUS = 20;
    private static final float BALL_SPEED = 200;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final int BRICKS_IN_ROW = 8;
    private static final int BRICKS_IN_COL = 5;
    private static final float BRICK_HEIGHT = 15;
    private static final float SPACE_BETWEEN_BRICKS = 1;
    private static final int LIVES_NUM = 4;
    private static final float NUM_WIDTH = 15;
    private static final float HEART_WIDTH = 25;
    private static final float NUMERIC_HEIGHT = 60;
    private static final float HEARTS_HEIGHT = 35;
    private static final float START_Y_PADDLE = 30;
    private static final int SPACE_BETWEEN_HEARTS = 35;
    private static final float WINDOW_WIDTH = 700;
    private static final float WINDOW_HEIGHT = 500;
    private static final String WIN_MSG = "you won!";
    private static final String LOSE_MSG = "you lose!";
    private static final String REPLAY_MSG = " Play again?";
    private static final String HEARTS_IMAGE_PATH = "assets/heart.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String WINDOW_TITLE = "Bricker";

    private Ball ball;
    private WindowController windowController;
    private Counter lifeCounter;
    private Counter bricksCounter;

    /**
     * Constructor
     *
     * @param WindowTitle      - the game window title.
     * @param WindowDimensions - pixel dimensions for game window height x width.
     */
    public BrickerGameManager(String WindowTitle, Vector2 WindowDimensions) {
        super(WindowTitle, WindowDimensions);
    }

    /**
     * Calling this function should initialize the game window.
     * It should initialize objects in the game window - ball, paddle, walls, life counters, bricks.
     * This version of the game has 5 rows, 8 columns of bricks.
     *
     * @param imageReader      - an ImageReader instance for reading
     *                         images from files for rendering of objects.
     * @param soundReader      - a SoundReader instance
     *                         for reading soundclips from files for rendering event sounds.
     * @param inputListener    - an InputListener instance for reading user input.
     * @param windowController - controls visual rendering of the game window and object renderables.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;

        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(80);
        MockPaddle.isInstantiated = false;
        lifeCounter = new Counter(LIVES_NUM);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        //create background
        createBackground(imageReader, windowController);

        //create borders
        createBorders(windowDimensions);

        //create ball
        createBall(imageReader, soundReader);

        //create paddle
        createPaddle(imageReader, inputListener, windowDimensions);

        //create graphic life counter
        createGraphicLifeCounter(imageReader, windowDimensions);

        //create numeric life counter
        createNumericLifeCounter(windowDimensions);

        //create collision strategy factory
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(
                gameObjects(), this, imageReader, soundReader, inputListener
                , windowController, windowDimensions);

        //create Bricks
        createBricks(imageReader, windowDimensions, brickStrategyFactory);
    }

    private void createNumericLifeCounter(Vector2 windowDimensions) {
        //create and adds numeric life counter in the bottom left corner (above the graphic)
        GameObject numericLifeCounter = new NumericLifeCounter(lifeCounter, new Vector2(BORDER_WIDTH,
                windowDimensions.y() - NUMERIC_HEIGHT), Vector2.ONES.mult(NUM_WIDTH), gameObjects());
        numericLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    private void createGraphicLifeCounter(ImageReader imageReader, Vector2 windowDimensions) {
        //create and adds 4 graphic life counters in the bottom left corner
        Renderable heartImage = imageReader.readImage(HEARTS_IMAGE_PATH, true);
        for (int i = 0; i < LIVES_NUM; i++) {
            GameObject graphicLifeCounter = new GraphicLifeCounter(
                    new Vector2(BORDER_WIDTH + i * SPACE_BETWEEN_HEARTS,
                            windowDimensions.y() - HEARTS_HEIGHT),
                    Vector2.ONES.mult(HEART_WIDTH), lifeCounter, heartImage, gameObjects(), i + 1);
            graphicLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);
        }
    }

    /**
     * Code in this function is run every frame update.
     *
     * @param deltaTime - The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float ballHeight = ball.getCenter().y();
        checkForGameEnd(ballHeight);
        for (GameObject object : gameObjects()) {
            if (object.getCenter().y() > windowController.getWindowDimensions().y()) {
                gameObjects().removeGameObject(object);
            }
        }
    }

    private void checkForGameEnd(float ballHeight) {
        String prompt = "";
        if (bricksCounter.value() == 0) {
            //no more bricks
            prompt = WIN_MSG;
        }
        if (ballHeight > windowController.getWindowDimensions().y()) {
            //ball out of the game
            lifeCounter.decrement();
            if (lifeCounter.value() == 0) {
                //no more lives
                prompt = LOSE_MSG;
            } else {
                //return the ball to the center
                repositionBall(ball);
            }
        }
        if (!prompt.isEmpty()) {
            //lose / win
            prompt += REPLAY_MSG;
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }


    private void createBricks(ImageReader imageReader,
                              Vector2 windowDimensions,
                              BrickStrategyFactory brickStrategyFactory) {
        //creates and 40 bricks and place them on the window
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        float windowMinusSpaces =
                windowDimensions.x() - (2 * BORDER_WIDTH + SPACE_BETWEEN_BRICKS * (BRICKS_IN_ROW - 1));
        float brickWidth = windowMinusSpaces / BRICKS_IN_ROW;
        bricksCounter = new Counter(BRICKS_IN_ROW * BRICKS_IN_COL);
        for (int i = 0; i < BRICKS_IN_ROW; i++) {
            for (int j = 0; j < BRICKS_IN_COL; j++) {
                GameObject brick = new Brick(
                        new Vector2(i * (brickWidth + SPACE_BETWEEN_BRICKS) + BORDER_WIDTH,
                                j * (BRICK_HEIGHT + SPACE_BETWEEN_BRICKS) + BORDER_WIDTH),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                        //get random strategy
                        brickStrategyFactory.getStrategy(), bricksCounter);
                gameObjects().addGameObject(brick);
            }
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        //creates and adds the main ball of the game
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        Ball ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        this.ball = ball;
        repositionBall(ball);
        ball.setCenter(windowController.getWindowDimensions().mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    public void repositionBall(GameObject ball) {
        //place the ball in the center of the window and set it velocity randomly to one of four directions
        float ballValX = BALL_SPEED;
        float ballValY = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballValX *= -1;
        }
        if (random.nextBoolean()) {
            ballValY *= -1;
        }
        ball.setVelocity(new Vector2(ballValX, ballValY));
        ball.setCenter(windowController.getWindowDimensions().mult(0.5f));
    }

    private void createPaddle(ImageReader imageReader,
                              UserInputListener inputListener,
                              Vector2 windowDimensions) {
        //creates and adds paddle to the game
        Renderable peddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                peddleImage, inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - START_Y_PADDLE));
        gameObjects().addGameObject(paddle);
    }

    private void createBackground(ImageReader imageReader, WindowController windowController) {
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(),
                imageReader.readImage(BACKGROUND_IMAGE_PATH, false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBorders(Vector2 windowDimensions) {
        GameObject leftBorder = new GameObject(
                Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(leftBorder);
        GameObject rightBorder = new GameObject(
                new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(rightBorder);
        GameObject upBorder = new GameObject(
                Vector2.ZERO, new Vector2(windowDimensions.x(), (float) BORDER_WIDTH), null);
        gameObjects().addGameObject(upBorder);
    }

    /**
     * Entry point for game. Should contain:
     * 1. An instantiation call to BrickerGameManager constructor.
     * 2. A call to run() method of instance of BrickerGameManager.
     * Should initialize game window of dimensions (x,y) = (700,500).
     *
     * @param args commend line arguments.
     */
    public static void main(String[] args) {
        //restart game with 700 x 500 pixels
        new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }
}


