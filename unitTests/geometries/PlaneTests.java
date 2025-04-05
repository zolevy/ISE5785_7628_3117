package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Plane class, specifically the constructor and getNormal method.
 * It includes tests for creating a plane with non-collinear points and for ensuring that the normal is correct.
 */
class PlaneTests {

    private static final double DELTA = 0.00001; // Tolerance for double comparisons

    /**
     * Tests the constructor of the Plane class.
     * This test checks various edge cases, including coincident points and collinear points.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test correct plane creation with three non-collinear points
        // Choosing points such that the cross product of the vectors does not result in a vector of length 1
        Point p1 = new Point(1.0, 0.0, 0.0);
        Point p2 = new Point(0.0, 3.0, 0.0);
        Point p3 = new Point(0.0, 0.0, 4.0);
        Plane plane = new Plane(p1, p2, p3);

        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector v3 = p3.subtract(p2);
        Vector normal = v1.crossProduct(v2).normalize();

        assertEquals(1, plane.getNormal().length(), "Plane normal is not normalized");

        int perpendicularCount = 0;
        if (plane.getNormal().dotProduct(v1) == 0) perpendicularCount++;
        if (plane.getNormal().dotProduct(v2) == 0) perpendicularCount++;
        if (plane.getNormal().dotProduct(v3) == 0) perpendicularCount++;
        if (perpendicularCount < 2) {
            fail("Plane normal is not perpendicular to at least two different vectors");
        }

        // =============== Boundary Values Tests ==================

        // TC02: Two points coincide (p1 and p2)
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3), "Constructor did not throw an exception when two points coincide (p1, p1, p3)");

        // TC03: Two points coincide (p1 and p3)
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1), "Constructor did not throw an exception when two points coincide (p1, p2, p1)");

        // TC04: Two points coincide (p2 and p3)
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2), "Constructor did not throw an exception when two points coincide (p1, p2, p2)");

        // TC05: All points coincide
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1), "Constructor did not throw an exception when all points coincide (p1, p1, p1)");

        // TC06: All points are collinear
        assertThrows(IllegalArgumentException.class, () -> new Plane(
                new Point(0.0, 0.0, 0.0),
                new Point(2.0, 2.0, 2.0),
                new Point(4.0, 4.0, 4.0)
        ), "Constructor did not throw an exception when all points are collinear");
    }

    /**
     * Tests the getNormal method of the Plane class.
     * This test checks if the normal is correctly normalized and orthogonal to all vectors created from the points.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test if the normal that was generated is normalized and orthogonal to all the vectors
        // ensure there are no exceptions
        Point p1 = new Point(0.0, 0.0, 1.0);
        Point p2 = new Point(3.0, 4.0, 5.0);
        Point p3 = new Point(6.0, 7.0, 8.0);
        Plane plane = new Plane(p1, p2, p3);
        // Generate the test result
        Vector result = plane.getNormal(p1);
        // Ensure |result| = 1 (normal is a unit vector)
        assertEquals(1.0, result.length(), DELTA, "Plane's normal is not a unit vector");
        // Ensure the result is orthogonal to all vectors created from the points
        Vector vector1 = p2.subtract(p1); // Vector between p1 and p2
        Vector vector2 = p3.subtract(p1); // Vector between p1 and p3
        Vector vector3 = p3.subtract(p2); // Vector between p2 and p3
        // Check orthogonality with v1
        assertEquals(0.0, result.dotProduct(vector1), DELTA, "Normal is not orthogonal to all edges");
        // Check orthogonality with vector2
        assertEquals(0.0, result.dotProduct(vector2), DELTA, "Normal is not orthogonal to all edges");
        // Check orthogonality with v3
        assertEquals(0.0, result.dotProduct(vector3), DELTA, "Normal is not orthogonal to all edges");
    }
}