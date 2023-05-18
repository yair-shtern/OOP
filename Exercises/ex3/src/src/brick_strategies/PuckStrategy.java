package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

/**
 * Introduces several pucks instead of brick once removed.
 *
 * @author Yair Stern
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator implements CollisionStrategy {
    private static final int NUM_OF_PUCK_RELEASE = 3;
    private static final float PUCK_SPEED = 160;
    private static final float DIC_FROM_BRICK = 20;
    private static final String MOCK_BALL_IMAGE_PATH = "assets/mockBall.png";
    private static final String MOCK_BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private final ImageRenderable ballImage;
    private static Sound collisionSound = null;
    private final int[] direction = new int[]{-1, 0, 1};


    /**
     * Constructor
     *
     * @param toBeDecorated - a basic collision strategy (remove), to decorate with mor behaviors.
     * @param imageReader   - image reader to display on screen.
     * @param soundReader   - sound reader to display when there is some collision.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        ballImage = imageReader.readImage(MOCK_BALL_IMAGE_PATH, true);
        if (collisionSound == null) {
            collisionSound = soundReader.readSound(MOCK_BALL_SOUND_PATH);
        }
    }

    /**
     * Add pucks to game on collision and delegate to held CollisionStrategy.
     *
     * @param thisObj  - brick
     * @param otherObj - ball
     * @param counter  - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        float radius = thisObj.getDimensions().x() / 3;
        //create 3 pucks falling down left, right and straight.
        for (int i = 0; i < NUM_OF_PUCK_RELEASE; i++) {
            Puck puck = new Puck(
                    Vector2.ZERO, new Vector2(radius, radius), ballImage, collisionSound);
            puck.setVelocity(new Vector2(PUCK_SPEED * direction[i], PUCK_SPEED));
            puck.setCenter(new Vector2(thisObj.getCenter().x(), thisObj.getCenter().y() + DIC_FROM_BRICK));
            getGameObjectCollection().addGameObject(puck);
        }
    }
}
