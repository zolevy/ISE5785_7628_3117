package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTests {
    private static final double DELTA = 0.000001;

    // Test for Vector constructor
    @Test
    void testConstructor() {
        // Test with non-zero vector
        Vector v = new Vector(1.0, 2.0, 3.0);
        assertNotNull(v, "Vector should be created successfully for non-zero values");

        // Test with zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0.0, 0.0, 0.0),
                "Vector cannot be created with zero values");
    }

    // Test for lengthSquared method
    @Test
    void testLengthSquared() {
        Vector v = new Vector(3.0, 4.0, 0.0);
        assertEquals(25.0, v.lengthSquared(), DELTA, "Length squared of vector is incorrect");
    }

    // Test for length method
    @Test
    void testLength() {
        Vector v = new Vector(3.0, 4.0, 0.0);
        assertEquals(5.0, v.length(), DELTA, "Length of vector is incorrect");
    }

    // Test for add method
    @Test
    void testAdd() {
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        Vector result = v1.add(v2);
        assertEquals(new Vector(5.0, 7.0, 9.0), result, "Vector addition is incorrect");
    }

    // Test for scale method
    @Test
    void testScale() {
        Vector v = new Vector(1.0, 2.0, 3.0);
        Vector result = v.scale(2.0);
        assertEquals(new Vector(2.0, 4.0, 6.0), result, "Vector scaling is incorrect");
    }

    // Test for dotProduct method
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        assertEquals(32.0, v1.dotProduct(v2), DELTA, "Dot product calculation is incorrect");

        // =============== Boundary Values Tests ==================
        // TC01: Dot product of two parallel vectors
        Vector vParallel = new Vector(2.0, 4.0, 6.0);
        assertEquals(32.0, v1.dotProduct(vParallel), DELTA, "Dot product of parallel vectors is incorrect");

        // TC02: Dot product of a vector with itself
        assertEquals(v1.lengthSquared(), v1.dotProduct(v1), DELTA, "Dot product of vector with itself is incorrect");

        // TC03: Dot product of orthogonal vectors (should be zero)
        Vector vOrthogonal = new Vector(-2.0, 1.0, 0.0);
        assertEquals(0, v1.dotProduct(vOrthogonal), DELTA, "Dot product of orthogonal vectors is incorrect");
    }

    // Test for crossProduct method
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        Vector result = v1.crossProduct(v2);
        assertEquals(new Vector(-3.0, 6.0, -3.0), result, "Cross product calculation is incorrect");

        // =============== Boundary Values Tests ==================
        // TC01: Cross product of two parallel vectors (result should be zero vector)
        Vector vParallel = new Vector(2.0, 4.0, 6.0);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(vParallel), "Cross product of parallel vectors should throw exception");

        // TC02: Cross product of orthogonal vectors (should give a vector perpendicular to both)
        Vector vOrthogonal = new Vector(1.0, 0.0, 0.0);
        Vector resultOrthogonal = v1.crossProduct(vOrthogonal);
        assertEquals(new Vector(0.0, -3.0, 2.0), resultOrthogonal, DELTA, "Cross product of orthogonal vectors is incorrect");

        // TC03: Cross product of a vector with itself (should result in zero vector)
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1), "Cross product of a vector with itself should throw exception");
    }

    // Test for normalize method
    @Test
    void testNormalize() {
        Vector v = new Vector(3.0, 4.0, 0.0);
        Vector normalized = v.normalize();
        assertEquals(new Vector(0.6, 0.8, 0.0), normalized, DELTA, "Normalization of vector is incorrect");
    }

    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(5.0, 6.0, 7.0);
        Vector v2 = new Vector(2.0, 3.0, 4.0);
        Vector result = v1.subtract(v2);
        assertEquals(new Vector(3.0, 3.0, 3.0), result, DELTA, "Vector subtraction is incorrect");

        // =============== Boundary Values Tests ==================
        // TC01: Test with a vector and the zero vector (expecting the same vector as result)
        Vector vZero = new Vector(0.0, 0.0, 0.0);
        result = v1.subtract(vZero);
        assertEquals(v1, result, DELTA, "Subtraction with zero vector failed");

        // TC02: Test subtraction of a vector from itself (should result in the zero vector)
        result = v1.subtract(v1);
        assertEquals(vZero, result, DELTA, "Subtraction of vector from itself failed");
    }

}
