package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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

    /**
     * Returns a point on the plane.
     *
     * @return a point on the plane
     */
    public Point getQ() {
        return q;
    }

    /**
     * Constructs a plane defined by three points.
     * The normal vector is calculated as the normalized cross product of the vectors
     * from the first point to the second and third points.
     * Throws an exception if the points are collinear or coincide.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     * @throws IllegalArgumentException if the points are collinear or not distinct
     */
    public Plane(Point point1, Point point2, Point point3) {
        this.q = point1;
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);
        Vector v1n = v1.normalize();
        Vector v2n = v2.normalize();
        if (v1.equals(v2)) {
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
     * Returns the normal vector to the plane at a given point.
     *
     * @param point a point on the plane (unused)
     * @return the normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }

    /**
     * Returns the normal vector to the plane.
     *
     * @return the normal vector
     */
    public Vector getNormal() {
        return this.normal;
    }

    /**
     * Finds the intersection points between a ray and the plane.
     *
     * @param ray the ray to intersect with the plane
     * @return a list containing the intersection point, or null if there is no intersection
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        Vector n = this.normal;

        double nv = n.dotProduct(dir);
        if (Util.isZero(nv)) {
            // Ray is parallel to the plane
            return null;
        }

        double numerator = n.dotProduct(q.subtract(p0));
        if (Util.isZero(numerator)) {
            // Ray starts on the plane → no intersection according to test logic
            return null;
        }

        double t = Util.alignZero(numerator / nv);
        if (t <= 0) {
            return null;
        }
        Point intersectionPoint = ray.getPoint(t);
        return List.of(new Intersection(this, intersectionPoint));
    }

}
