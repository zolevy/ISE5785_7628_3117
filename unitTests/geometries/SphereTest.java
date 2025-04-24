package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains tests for the Sphere class, specifically for checking the
 * behavior of the getNormal method to ensure it calculates the correct normal
 * vectors for points on the surface of the sphere.
 */
class SphereTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals.
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     * This test verifies that the normal vector of the sphere is calculated correctly
     * at different points on the surface of the sphere. It ensures that the normal
     * is a unit vector and points correctly from the center to the point on the surface.
     */
    @org.junit.jupiter.api.Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test normal calculation for valid points on the sphere surface
        Point center = new Point(0.0, 0.0, 0.0);
        double radius = 5.0;
        Sphere sphere = new Sphere(radius, center); // Create sphere

        // Points on the surface of the sphere
        Point point1 = new Point(5.0, 0.0, 0.0); // Point on the x-axis
        Point point2 = new Point(0.0, 5.0, 0.0); // Point on the y-axis
        Point point3 = new Point(0.0, 0.0, 5.0); // Point on the z-axis

        // Generate the test results
        Vector result1 = sphere.getNormal(point1);
        Vector result2 = sphere.getNormal(point2);
        Vector result3 = sphere.getNormal(point3);

        // Ensure |result| = 1 (normal is a unit vector)
        assertEquals(1.0, result1.length(), DELTA, "Sphere's normal at point1 is not a unit vector");
        assertEquals(1.0, result2.length(), DELTA, "Sphere's normal at point2 is not a unit vector");
        assertEquals(1.0, result3.length(), DELTA, "Sphere's normal at point3 is not a unit vector");

        // Ensure the result is pointing from the center of the sphere to the point
        assertEquals(point1.subtract(center).normalize(), result1, "Normal at axis-x is incorrect");
        assertEquals(point2.subtract(center).normalize(), result2, "Normal at axis-y is incorrect");
        assertEquals(point3.subtract(center).normalize(), result3, "Normal at axis-z is incorrect");
    }

    /** A point used in some tests */
    private final Point p001 = new Point(0.0, 0.0, 1.0);
    /** A point used in some tests */
    private final Point p100 = new Point(1.0, 0.0, 0.0);
    /** A vector used in some tests */
    private final Vector v001 = new Vector(0.0, 0.0, 1.0);
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, p100);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0.0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0.0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3.0, 1.0, 0.0);
        final Vector v110 = new Vector(1.0, 1.0, 0.0);
        final Point pm100 = new Point(-1.0, 0.0, 0.0);
        final Point pInsideSphere = new Point(1.0, 0.5, 0.0);
        final Point pOutsideSphere = new Point(3.0, 1.0, 0.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pm100, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(pm100, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final Point interPoint3 = new Point(1.6851646544245033, 0.7283882181415011, 0.0);
        final var listOfInterPoint3  = List.of(interPoint3);
        final var resultTest3 = sphere.findIntersections(new Ray(pInsideSphere, v310));

        assertNotNull(resultTest3, "Can't be empty list");
        assertEquals(1, resultTest3.size(), "Wrong number of points");
        assertEquals(listOfInterPoint3, resultTest3, "Ray crosses sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pOutsideSphere, v110)), "Ray's line out of sphere");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        final Point p110 = new Point(1.0, 1.0, 0.0);
        final var listOfInterPoint11  = List.of(p110);

        final Point p200 = new Point(2.0, 0.0, 0.0);
        final Vector vm110 = new Vector(-1.0, 1.0, 0.0);
        final var resultTest11 = sphere.findIntersections(new Ray(p200, vm110));

        assertNotNull(resultTest11, "Can't be empty list");
        assertEquals(1, resultTest11.size(), "Wrong number of points");
        assertEquals(listOfInterPoint11, resultTest11, "Ray crosses sphere");

        // TC12: Ray starts at sphere and goes outside (0 points)
        final Vector v1m10 = new Vector(1.0, -1.0, 0.0);
        assertNull(sphere.findIntersections(new Ray(p200, v1m10)), "Ray's line out of sphere");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        final Vector v200 = new Vector(2.0, 0.0, 0.0);
        final var result21actual = sphere.findIntersections(new Ray(pm100, v200));
        final Point p000 = new Point(0.0, 0.0, 0.0);
        final var result21exp = List.of(p200, p000);

        assertNotNull(result21actual, "Can't be empty list");
        assertEquals(2, result21actual.size(), "Wrong number of points");
        assertEquals(result21exp, result21actual, "Ray crosses sphere through center");

        // TC22: Ray starts at sphere and goes inside (1 points)
        final var result22actual = sphere.findIntersections(new Ray(p000, v200));
        final var result22exp = List.of(p200);

        assertNotNull(result22actual, "Can't be empty list");
        assertEquals(1, result22actual.size(), "Wrong number of points");
        assertEquals(result22exp, result22actual, "Ray crosses sphere through center");

        // TC23: Ray starts inside (1 points)
        final Point p1500 = new Point(1.5, 0.0, 0.0);
        final var result23actual = sphere.findIntersections(new Ray(p1500, v200));
        final var result23exp = List.of(p200);

        assertNotNull(result23actual, "Can't be empty list");
        assertEquals(1, result23actual.size(), "Wrong number of points");
        assertEquals(result23exp, result23actual, "Ray crosses sphere through center");

        // TC24: Ray starts at the center (1 points)
        final var result24actual = sphere.findIntersections(new Ray(p100, v200));
        final var result24exp = List.of(p200);

        assertNotNull(result24actual, "Can't be empty list");
        assertEquals(1, result24actual.size(), "Wrong number of points");
        assertEquals(result24exp, result24actual, "Ray crosses sphere through center");

        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p200, v200)), "Ray's line out of sphere");

        // TC26: Ray starts after sphere (0 points)
        final Point p2500 = new Point(2.5, 0.0, 0.0);
        assertNull(sphere.findIntersections(new Ray(p2500, v200)), "Ray's line out of sphere");

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        //the tangent point is (0, 0, 0)
        final Vector v010 = new Vector(0.0, 1.0, 0.0); //tanv010 = v010
        final Point beforeTanPoint0m10 = new Point(0.0, -1.0, 0.0);
        assertNull(sphere.findIntersections(new Ray(beforeTanPoint0m10, v010)), "Ray's line out of sphere");

        // TC32: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p000, v010)), "Ray's line out of sphere");

        // TC33: Ray starts after the tangent point
        final Point afterTanPoint020 = new Point(0.0, 2.0, 0.0);
        assertNull(sphere.findIntersections(new Ray(afterTanPoint020, v010)), "Ray's line out of sphere");

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(p2500, v010)), "Ray's line out of sphere");

        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(p1500, v010)), "Ray's line out of sphere");

    }
}