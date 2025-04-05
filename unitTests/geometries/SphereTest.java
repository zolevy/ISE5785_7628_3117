package geometries;

import primitives.Point;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @org.junit.jupiter.api.Test
    @Test
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

        // Ensure no exceptions when getting the normal
        assertDoesNotThrow(() -> sphere.getNormal(point1), "Exception thrown while getting normal for p1");
        assertDoesNotThrow(() -> sphere.getNormal(point2), "Exception thrown while getting normal for p2");
        assertDoesNotThrow(() -> sphere.getNormal(point3), "Exception thrown while getting normal for p3");

        // Generate the test results
        Vector result1 = sphere.getNormal(point1);
        Vector result2 = sphere.getNormal(point2);
        Vector result3 = sphere.getNormal(point3);

        // Ensure |result| = 1 (normal is a unit vector)
        assertEquals(1.0, result1.length(), DELTA, "Sphere's normal at point1 is not a unit vector");
        assertEquals(1.0, result2.length(), DELTA, "Sphere's normal at point2 is not a unit vector");
        assertEquals(1.0, result3.length(), DELTA, "Sphere's normal at point3 is not a unit vector");

        // Ensure the result is pointing from the center of the sphere to the point
        assertEquals(point1.subtract(center).normalize(), result1, "Normal at point1 is incorrect");
        assertEquals(point2.subtract(center).normalize(), result2, "Normal at point2 is incorrect");
        assertEquals(point3.subtract(center).normalize(), result3, "Normal at point3 is incorrect");
    }

}