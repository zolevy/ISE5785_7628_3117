package geometries;

import primitives.*;

import java.util.List;

/**
 * Represents an infinite tube in 3D space, defined by a central axis (a ray) and a radius.
 * A tube is like a cylinder without top and bottom caps — it extends infinitely in both directions.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with a specified radius and central axis.
     *
     * @param radius the radius of the tube
     * @param axis   the central axis of the tube
     */
    public Tube(Double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Computes the normal vector to the surface of the tube at a given point.
     * The normal is perpendicular to the axis and points outward from the surface.
     *
     * @param givenPoint a point on the surface of the tube
     * @return the normalized normal vector at the specified point
     */
    @Override
    public Vector getNormal(Point givenPoint) {
        Point point0 = axis.getHead();
        Vector vector = axis.getDirection().normalize();

        // Calculate vector from axis base to the given point
        Vector u = givenPoint.subtract(point0);

        // Project u onto the axis direction to find closest point on axis
        double t = vector.dotProduct(u);
        Point center = point0.add(vector.scale(t));

        // Compute the normal as the vector from the axis to the given point
        return givenPoint.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of a ray with the tube.
     *
     * @param ray the ray to intersect with the tube
     * @return a list of intersection points or null if there are no intersections
     */
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
