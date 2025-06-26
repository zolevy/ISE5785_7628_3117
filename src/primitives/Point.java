package primitives;

/**
 * Represents a point in 3D space.
 */
public class Point {

    /**
     * The coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * The zero point (0,0,0) in 3D space.
     */
    public static final Point ZERO = new Point(0.0, 0.0, 0.0);

    /**
     * Constructs a point with the specified x, y, and z coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point(Double x, Double y, Double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Returns the X coordinate of this point/vector.
     *
     * @return the X coordinate as a double
     */
    public double getX() {
        return xyz.d1();
    }

    /**
     * Returns the Y coordinate of this point/vector.
     *
     * @return the Y coordinate as a double
     */
    public double getY() {
        return xyz.d2();
    }

    /**
     * Returns the Z coordinate of this point/vector.
     *
     * @return the Z coordinate as a double
     */
    public double getZ() {
        return xyz.d3();
    }

    /**
     * Constructs a point from a Double3 object.
     *
     * @param point the Double3 object representing the coordinates
     */
    public Point(Double3 point) {
        this.xyz = point;
    }

    /**
     * Checks if this point is equal to another object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && this.xyz.equals(other.xyz);
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string representing the point
     */
    @Override
    public String toString() {
        return String.format(xyz.toString());
    }

    /**
     * Subtracts another point from this point and returns the resulting vector.
     *
     * @param point the point to subtract
     * @return the resulting vector
     */
    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * Adds a vector to this point and returns the new point.
     *
     * @param vector the vector to add
     * @return the resulting point
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    /**
     * Computes the Euclidean distance between this point and another point.
     *
     * @param point the other point
     * @return the distance between the two points
     */
    public Double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    /**
     * Computes the squared Euclidean distance between this point and another point.
     *
     * @param point the other point
     * @return the squared distance between the two points
     */
    public Double distanceSquared(Point point) {
        return ((point.xyz.d1() - this.xyz.d1()) * (point.xyz.d1() - this.xyz.d1()) +
                (point.xyz.d2() - this.xyz.d2()) * (point.xyz.d2() - this.xyz.d2()) +
                (point.xyz.d3() - this.xyz.d3()) * (point.xyz.d3() - this.xyz.d3()));
    }
}
