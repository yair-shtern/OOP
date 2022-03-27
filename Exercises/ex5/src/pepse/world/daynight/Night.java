// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Darkens the entire window.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Night{
    // -------------------------------------- PRIVATE -------------------------------------
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT = "night";
    private static final float INIT_VALUE_TRANSITION = 0f;

    // -------------------------------------- METHODS --------------------------------------
    /**
     * This function creates a black rectangular game object that covers the entire game window and changes
     * its opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     * @param gameObjects - The collection of all participating game objects.
     * @param layer - The number of the layer to which the created game object should be added.
     * @param  windowDimensions - The dimensions of the windows.
     * @param  cycleLength - The amount of seconds it should take the created game object to complete a full cycle.
     * @return A new game object representing day-to-night transitions.
     */
    public static GameObject create(GameObjectCollection gameObjects,int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        RectangleRenderable nightRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT);
        gameObjects.addGameObject(night, layer);

        new Transition<Float>(night, night.renderer()::setOpaqueness, INIT_VALUE_TRANSITION, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        return night;
    }
}
