package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Cylinder class, specifically the getNormal method.
 * It includes tests for various points on the cylinder's surface and bases.
 */
class CylinderTest {
    private static final double DELTA = 0.00001; // Tolerance for double comparisons

    /**
     * Tests the getNormal method of the Cylinder class.
     * This test covers different cases for points on the lateral surface, the bottom base, and the top base.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Ray axisRay = new Ray(new Point(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 1.0)); // Cylinder along Z-axis
        double radius = 2.0;
        double height = 4.0;
        Cylinder cylinder = new Cylinder(radius, axisRay, height);

        // TC01: Point on the curved surface
        Point p1 = new Point(2.0, 0.0, 2.0); // A point on the lateral surface
        Vector normal1 = cylinder.getNormal(p1);
        assertEquals(new Vector(1.0, 0.0, 0.0), normal1, "Cylinder's normal on the lateral surface is incorrect");

        // TC02: Point on the bottom base (excluding center)
        Point p2 = new Point(1.0, 1.0, 0.0);
        Vector normal2 = cylinder.getNormal(p2);
        assertEquals(new Vector(0.0, 0.0, -1.0), normal2, "Cylinder's normal on the bottom base is incorrect");

        // TC03: Point on the top base (excluding center)
        Point p3 = new Point(-1.0, -1.0, 4.0);
        Vector normal3 = cylinder.getNormal(p3);
        assertEquals(new Vector(0.0, 0.0, 1.0), normal3, "Cylinder's normal on the top base is incorrect");

        // =============== Boundary Tests ================
        // TC04: Center of bottom base
        Point p4 = new Point(0.0, 0.0, 0.0);
        Vector normal4 = cylinder.getNormal(p4);
        assertEquals(new Vector(0.0, 0.0, -1.0), normal4, "Cylinder's normal at the bottom base center is incorrect");

        // TC05: Center of top base
        Point p5 = new Point(0.0, 0.0, 4.0);
        Vector normal5 = cylinder.getNormal(p5);
        assertEquals(new Vector(0.0, 0.0, 1.0), normal5, "Cylinder's normal at the top base center is incorrect");

        // TC06: Edge between lateral surface and bottom base
        Point p6 = new Point(2.0, 0.0, 0.0);
        Vector normal6 = cylinder.getNormal(p6);
        assertTrue(normal6.equals(new Vector(1.0, 0.0, 0.0)) || normal6.equals(new Vector(0.0, 0.0, -1.0)),
                "Cylinder's normal at the bottom edge is incorrect");

        // TC07: Edge between lateral surface and top base
        Point p7 = new Point(2.0, 0.0, 4.0);
        Vector normal7 = cylinder.getNormal(p7);
        assertTrue(normal7.equals(new Vector(1.0, 0.0, 0.0)) || normal7.equals(new Vector(0.0, 0.0, 1.0)),
                "Cylinder's normal at the top edge is incorrect");
    }
}