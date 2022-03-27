package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a graphic object on the game window showing as many widgets as lives left.
 *
 * @author Yair Stern
 */
public class GraphicLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private final int numOfLives;

    /**
     * Constructor
     *
     * @param widgetTopLeftCorner   - top left corner of left most life widgets.
     *                              Other widgets will be displayed to its right, aligned in hight.
     * @param widgetDimensions      - dimensions of widgets to be displayed.
     * @param livesCounter          - global lives counter of game.
     * @param widgetRenderable      - image to use for widgets.
     * @param gameObjectsCollection - global game object collection managed by game manager.
     * @param numOfLives            - global setting of number of lives a player will have in a game.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
    }

    /**
     * Override update check if liveCounter < num of lives and remove the object if so
     *
     * @param deltaTime - The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            gameObjectsCollection.removeGameObject(this, Layer.BACKGROUND);
        }
    }
}
