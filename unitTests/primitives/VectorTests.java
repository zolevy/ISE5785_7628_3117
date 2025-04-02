package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTests {
    private static final double DELTA = 0.000001;
    /**
     * Test method for
     * {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v123 = new Vector(0.0,0.0,1.0);
        Vector v03M2 = new Vector(1.0 ,0.0, 0.0);
        Vector vr = v123.crossProduct(v03M2);
        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v123.length() * v03M2.length(), vr.length(), DELTA, "crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(v123), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(v03M2), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of parallel vectors
        Vector vM2M4M6 = new Vector(0.0,0.0,25.0);
        assertThrows(IllegalArgumentException.class, () -> v123.crossProduct(vM2M4M6), //
                "crossProduct() for parallel vectors does not throw an exception");
    }

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        Vector result = v1.add(v2);
        assertEquals(new Vector(5.0, 7.0, 9.0), result);

        // =============== Boundary Values Tests ==================

    }

    @Test
    void testLengthSquared(){
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        double result = v1.lengthSquared();
        assertEquals(14.0, result, DELTA);
    }

    @Test
    void testLength(){
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        double result = v1.length();
        assertEquals(Math.sqrt(14.0), result, DELTA);
    }

}