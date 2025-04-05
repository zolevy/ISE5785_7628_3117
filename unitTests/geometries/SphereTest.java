package geometries;

import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

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
}