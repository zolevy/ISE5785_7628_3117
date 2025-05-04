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

    /**
     * A point used in some tests
     */
    private final Point p001 = new Point(0.0, 0.0, 1.0);
    /**
     * A point used in some tests
     */
    private final Point p100 = new Point(1.0, 0.0, 0.0);
    /**
     * A vector used in some tests
     */
    private final Vector v001 = new Vector(0.0, 0.0, 1.0);

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     * This test verifies the behavior of the findIntersections method for the Sphere class,
     * testing various cases such as ray outside the sphere, inside the sphere, and passing through the center.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1.0, new Point(1.0, 1.0, 0.0));
        Ray ray;

        // TC01: Ray's line is outside the sphere (0 points)
        ray = new Ray(new Point(-1.0, -1.0, 0.0), new Vector(0.0, -1.0, 0.0));
        assertNull(sphere.findIntersections(ray), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        ray = new Ray(new Point(-1.0, 1.0, 0.0), new Vector(3.0, 0.0, 0.0));
        var result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(
                List.of(
                        new Point(0.0, 1.0, 0.0),
                        new Point(2.0, 1.0, 0.0)
                ),
                result,
                "Ray crosses sphere"
        );

        // TC03: Ray starts inside the sphere (1 point)
        ray = new Ray(new Point(1.0, 1.5, 0.0), new Vector(0.0, 1.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(
                List.of(new Point(1.0, 2.0, 0.0)),
                result,
                "Ray from inside hits one point"
        );

        // TC04: Ray starts after the sphere (0 points)
        ray = new Ray(new Point(3.0, 1.0, 0.0), new Vector(1.0, 1.0, 0.0));
        assertNull(sphere.findIntersections(ray), "Ray starts after sphere");

        // TC11: Ray starts at sphere and goes inside (1 point)
        ray = new Ray(new Point(2.0, 1.0, 0.0), new Vector(-1.0, 0.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                List.of(new Point(0.0, 1.0, 0.0)),
                result,
                "Ray from surface inward hits one point"
        );

        // TC12: Ray starts at sphere and goes outside (0 points)
        ray = new Ray(new Point(2.0, 1.0, 0.0), new Vector(1.0, 0.0, 0.0));
        assertNull(sphere.findIntersections(ray), "Ray out of sphere");

        // TC21: Ray through center, before sphere (2 points)
        ray = new Ray(new Point(-1.0, 1.0, 0.0), new Vector(1.0, 0.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(
                List.of(new Point(0.0, 1.0, 0.0), new Point(2.0, 1.0, 0.0)),
                result,
                "Ray through center (2 points)"
        );

        // TC22: Ray through center, from surface inward (1 point)
        ray = new Ray(new Point(2.0, 1.0, 0.0), new Vector(-1.0, 0.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(new Point(0.0, 1.0, 0.0)), result);

        // TC23: Ray starts inside (1 point)
        ray = new Ray(new Point(1.5, 1.0, 0.0), new Vector(1.0, 0.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(new Point(2.0, 1.0, 0.0)), result);

        // TC24: Ray starts at center (1 point)
        ray = new Ray(new Point(1.0, 1.0, 0.0), new Vector(1.0, 0.0, 0.0));
        result = sphere.findIntersections(ray);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(new Point(2.0, 1.0, 0.0)), result);

        // TC25: Ray starts at surface and goes outside (0 points)
        ray = new Ray(new Point(2.0, 1.0, 0.0), new Vector(1.0, 0.0, 0.0));
        assertNull(sphere.findIntersections(ray), "Ray goes out");
    }

}
