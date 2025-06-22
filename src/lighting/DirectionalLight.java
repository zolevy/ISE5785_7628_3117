package lighting;

import primitives.*;

/**
 * Represents a directional light source with parallel rays in a specific direction.
 * This type of light simulates a far-away light source like the sun,
 * which means all rays are parallel and have constant intensity.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a new {@code DirectionalLight} with the given intensity and direction.
     *
     * @param intensity the color/intensity of the light
     * @param direction the direction vector of the light rays (will be normalized)
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Returns the direction of the light at the specified point.
     * For directional light, the direction is constant and does not depend on the point.
     *
     * @param p the point at which the direction is requested (ignored)
     * @return the normalized direction vector of the light
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Returns the intensity of the light at the specified point.
     * For directional light, the intensity is constant and does not depend on the point.
     *
     * @param p the point at which the intensity is requested (ignored)
     * @return the constant intensity of the directional light
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Returns the distance from the light source to the given point.
     * For directional light, this distance is infinite since it represents a far-away source.
     *
     * @param point the point to which the distance is requested
     * @return {@code Double.POSITIVE_INFINITY} indicating infinite distance
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
