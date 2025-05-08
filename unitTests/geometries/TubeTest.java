package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests for the {@link geometries.Tube#getNormal(primitives.Point)} method
 * in the Tube class. It verifies the calculation of the normal vector at different points on the tube's surface.
 */
class TubeTest {

    /**
     * Tolerance for double comparisons.
     */
    private static final double DELTA = 1e-10;

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     * This test verifies that the normal vector at a given point on the tube's surface is correctly calculated,
     * normalized (unit vector), and perpendicular to the tube's axis.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =============

        // TC01: Test normal calculation for a regular point on the tube surface
        Ray axisRay = new Ray(new Point(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 1.0)); // Tube along Z-axis
        double radius = 2.0;
        Tube tube = new Tube(radius, axisRay);

        Point point = new Point(2.0, 0.0, 5.0); // A point on the tube surface

        // Generate the test result
        Vector normal = tube.getNormal(point);

        // Ensure |normal| = 1 (normal is a unit vector)
        assertEquals(1.0, normal.length(), DELTA, "Tube's normal is not a unit vector");

        // Ensure the normal is perpendicular to the tube's axis
        assertEquals(new Vector(1.0, 0.0, 0.0), normal, "Tube's normal is incorrect");

        // =============== Boundary Tests ================

       /* // TC01: Test normal calculation for a point on the tube's axis
        Point axisPoint = new Point(0.0, 0.0, 5.0); // A point on the tube's axis
        // Generate the test result for the axis point
        Vector axisNormal = tube.getNormal(axisPoint);
        // Ensure the normal is perpendicular to the tube's axis
        assertEquals(new Vector(0.0, 1.0, 0.0), axisNormal, "Tube's normal at axis point is incorrect");

        */

        // TC02: Test normal calculation for a point on the tube's surface
        Point surfacePoint = new Point(0.0, 2.0, 5.0); // A point on the tube's surface
        // Generate the test result for the surface point
        Vector surfaceNormal = tube.getNormal(surfacePoint);
        // Ensure the normal is perpendicular to the tube's axis
        assertEquals(new Vector(0.0, 1.0, 0.0), surfaceNormal, "Tube's normal at surface point is incorrect");


        /*
        // TC02: Test normal calculation for a point "in front" of the tube's axis
        Point boundaryPoint = new Point(0.0, 2.0, 0.0); // Directly in front of the tube's axis

        // Generate the test result for the boundary point
        Vector boundaryNormal = tube.getNormal(boundaryPoint);

        // Ensure the normal is pointing correctly
        assertEquals(new Vector(0.0, 1.0, 0.0), boundaryNormal, "Tube's normal at boundary point is incorrect");

         */
    }
}
