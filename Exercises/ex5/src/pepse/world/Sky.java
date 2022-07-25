package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Represents the sky.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Sky {

    // -------------------------------------- PRIVATE -------------------------------------
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String SKY = "sky";

    // -------------------------------------- METHODS --------------------------------------
    /**
     * This function creates a light blue rectangle which is always at the back of the window.
     * @param gameObjects  The collection of all participating game objects.
     * @param windowDimensions The number of the layer to which the created game object should be added.
     * @param skyLayer The number of the layer to which the created sky should be added.
     * @return A new game object representing the sky.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SKY);
        return sky;
    }
}
