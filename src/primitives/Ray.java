package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point (head) and a direction vector.
 */
public class Ray {

    /**
     * The starting point of the ray.
     */
    private final Point head;

    /**
     * The normalized direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Returns the starting point of the ray.
     *
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns a point on the ray at a distance {@code t} from the head in the direction of the ray.
     *
     * @param t the distance from the ray origin
     * @return the point located at distance {@code t} along the ray
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Constructs a ray with a given head point and direction vector.
     * The direction vector is normalized upon initialization.
     *
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Compares this ray to the specified object for equality.
     * Two rays are considered equal if their head and direction are equal.
     *
     * @param obj the object to compare to
     * @return true if the rays are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    /**
     * Returns a string representation of the ray.
     *
     * @return a string representing the ray
     */
    @Override
    public String toString() {
        return "Ray(head=" + head + ", direction=" + direction + ")";
    }

    /**
     * Finds and returns the point in the given list that is closest to the head of the ray.
     *
     * @param L the list of points to search through
     * @return the point closest to the ray's head, or {@code null} if the list is {@code null} or empty
     */
    public Point findClosestPoint(List<Point> L) {
        if (L == null)
            return null;
        Point startingPoint = this.getHead();
        double minDistance = Double.POSITIVE_INFINITY;
        Point minDistancePoint = null;
        double currentDistance;
        for (Point currentPoint : L) {
            currentDistance = startingPoint.distance(currentPoint); // calculate distance
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                minDistancePoint = currentPoint;
            }
        }
        return minDistancePoint;
    }
}
