package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests for the Triangle class, specifically for testing
 * the {@link geometries.Triangle#getNormal(primitives.Point)} method to ensure
 * that the normal vector is correctly calculated and orthogonal to all edges of the triangle.
 */
class TriangleTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals.
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     * This test verifies that the normal vector of the triangle is calculated correctly,
     * is normalized, and is orthogonal to all edges of the triangle.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test if the normal that was generated is normalized and orthogonal to all the edges

        Point p1 = new Point(0.0, 0.0, 1.0);
        Point p2 = new Point(3.0, 4.0, 5.0);
        Point p3 = new Point(6.0, 7.0, 8.0);
        Triangle triangle = new Triangle(p1, p2, p3);

        // Generate the test result
        Vector result = triangle.getNormal(p1);

        // Ensure |result| = 1 (normal is a unit vector)
        assertEquals(1.0, result.length(), DELTA, "Triangle's normal is not a unit vector");

        // Ensure the result is orthogonal to all edges
        Vector edge1 = p2.subtract(p1); // Vector between p1 and p2
        Vector edge2 = p3.subtract(p1); // Vector between p1 and p3
        Vector edge3 = p3.subtract(p2); // Vector between p2 and p3

        // Check orthogonality with edge1
        assertEquals(0.0, result.dotProduct(edge1), DELTA, "Normal is not orthogonal to all edges");

        // Check orthogonality with edge2
        assertEquals(0.0, result.dotProduct(edge2), DELTA, "Normal is not orthogonal to all edges");

        // Check orthogonality with edge3
        assertEquals(0.0, result.dotProduct(edge3), DELTA, "Normal is not orthogonal to all edges");
    }
}