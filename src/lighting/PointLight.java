package lighting;

import primitives.*;

/**
 * Represents a point light source that emits light uniformly in all directions from a specific position in space.
 * <p>
 * Supports distance attenuation using constant, linear, and quadratic factors to simulate the weakening
 * of light over distance.
 */
public class PointLight extends Light implements LightSource {

    /** The position of the point light in 3D space */
    protected final Point position;

    /** Constant attenuation factor (default 1) */
    private double kC = 1.0;

    /** Linear attenuation factor (default 0) */
    private double kL = 0.0;

    /** Quadratic attenuation factor (default 0) */
    private double kQ = 0.0;

    /**
     * Constructs a point light source with a given intensity and position.
     *
     * @param intensity the base light color/intensity
     * @param position  the 3D position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor (kC).
     *
     * @param kc the constant attenuation coefficient
     * @return the current {@code PointLight} instance (for method chaining)
     */
    public PointLight setkC(double kc) {
        this.kC = kc;
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL).
     *
     * @param kl the linear attenuation coefficient
     * @return the current {@code PointLight} instance (for method chaining)
     */
    public PointLight setKl(double kl) {
        this.kL = kl;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ).
     *
     * @param kq the quadratic attenuation coefficient
     * @return the current {@code PointLight} instance (for method chaining)
     */
    public PointLight setKq(double kq) {
        this.kQ = kq;
        return this;
    }

    /**
     * Calculates and returns the attenuated light intensity at a given point in the scene.
     * <p>
     * The intensity is reduced based on the distance from the light source using the formula:
     * <pre>
     *     intensity / (kC + kL*d + kQ*d²)
     * </pre>
     *
     * @param p the point at which to calculate the light intensity
     * @return the attenuated {@link Color} intensity at point {@code p}
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distanceSquared(p);
        double sqrted = Math.sqrt(d);
        double factor = 1.0 / (kC + kL * sqrted + kQ * sqrted * sqrted);
        return intensity.scale(factor);
    }

    /**
     * Returns the normalized direction vector from the light source to a given point.
     *
     * @param p the point in the scene
     * @return a normalized {@link Vector} pointing from the light source to point {@code p},
     *         or {@code null} if {@code p} equals the light's position
     */
    @Override
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns the distance between the light source and a given point.
     *
     * @param point the point to calculate distance to
     * @return the distance between the light source and point {@code point}
     */
    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
