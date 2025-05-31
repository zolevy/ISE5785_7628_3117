package lighting;
import primitives.*;

/**
 * Represents a point light source emitting light from a specific position in space.
 * Supports attenuation with distance using constant, linear, and quadratic factors.
 */
public class PointLight extends Light implements LightSource {
    protected final Point position;
    private double kC = 1.0;
    private double kL = 0.0;
    private double kQ = 0.0;

    /**
     * Constructs a PointLight with specified intensity and position.
     *
     * @param intensity the color/intensity of the light
     * @param position  the position of the light source in space
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kc constant factor
     * @return this PointLight instance for chaining
     */
    public PointLight setkC(double kc) {
        this.kC = kc;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kl linear factor
     * @return this PointLight instance for chaining
     */
    public PointLight setKl(double kl) {
        this.kL = kl;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kq quadratic factor
     * @return this PointLight instance for chaining
     */
    public PointLight setKq(double kq) {
        this.kQ = kq;
        return this;
    }

    /**
     * Calculates the light intensity at point p, considering distance attenuation.
     *
     * @param p the point to calculate intensity for
     * @return the attenuated color intensity at point p
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distanceSquared(p);
        double sqrted = Math.sqrt(d);
        double factor = 1.0 / (kC + kL * sqrted + kQ * sqrted * sqrted);
        return intensity.scale(factor);
    }

    /**
     * Returns the normalized vector from the light position to point p.
     *
     * @param p the point in the scene
     * @return normalized direction vector from the light to p, or null if p equals position
     */
    @Override
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
