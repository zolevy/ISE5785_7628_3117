package geometries;

import primitives.*;
import java.util.List;
import static primitives.Util.*;

/**
 * Represents a triangle in 3D space, defined as a specific case of a polygon with three vertices.
 */
public class Triangle extends Polygon {
    /**
     * Constructs a triangle with the specified vertices.
     *
     * @param vertices the three vertices of the triangle
     */
    public Triangle(Point... vertices) {
        super(vertices);
    }

    /**
     * Finds the intersection points of a ray with the triangle.
     *
     * @param ray the ray to intersect with the triangle
     * @return a list containing the intersection point, or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        var intersections = plane.findIntersections(ray);
        if (intersections == null)
            return null;

        Point  p0 = ray.getHead();
        Vector v  = ray.getDirection();
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector n  = v1.crossProduct(v2).normalize();
        double s1 = alignZero(v.dotProduct(n));
        if (s1 == 0)
            return null;

        Vector v3 = vertices.get(2).subtract(p0);
        n = v2.crossProduct(v3).normalize();
        double s2 = alignZero(v.dotProduct(n));
        if (s1 * s2 <= 0)
            return null;

        n = v3.crossProduct(v1).normalize();
        double s3 = alignZero(v.dotProduct(n));
        if (s1 * s3 <= 0)
            return null;

        return List.of(intersections.get(0));
    }
}
