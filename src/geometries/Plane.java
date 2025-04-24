package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

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

    public Point getQ() {
        return q;
    }
    /**
     * Constructs a plane defined by three points.
     * <p>
     * The normal vector is calculated as the normalized cross product of the vectors
     * from the first point to the second and third points.
     * <p>
     * If the three points are collinear or coincide, the cross product will be zero,
     * and a {@link IllegalArgumentException} may be thrown when trying to normalize it.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     * @throws IllegalArgumentException if the points are collinear or not distinct
     */
    public Plane(Point point1, Point point2, Point point3) {
        //maybe check if it's possible to make a plane from these three points
        this.q = point1;
        //normal calculation
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);

        Vector v1n = v1.normalize();
        Vector v2n = v2.normalize();
        if (v1.equals(v2)){
            throw new IllegalArgumentException("The points are collinear or coincide");
        }
        this.normal = v1.crossProduct(v2).normalize();
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

    /**
     * Returns the normal vector to the plane.
     *
     * @return the normalized normal vector of the plane
     */
    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point rayOrigin = ray.getHead();
        Vector rayDirection = ray.getDirection();

        Vector normalToPlane = normal;
        Point q0 = getQ();
        double nv = alignZero(normalToPlane.dotProduct(rayDirection));

        // Ray is parallel to the plane or lies in it → no intersection
        if (isZero(nv)) return null;

        double numerator = alignZero(normalToPlane.dotProduct(q0.subtract(rayOrigin)));

        double t = alignZero(numerator / nv);

        // Intersection is at the ray origin or behind it → discard
        if (t <= 0) return null;

        Point intersectionPoint = ray.getPoint(t);
        return List.of(intersectionPoint);
    }

}
