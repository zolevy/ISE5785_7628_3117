package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface for light sources in the scene.
 * Defines methods to get light intensity and direction at a given point.
 */
public interface LightSource {

    /**
     * Returns the light intensity at a given point.
     *
     * @param p the point in the scene to calculate the light intensity
     * @return the color intensity of the light at point p
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the given point.
     *
     * @param p the point in the scene
     * @return the normalized vector representing the direction of the light at point p
     */
    Vector getL(Point p);
}
