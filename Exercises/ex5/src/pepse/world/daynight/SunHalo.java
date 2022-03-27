// USER_NAMES = hilla_heimberg , yair.shtern
// IDS - 208916221 , 318442241

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Represents the halo of sun.
 * @author Hilla Heimberg, Yair Shtern
 */
public class SunHalo {
    // -------------------------------------- PRIVATE -------------------------------------
    private static final int FACTOR_FOR_DIMENSION = 2;

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String HALO = "halo";

    // -------------------------------------- METHODS --------------------------------------

    /**
     * This function creates a halo around a given object that represents the sun.
     * The halo will be tied to the given sun, and will always move with it.
     *
     * @param gameObjects The collection of all participating game objects.
     * @param layer       The number of the layer to which the created halo should be added.
     * @param sun         A game object representing the sun (it will be followed by the created game object).
     * @param color       The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color) {
        OvalRenderable haloRenderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(FACTOR_FOR_DIMENSION),
                haloRenderable);
        sunHalo.setCoordinateSpace(sun.getCoordinateSpace());
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(HALO);

        return sunHalo;
    }
}