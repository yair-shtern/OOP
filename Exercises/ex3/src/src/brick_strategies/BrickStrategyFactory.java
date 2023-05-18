package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.*;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

/**
 * Factory class for creating Collision strategies.
 *
 * @author Yair Stern
 */
public class BrickStrategyFactory {
    private static final int NUM_OF_STRATEGIES = 6;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final SoundReader soundReader;
    private final BrickerGameManager gameManager;
    private final Random random;

    /**
     * Constructor
     *
     * @param gameObjects      - the game object collection.
     * @param gameManager      - the brick game manager.
     * @param imageReader      - image reader to display on screen.
     * @param soundReader      - sound reader to display when there is some collision.
     * @param inputListener    - input from user (can be a mouse or keyboard act).
     * @param windowController - the game window controller.
     * @param windowDimensions - the game window dimensions.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjects,
                                BrickerGameManager gameManager,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.gameManager = gameManager;
        random = new Random();
    }

    /**
     * method randomly selects between 5 strategies and returns one CollisionStrategy object
     * which is a RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies,
     * or decorated by one of the decorator strategies and a pair of additional two decorator strategies.
     *
     * @return CollisionStrategy object.
     */
    public CollisionStrategy getStrategy() {
        //choose randomly between the possible brick strategies
        int num = NUM_OF_STRATEGIES;
        //randomly choose behavior (0,..,5)
        if (random.nextInt(num) == 0) {
            if (random.nextInt(num - 1) == 0) {
                //3 behaviors (0,...4) 1/30 chance.
                CollisionStrategy[] behaviors = {
                        getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 2),
                        getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 2),
                        getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 2)};
                return new DoubleBehavior(behaviors, 3);
            }//2 behaviors. 1/6 chance to choose it.
            CollisionStrategy[] behaviors = {
                    getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 2),
                    getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 2)};
            return new DoubleBehavior(behaviors, 2);
        }//1 behavior
        return getCollisionStrategy(new RemoveBrickStrategy(gameObjects), num - 1);
    }

    private CollisionStrategy getCollisionStrategy(CollisionStrategy toBeDecorated, int num) {
        //privet method return random brick collision strategy.
        switch (random.nextInt(num)) {
            case 0:
                return new AddPaddleStrategy(toBeDecorated, imageReader, inputListener, windowDimensions);
            case 1:
                return new ChangeCameraStrategy(toBeDecorated, windowController, gameManager);
            case 2:
                return new PuckStrategy(toBeDecorated, imageReader, soundReader);
            case 3:
                return new ReduceAndExpandStrategy(toBeDecorated, imageReader, windowDimensions);
        }//case 4
        return toBeDecorated;
    }
}