package lighting;

import primitives.*;

/**
 * Represents a directional light source with parallel rays in a given direction.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a directional light with specified intensity and direction.
     *
     * @param intensity the light color/intensity
     * @param direction the light direction vector
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Returns the direction of the light at point p.
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Returns the light intensity at point p (constant for directional light).
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }
}
