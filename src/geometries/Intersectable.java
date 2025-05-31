package geometries;

import lighting.LightSource;
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

    /**
     * Represents a single intersection between a ray and a geometry.
     * Includes additional data for shading and lighting calculations.
     */
    public static class Intersection {

        /** The geometry object that was intersected */
        public final Geometry geometry;

        /** The point at which the intersection occurs */
        public final Point point;

        /** The material of the intersected geometry */
        public final Material material;

        /** The direction of the ray at the point of intersection */
        public Vector rayDirection;

        /** The normal vector at the point of intersection */
        public Vector normal;

        /** Dot product between ray direction and normal (used in shading) */
        public double rayDirectionDPNormal;

        /** The light source involved in the shading calculation */
        public LightSource lightSource;

        /** The direction from the point to the light source */
        public Vector lightDirection;

        /** Dot product between light direction and normal (used in shading) */
        public double lightDirectionDPNormal;

        /**
         * Constructs an Intersection object with the given geometry and point.
         *
         * @param geometry the geometry intersected
         * @param point    the point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            if(geometry == null){
                this.material = null;
            } else {
                this.material = geometry.getMaterial();
            }
        }

        /**
         * Checks if this intersection is equal to another object.
         * Two intersections are equal if they have the same geometry and point.
         *
         * @param obj the object to compare
         * @return true if equal, false otherwise
         */
        @Override
        public boolean equals (Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other) && (other.geometry == this.geometry) && this.point.equals(other.point);
        }

        /**
         * Returns a string representation of the intersection.
         *
         * @return string representing the intersection
         */
        @Override
        public String toString() {
            return String.format(geometry.toString(),point.toString());
        }
    }

    /**
     * Helper method for calculating intersections.
     * Should be implemented in subclasses.
     *
     * @param ray the ray to intersect
     * @return list of intersection data, or null if none
     */
    protected List<Intersection> calculateIntersectionsHelper(Ray ray)
    {
        return null;
    }

    /**
     * Returns the list of full intersection data (geometry + point).
     *
     * @param ray the ray to test for intersections
     * @return list of intersection objects or null
     */
    public final List<Intersection> calculateIntersections(Ray ray)
    {
        return calculateIntersectionsHelper(ray);
    }
}
