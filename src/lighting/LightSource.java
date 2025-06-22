package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface for light sources in a 3D scene.
 * <p>
 * Provides methods for computing the intensity and direction of light at a specific point.
 * This allows different types of light sources (like point lights, directional lights, spotlights)
 * to be used interchangeably in rendering.
 */
public interface LightSource {

    /**
     * Returns the intensity (color and strength) of the light at a specific point in the scene.
     *
     * @param p the point in the scene
     * @return the color representing the light's intensity at point {@code p}
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source toward a specific point.
     * This vector is normalized.
     *
     * @param p the point in the scene
     * @return a normalized {@link Vector} pointing from the light source to point {@code p}
     */
    Vector getL(Point p);

    /**
     * Returns the distance from the light source to the given point.
     * For infinite light sources (like {@code DirectionalLight}), this may return {@code Double.POSITIVE_INFINITY}.
     *
     * @param point the point in the scene
     * @return the distance to the light source from point {@code point}
     */
    double getDistance(Point point);
}
