package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a plane in 3D space, defined by either three points or a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    private final Point q;

    /**
     * The normal vector to the plane.
     */
    private final Vector normal;

    /**
     * Constructs a plane defined by three points.
     *
     * @param q1 the first point on the plane
     * @param q2 the second point on the plane
     * @param q3 the third point on the plane
     */
    public Plane(Point q1, Point q2, Point q3) {
        this.q = q1;
        this.normal = null; // Normal calculation should be implemented here
    }

    /**
     * Constructs a plane defined by a point and a normal vector.
     *
     * @param q a point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane. The parameter is unused.
     *
     * @param point a point on the plane (not used in this implementation)
     * @return the normal vector to the plane
     */
    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }
}
