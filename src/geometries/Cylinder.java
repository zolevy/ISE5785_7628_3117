package geometries;

import primitives.*;

/**
 * Represents a cylinder in 3D space, defined by a central axis and a height.
 * A cylinder is a special case of a tube with a finite height.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final Double height;
    private static final double DELTA = 0.00001; // Tolerance for double comparisons


    /**
     * Constructs a cylinder with a specified radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the central axis of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Double radius, Ray axis, Double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * This method is not implemented for Cylinder.
     *
     * @param point a point on the surface of the cylinder
     * @return always returns null
     */
    @Override
    public Vector getNormal (Point point) {
        Point basePoint = axis.getHead();
        Vector v = axis.getDirection().normalize();
        Vector u = point.subtract(basePoint); // Vector from axis base to P
        double t = v.dotProduct(u); // Projection scalar

        // Check if the point is on one of the bases
        if (Math.abs(t) < DELTA) { // Bottom base (t ≈ 0)
            return v.scale(-1.0); // Normal points downward
        }
        if (Math.abs(t - height) < DELTA) { // Top base (t ≈ height)
            return v; // Normal points upward
        }

        // Otherwise, it's on the lateral surface
        Point center = basePoint.add(v.scale(t)); // Closest point on the axis
                /*we could use Tube's func and do code reuse,
                but it will cause code reuse of computing unnecessary things in super's func */
        return point.subtract(center).normalize();
    }
}
