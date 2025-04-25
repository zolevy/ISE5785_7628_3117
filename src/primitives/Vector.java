package primitives;

/**
 * Represents a three-dimensional vector in 3D space, extending the Point class.
 * A vector is defined by its x, y, and z coordinates and cannot be the zero vector.
 */
public class Vector extends Point {

    /**
     * Constructs a vector with the specified x, y, and z coordinates.
     * Throws an exception if the vector is the zero vector (0,0,0).
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     */
    public Vector(Double x, Double y, Double z) {
        super(x, y, z);
        if (x == 0 && y == 0 && z == 0) {
            throw new IllegalArgumentException("Vector cannot be created from Point(0,0,0)");
        }
    }

    /**
     * Constructs a vector from a Double3 object.
     * Throws an exception if the vector is the zero vector (0,0,0).
     *
     * @param values the Double3 object representing the vector's coordinates
     */
    public Vector(Double3 values) {
        super(values);
        if (values.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be created from Point(0,0,0)");
        }
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) && this.xyz.equals(other.xyz);
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string representing the vector
     */
    @Override
    public String toString() {
        return String.format(xyz.toString());
    }

    /**
     * Computes the squared length of the vector.
     *
     * @return the squared length of the vector
     */
    public Double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public Double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Adds another vector to this vector and returns the resulting vector.
     *
     * @param vector the vector to add
     * @return the resulting vector
     */
    public Vector add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a scalar value and returns the resulting vector.
     *
     * @param scalar the scalar to multiply the vector by
     * @return the scaled vector
     */
    public Vector scale(Double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the dot product of the two vectors
     */
    public Double dotProduct(Vector vector) {
        return this.xyz.d1() * vector.xyz.d1() +
                this.xyz.d2() * vector.xyz.d2() +
                this.xyz.d3() * vector.xyz.d3();
    }

    /**
     * Computes the cross product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the cross product of the two vectors
     */
    public Vector crossProduct(Vector vector) {
        if (this.equals(vector))
        {
            throw new IllegalArgumentException("crossProduct resulted in zero vector");
        }
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Normalizes this vector, returning a unit vector in the same direction.
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        return this.scale(1 / this.length());
    }
}
