package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Polygon class represents a two-dimensional polygon in 3D Cartesian coordinate system.
 * A polygon is defined by a list of ordered vertices lying in the same plane,
 * and must be convex.
 *
 * Based on original implementation by Dan.
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices.
     */
    protected final List<Point> vertices;

    /**
     * Associated plane in which the polygon lies.
     */
    protected final Plane plane;

    /**
     * The number of vertices in the polygon.
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of vertices:
     * <ul>
     *     <li>Less than 3 vertices</li>
     *     <li>Consecutive vertices are at the same point</li>
     *     <li>The vertices are not in the same plane</li>
     *     <li>The order of vertices is not according to edge path</li>
     *     <li>Three consecutive vertices lie in the same line</li>
     *     <li>The polygon is concave (not convex)</li>
     * </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return;
        Vector n = plane.getNormal(vertices[0]);
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * Returns the normal vector to the surface of the polygon at a given point.
     * The normal is inherited from the plane in which the polygon lies.
     *
     * @param point the point on the polygon surface (not used in this implementation)
     * @return the normal vector to the polygon
     */
    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    /**
     * Finds the intersection points between a ray and the polygon.
     * Currently not implemented.
     *
     * @param ray the ray to intersect with the polygon
     * @return null (not implemented)
     */
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
