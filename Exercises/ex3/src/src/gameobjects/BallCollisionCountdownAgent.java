package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball with a brick with a change camera strategy.
 * It checks ball's collision counter every frame, and once it finds the ball has collided
 * countDownValue times since instantiation, it calls the strategy to reset the camera to normal.
 *
 * @author Yair Stern
 */
public class BallCollisionCountdownAgent extends GameObject {
    private final Ball ball;
    private final ChangeCameraStrategy owner;
    private final int countDownValue;

    /**
     * Constructor
     *
     * @param ball           - Ball object whose collisions are to be counted.
     * @param owner          - Object asking for countdown notification.
     * @param countDownValue - Number of ball collisions. Notify caller object that the ball
     *                       collided countDownValue times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball,
                                       ChangeCameraStrategy owner,
                                       int countDownValue) {
        super(ball.getTopLeftCorner(), ball.getDimensions(), null);
        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
    }

    /**
     * @param deltaTime - The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() >= countDownValue) {
            owner.turnOffCameraChange();
            owner.getGameObjectCollection().removeGameObject(this, Layer.BACKGROUND);
        }
    }
}

