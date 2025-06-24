package primitives;

/**
 * A simple class representing a 2D point with x and y coordinates.
 */
public class Point2D {
    private final Double x;
    private final Double y;

    /**
     * Constructs a 2D point with the given x and y values.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point2D(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    /** @return the x-coordinate */
    public Double getX() {
        return x;
    }

    /** @return the y-coordinate */
    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point2D(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point2D)) return false;
        Point2D other = (Point2D) obj;
        return Double.compare(x, other.x) == 0 &&
                Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }
}
