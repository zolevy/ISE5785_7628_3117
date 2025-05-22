package geometries;

import primitives.*;
import java.util.List;

/**
 * Interface representing an object that can be intersected by a ray.
 * Classes implementing this interface must provide a method to find intersection points with a ray.
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points between a ray and the object.
     *
     * @param ray the ray to intersect with the object
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals (Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other) && (other.geometry == this.geometry) && this.point.equals(other.point);
        }

        @Override
        public String toString() {
            return String.format(geometry.toString(),point.toString());
        }
    }


    protected List<Intersection> calculateIntersectionsHelper(Ray ray)
    {
        return null;
    }

    public final List<Intersection> calculateIntersections(Ray ray)
    {
        return calculateIntersectionsHelper(ray);
    }
}
