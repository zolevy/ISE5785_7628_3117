package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TubeTest {
    private static final double DELTA = 1e-10; // Tolerance for double comparisons

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test normal calculation for a regular point on the tube surface
        Ray axisRay = new Ray(new Point(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 1.0)); // Tube along Z-axis
        double radius = 2.0;
        Tube tube = new Tube(radius, axisRay);

        Point point = new Point(2.0, 0.0, 5.0); // A point on the tube surface

        Vector normal = tube.getNormal(point);
        // Ensure |normal| = 1 (unit vector)
        assertEquals(1.0, normal.length(), DELTA, "Tube's normal is not a unit vector");
        // Ensure the normal is perpendicular to the tube's axis
        assertEquals(new Vector(1.0, 0.0, 0.0), normal, "Tube's normal is incorrect");

        // =============== Boundary Tests ================
        // TC02: Test normal calculation for a point "in front" of the tube's axis
        Point boundaryPoint = new Point(0.0, 2.0, 0.0); // Directly in front of the tube's axis
        Vector boundaryNormal = tube.getNormal(boundaryPoint);
        assertEquals(new Vector(0.0, 1.0, 0.0), boundaryNormal, "Tube's normal at boundary point is incorrect");
    }
}