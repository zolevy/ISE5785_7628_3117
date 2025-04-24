package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Plane plane = new Plane(p001, p100, p010);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the plane (1 points)
        Point p111 = new Point(1.0, 1.0, 1.0);
        Vector v123 = new Vector(1.0,2.0,3.0)         ;
        Point interPoint01exp = new Point(0.6666666666666667, 0.3333333333333334, 0.0);
        final var listOfInterPoint01  = List.of(interPoint01exp);
        final var result01actual = plane.findIntersections(new Ray(p111, v123));

        assertNotNull(result01actual, "Can't be empty list");
        assertEquals(1, result01actual.size(), "Wrong number of points");
        assertEquals(listOfInterPoint01, result01actual, "Ray crosses plane");

        // TC02: Ray starts before and crosses the plane (0 points)
        Vector vm1m2m3 = new Vector(-1.0,-2.0,-3.0)         ;
        assertNull(plane.findIntersections(new Ray(p111, vm1m2m3)), "Ray's line out of plane");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the plane (but not the center)

        // TC11: Ray is parallel to the plane is not included in the plane (0 points)
        Vector v1m10 = new Vector(1.0,-1.0,0.0);
        assertNull(plane.findIntersections(new Ray(p111, v1m10)), "Ray's line out of plane");

        // TC12: Ray is parallel to the plane the ray is included in the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p010, v1m10)), "Ray's line out of plane");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray is orthogonal to the plane according to 𝑃0 before the plane (0 points)
        Vector vm1m1m1 = new Vector(-1.0,-1.0,-1.0);
        Point p000 = new Point(0.0, 0.0, 0.0);
        assertNull(plane.findIntersections(new Ray(p000, vm1m1m1)), "Ray's line out of plane");

        // TC22: Ray is orthogonal to the plane according to 𝑃0e in the plane  (0 points)
        Point p05050 = new Point(0.5, 0.5, 0.0);
        assertNull(plane.findIntersections(new Ray(p05050, vm1m1m1)), "Ray's line out of plane");

        // TC23: Ray is orthogonal to the plane according to 𝑃0 after the plane (1 points)
        Point interPoint23exp = new Point(0.3333333333333334, 0.3333333333333334, 0.3333333333333334);
        final var listOfInterPoint23  = List.of(interPoint23exp);
        final var result23actual = plane.findIntersections(new Ray(p05050, vm1m1m1));

        assertNotNull(result23actual, "Can't be empty list");
        assertEquals(1, result23actual.size(), "Wrong number of points");
        assertEquals(listOfInterPoint23, result23actual, "Ray crosses plane");

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray is neither orthogonal nor parallel to and begins at
        //the plane (𝑃0 is in the plane, but not the ray)
        assertNull(plane.findIntersections(new Ray(p05050, v123)), "Ray's line out of plane");

        // **** Group 4: Ray's line is tangent to the sphere (all tests 0 points)
        // TC41: Ray is neither orthogonal nor parallel to the plane and
        //begins in the same point which appears as reference
        //point in the plane (Q)
        assertNull(plane.findIntersections(new Ray(p010, v123)), "Ray's line out of plane");

    }
}