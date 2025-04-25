package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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


    /** A point used in some tests */
    private final Point p001 = new Point(0.0, 0.0, 1.0);
    /** A point used in some tests */
    private final Point p100 = new Point(1.0, 0.0, 0.0);
    /** A vector used in some tests */
    private final Point p010 = new Point(0.0, 1.0, 0.0);
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Triangle triangle = new Triangle(p001, p100, p010);
        Vector vm1m2m3 = new Vector(-1.0,-2.0,-3.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Inside triangle (1 points)
        Point p111 = new Point(1.0, 1.0, 1.0);
        Point pStart = new Point(2.5, 4.25, 6.25);
        Point interPointExp = new Point(0.5, 0.25, 0.25);
        final var listOfInterPoint01 = List.of(interPointExp);
        final var result01actual = triangle.findIntersections(new Ray(pStart, vm1m2m3));


        assertNotNull(result01actual, "Can't be empty list");
        assertEquals(1, result01actual.size(), "Wrong number of points");
        assertEquals(listOfInterPoint01, result01actual, "Ray crosses triangle");

        // TC02: Outside against edge (0 points)
        Point p220 = new Point(2.0,2.0,0.0);
        assertNull(triangle.findIntersections(new Ray(p220, vm1m2m3)), "Ray's line out of triangle");

        // TC03: Outside against edge (0 points)
        Point pm13m1 = new Point(-1.0,3.0,-1.0);
        assertNull(triangle.findIntersections(new Ray(pm13m1, vm1m2m3)), "Ray's line out of triangle");

        // =============== Boundary Values Tests ==================
        // TC11: On edge (0 points)
        Point p011 = new Point(0.0, 1.0, 1.0);
        assertNull(triangle.findIntersections(new Ray(p011, vm1m2m3)), "Ray's line out of triangle");

        // TC12: In vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(p111, vm1m2m3)), "Ray's line out of triangle");

        // TC21: On edge's continuation (0 points)
        Point p2m11 = new Point(2.0, -1.0, 1.0);
        assertNull(triangle.findIntersections(new Ray(p2m11, vm1m2m3)), "Ray's line out of triangle");

    }

}