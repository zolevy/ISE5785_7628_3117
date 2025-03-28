package geometries;

import primitives.*;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with a specified radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere(Double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Computes the normal vector to the sphere at a given point on its surface.
     *
     * @param q a point on the surface of the sphere
     * @return the normal vector at the specified point
     */
    public Vector getNormal(Point q) {
        return null;
    }
}
