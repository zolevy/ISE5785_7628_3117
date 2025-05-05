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

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test correct plane creation with three non-collinear points
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

        // TC02: Two points coincide
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3));
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1));
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2));
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1));

        // TC06: Collinear points
        assertThrows(IllegalArgumentException.class, () -> new Plane(
                new Point(0.0, 0.0, 0.0),
                new Point(2.0, 2.0, 2.0),
                new Point(4.0, 4.0, 4.0)
        ));
    }

    @Test
    void testGetNormal() {
        // TC01: Normal is orthogonal to the plane
        Point p1 = new Point(0.0, 0.0, 1.0);
        Point p2 = new Point(3.0, 4.0, 5.0);
        Point p3 = new Point(6.0, 7.0, 8.0);
        Plane plane = new Plane(p1, p2, p3);

        Vector result = plane.getNormal(p1);

        assertEquals(1.0, result.length(), DELTA, "Plane's normal is not a unit vector");

        Vector vector1 = p2.subtract(p1);
        Vector vector2 = p3.subtract(p1);
        Vector vector3 = p3.subtract(p2);

        assertEquals(0.0, result.dotProduct(vector1), DELTA, "Normal is not orthogonal to vector1");
        assertEquals(0.0, result.dotProduct(vector2), DELTA, "Normal is not orthogonal to vector2");
        assertEquals(0.0, result.dotProduct(vector3), DELTA, "Normal is not orthogonal to vector3");
    }

    private final Point p001 = new Point(0.0, 0.0, 1.0);
    private final Point p100 = new Point(1.0, 0.0, 0.0);
    private final Point p010 = new Point(0.0, 1.0, 0.0);

    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(p001, p100, p010);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Point rayOrigin = new Point(-1.0, -1.0, -1.0);
        Vector rayDirection = new Vector(1.0, 1.0, 1.0);
        Ray ray = new Ray(rayOrigin, rayDirection);
        Point expectedIntersection = new Point(1.0 / 3, 1.0 / 3, 1.0 / 3);  // Corrected value
        var actualResult = plane.findIntersections(ray);
        assertNotNull(actualResult, "Ray should intersect the plane");
        assertEquals(1, actualResult.size(), "Expected exactly one intersection point");
        assertEquals(List.of(expectedIntersection), actualResult, "Ray intersects the plane at wrong point");

        // TC02: Ray starts before and goes away from plane
        Point p111 = new Point(1.0, 1.0, 1.0);
        Vector vm1m2m3 = new Vector(-1.0, -2.0, -3.0);
        assertNull(plane.findIntersections(new Ray(p111, vm1m2m3)));

        // =============== Boundary Values Tests ==================

        // TC11: Ray is parallel and outside the plane
        Vector v1m10 = new Vector(1.0, -1.0, 0.0);
        assertNull(plane.findIntersections(new Ray(p111, v1m10)));

        // TC12: Ray is parallel and lies in the plane
        assertNull(plane.findIntersections(new Ray(p010, v1m10)));

        // TC21: Ray is orthogonal to plane and before plane
        Vector vm1m1m1 = new Vector(-1.0, -1.0, -1.0);
        Point p000 = new Point(0.0, 0.0, 0.0);
        assertNull(plane.findIntersections(new Ray(p000, vm1m1m1)));

        // TC22: Ray is orthogonal and starts in plane
        Point p05050 = new Point(0.5, 0.5, 0.0);
        assertNull(plane.findIntersections(new Ray(p05050, vm1m1m1)));

        // TC23: Ray is orthogonal and starts after the plane
        Point interPoint23exp = new Point(1.0 / 3, 1.0 / 3, 1.0 / 3);
        var result23actual = plane.findIntersections(new Ray(p05050, vm1m1m1));
        assertNotNull(result23actual);
        assertEquals(1, result23actual.size());
        assertEquals(List.of(interPoint23exp), result23actual);

        // TC31: Ray starts at plane but not orthogonal or parallel
        Vector v123 = new Vector(1.0, 2.0, 3.0);
        assertNull(plane.findIntersections(new Ray(p05050, v123)));

        // TC41: Ray starts at reference point on the plane
        assertNull(plane.findIntersections(new Ray(p010, v123)));
    }
}
