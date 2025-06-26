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
    @Override
/**
 * Creates the axis-aligned bounding box (AABB) for the triangle.
 *
 * @return AABB enclosing the triangle.
 */
    public AABB createBoundingBox() {
        double minX = Math.min(Math.min(vertices.get(1).getX(), vertices.get(2).getX()), vertices.get(3).getX());
        double minY = Math.min(Math.min(vertices.get(1).getY(), vertices.get(2).getY()), vertices.get(3).getY());
        double minZ = Math.min(Math.min(vertices.get(1).getZ(), vertices.get(2).getZ()), vertices.get(3).getZ());

        double maxX = Math.max(Math.max(vertices.get(1).getX(), vertices.get(2).getX()), vertices.get(3).getX());
        double maxY = Math.max(Math.max(vertices.get(1).getY(), vertices.get(2).getY()), vertices.get(3).getY());
        double maxZ = Math.max(Math.max(vertices.get(1).getZ(), vertices.get(2).getZ()), vertices.get(3).getZ());

        Point min = new Point(minX, minY, minZ);
        Point max = new Point(maxX, maxY, maxZ);

        return new AABB(min, max);
    }

    /**
     * Finds the intersection points of a ray with the triangle.
     *
     * @param ray the ray to intersect with the triangle
     * @return a list containing the intersection point, or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        var intersections = plane.calculateIntersectionsHelper(ray);
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

        return List.of(new Intersection(this, intersections.get(0).point));
    }
}
