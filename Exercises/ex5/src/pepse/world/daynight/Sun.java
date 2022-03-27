// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 * @author Hilla Heimberg, Yair Shtern
 */
public class Sun {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final float SUN_RADIUS = 120;
    private static final float FINAL_ANGLE = 360f;
    private static final float START_ANGLE = 0f;
    private static final float FACTOR_FOR_DIMENSIONS = 0.5f;
    private static final int DIVISION_FACTOR = 2;

    private static final Color SUN_COLOR = new Color(238, 223, 140, 255);

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String SUN = "sun";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * This function creates a yellow circle that moves in the sky in an elliptical path (in camera
     * coordinates).
     *
     * @param windowDimensions The dimensions of the windows.
     * @param cycleLength      The amount of seconds it should take the created game object to complete a full
     *                        cycle.
     * @param gameObjects      The collection of all participating game objects.
     * @param layer            The number of the layer to which the created sun should be added.
     * @return A new game object representing the sun.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable sunRenderable = new OvalRenderable(SUN_COLOR);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SUN_RADIUS, SUN_RADIUS), sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN);
        gameObjects.addGameObject(sun, layer);

        sunCycleTransition(windowDimensions, cycleLength, sun);

        return sun;
    }

    /*
    Responsible for the cycle transition of the sun.
     */
    private static void sunCycleTransition(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        Vector2 centerOfSun = new Vector2(windowDimensions.mult(FACTOR_FOR_DIMENSIONS));
        Vector2 xStretchForEllipse = Vector2.LEFT.mult((windowDimensions.x()/DIVISION_FACTOR));
        Vector2 yStretchForEllipse = Vector2.UP.mult((windowDimensions.y()/DIVISION_FACTOR));

        new Transition<Float>(sun,
                angle -> sun.setCenter(
                        centerOfSun.add(xStretchForEllipse.mult((float) Math.sin(Math.toRadians(angle)))).
                                add(yStretchForEllipse.mult((float) Math.cos(Math.toRadians(angle))))),
                START_ANGLE, FINAL_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }
}