package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Represents a triangle in 3D space, defined as a specific case of a polygon with three vertices.
 */
public class Triangle extends Polygon{
    public Triangle(Point... vertices) {
        super(vertices);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Get the ray origin and direction
        Point rayOrigin = ray.getHead();
        Vector rayDirection = ray.getDirection();

        // Define the edges of the triangle
        Point vertex1 = vertices.get(1);
        Point vertex0 = vertices.get(0);
        Point vertex2 = vertices.get(2);

        Vector edge1 = vertex1.subtract(vertex0);
        Vector edge2 = vertex2.subtract(vertex0);

        // Calculate the normal vector of the triangle's plane
        Vector normal = edge1.crossProduct(edge2);

        // Compute the dot product of the normal vector and the ray's direction
        double normalDotRayDirection = alignZero(normal.dotProduct(rayDirection));

        // If the ray is parallel to the plane (dot product is zero), no intersection occurs
        if (isZero(normalDotRayDirection)) {
            return null;
        }

        // Calculate the vector from the ray's origin to a point on the triangle (vertex0)
        Vector rayOriginToVertex0 = rayOrigin.subtract(vertex0);

        // Calculate the intersection parameter t using the formula
        double t = alignZero(normal.dotProduct(rayOriginToVertex0) / normalDotRayDirection);

        // If t <= 0, the intersection occurs behind the ray or at the ray's origin, no valid intersection
        if (t <= 0) {
            return null;
        }

        // Calculate the intersection point on the triangle's plane
        Point intersectionPoint = ray.getPoint(t);

        // Check if the intersection point is inside the triangle using barycentric coordinates
        Vector vectorToIntersection = intersectionPoint.subtract(vertex0);
        Vector vectorToVertex1 = intersectionPoint.subtract(vertex1);
        Vector vectorToVertex2 = intersectionPoint.subtract(vertex2);

        // Calculate the cross products to check if the point is inside the triangle
        Vector cross1 = edge1.crossProduct(vectorToIntersection);
        Vector cross2 = edge2.crossProduct(vectorToVertex1);
        Vector cross3 = normal.crossProduct(vectorToVertex2);

        // Check if all the cross products have the same sign (indicating the point is inside the triangle)
        double crossProduct1 = alignZero(normal.dotProduct(cross1));
        double crossProduct2 = alignZero(normal.dotProduct(cross2));
        double crossProduct3 = alignZero(normal.dotProduct(cross3));

        if ((isZero(crossProduct1) || crossProduct1 > 0) &&
                (isZero(crossProduct2) || crossProduct2 > 0) &&
                (isZero(crossProduct3) || crossProduct3 > 0)) {
            return List.of(intersectionPoint);
        }

        // If the point is outside the triangle, return null
        return null;
    }


}