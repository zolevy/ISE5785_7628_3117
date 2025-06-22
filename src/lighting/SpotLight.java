package lighting;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * Represents a spotlight, a light source that emits a cone of light in a specific direction from a given position.
 * <p>
 * Inherits from {@link PointLight}, adding directionality to the light.
 * The light intensity decreases with both distance and the angle from the spotlight direction.
 */
public class SpotLight extends PointLight {

    /** The normalized direction vector of the spotlight */
    private final Vector direction;

    /**
     * Constructs a {@code SpotLight} with the specified intensity, position, and direction.
     *
     * @param intensity the color/intensity of the light
     * @param position  the origin point of the spotlight
     * @param direction the direction in which the spotlight shines
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the constant attenuation factor (kC).
     *
     * @param kc the constant attenuation coefficient
     * @return this {@code SpotLight} instance (for method chaining)
     */
    @Override
    public SpotLight setkC(double kc) {
        super.setkC(kc);
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL).
     *
     * @param kl the linear attenuation coefficient
     * @return this {@code SpotLight} instance (for method chaining)
     */
    @Override
    public SpotLight setKl(double kl) {
        super.setKl(kl);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ).
     *
     * @param kq the quadratic attenuation coefficient
     * @return this {@code SpotLight} instance (for method chaining)
     */
    @Override
    public SpotLight setKq(double kq) {
        super.setKq(kq);
        return this;
    }

    /**
     * Computes the light intensity at a given point in the scene.
     * <p>
     * The intensity is attenuated based on distance (via {@link PointLight#getIntensity(Point)}),
     * and scaled by the cosine of the angle between the spotlight direction and the vector to the point.
     * If the angle is greater than 90°, the point is outside the light cone and returns {@code Color.BLACK}.
     *
     * @param p the point at which to calculate the spotlight's effect
     * @return the scaled {@link Color} intensity at point {@code p}, or {@code Color.BLACK} if the point is outside the cone
     */
    @Override
    public Color getIntensity(Point p) {
        Vector vecLtwo = getL(p);
        if (vecLtwo == null) {
            vecLtwo = direction;
        }
        double angleFromDircToPoint = alignZero(direction.dotProduct(vecLtwo.normalize()));
        if (angleFromDircToPoint <= 0) {
            return Color.BLACK; // light is not directed toward the point
        }
        return super.getIntensity(p).scale(angleFromDircToPoint);
    }
}
