package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Display a graphic object on the game window showing a numeric count of lives left.
 *
 * @author Yair Stern
 */
public class NumericLifeCounter extends GameObject {
    private static final String COUNTER_STRING = "Lives remaining: %d";
    private final TextRenderable textImage;
    private final Counter livesCounter;
    private int initialCounterVal;

    /**
     * Constructor
     *
     * @param livesCounter         - global lives counter of game.
     * @param topLeftCorner        - top left corner of renderable
     * @param dimensions           - dimensions of renderable.
     * @param gameObjectCollection - global game object collection.
     */
    public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner,
                              Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.initialCounterVal = livesCounter.value();
        textImage = new TextRenderable(String.format(COUNTER_STRING, livesCounter.value()));
        textImage.setColor(Color.cyan);
        this.renderer().setRenderable(textImage);
    }

    /**
     * @param deltaTime - The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() != initialCounterVal) {
            initialCounterVal--;
            //set the live counter string to the current value
            textImage.setString(String.format(COUNTER_STRING, initialCounterVal));
            this.renderer().setRenderable(textImage);
        }
    }
}