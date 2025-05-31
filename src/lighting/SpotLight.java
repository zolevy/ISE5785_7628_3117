package lighting;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * Represents a spotlight emitting light from a point in a specific direction.
 * The intensity decreases with distance and angle from the direction vector.
 */
public class SpotLight extends PointLight {
    private final Vector direction;

    /**
     * Constructs a SpotLight with intensity, position, and direction.
     *
     * @param intensity the color/intensity of the light
     * @param position  the position of the light source
     * @param direction the direction in which the spotlight is aimed
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public SpotLight setkC(double kc) {
        super.setkC(kc);
        return this;
    }

    @Override
    public SpotLight setKl(double kl) {
        super.setKl(kl);
        return this;
    }

    @Override
    public SpotLight setKq(double kq) {
        super.setKq(kq);
        return this;
    }

    /**
     * Calculates the light intensity at point p, factoring distance attenuation and angle to direction.
     *
     * @param p the point to calculate intensity for
     * @return the attenuated and angle-scaled color intensity at point p, or black if outside the spotlight cone
     */
    @Override
    public Color getIntensity(Point p) {
        Vector vecLtwo = getL(p);
        if (vecLtwo == null) {
            vecLtwo = direction;
        }
        double angleFromDircToPoint = alignZero(direction.dotProduct(vecLtwo.normalize()));
        if (angleFromDircToPoint <= 0) {
            return Color.BLACK; // light is not directed towards the point
        }
        return super.getIntensity(p).scale(angleFromDircToPoint);
    }
}
