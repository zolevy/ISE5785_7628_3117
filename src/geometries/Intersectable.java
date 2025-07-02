package geometries;

import lighting.LightSource;
import primitives.*;
import java.util.List;

/**
 * Abstract class representing an object that can be intersected by a ray.
 * Classes extending this must provide methods to calculate intersections and bounding boxes.
 */
public abstract class Intersectable {
    // Static flags to enable/disable AABB optimization globally
    public static boolean enableBVH = false;
    public static boolean enableCBR = false;

    // Cached bounding box, computed lazily
    private AABB boundingBox = null;

    /**
     * Lazily compute and cache the bounding box if not already computed.
     */
    protected void computeBoundingBoxIfNeeded() {
        if (boundingBox == null) {
            boundingBox = createBoundingBox();
        }
    }

    /**
     * Returns the cached bounding box or computes it if needed.
     * Returns null if neither CBR nor BVH optimizations are enabled.
     * @return The bounding box or null.
     */
    public AABB getBoundingBox() {
        if (!(enableCBR || enableBVH)) return null;
        computeBoundingBoxIfNeeded();
        return boundingBox;
    }

    /**
     * Abstract method for subclasses to implement specific bounding box calculation.
     * @return The bounding box of the geometry.
     */
    protected abstract AABB createBoundingBox();

    /**
     * Finds the intersection points between a ray and the object.
     *
     * @param ray the ray to intersect with the object
     * @return a list of intersection points, or null if none
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Represents a single intersection between a ray and a geometry,
     * including data for shading and lighting.
     */
    public static class Intersection {

        /** The geometry object intersected */
        public final Geometry geometry;

        /** The point of intersection */
        public final Point point;

        /** The material of the intersected geometry */
        public final Material material;

        /** The ray direction at intersection point */
        public Vector rayDirection;

        /** The normal vector at intersection point */
        public Vector normal;

        /** Dot product between ray direction and normal (for shading) */
        public double rayDirectionDPNormal;

        /** The light source for shading */
        public LightSource lightSource;

        /** Direction from intersection point to light */
        public Vector lightDirection;

        /** Dot product between light direction and normal (for shading) */
        public double lightDirectionDPNormal;

        /**
         * Returns the intersection point.
         * @return The point of intersection.
         */
        public Point getPoint() {
            return this.point;
        }

        /**
         * Constructs an Intersection with given geometry and point.
         * @param geometry The geometry intersected
         * @param point The point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = (geometry == null) ? null : geometry.getMaterial();
        }

        /**
         * Checks equality: same geometry and point.
         * @param obj Object to compare
         * @return true if equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other) && (other.geometry == this.geometry) && this.point.equals(other.point);
        }

        /**
         * String representation of the intersection.
         * @return String describing the intersection
         */
        @Override
        public String toString() {
            return String.format(geometry.toString(), point.toString());
        }
    }

    /**
     * Helper method to be implemented by subclasses for intersection calculation.
     * @param ray The ray to intersect
     * @return List of Intersection objects or null if none
     */
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * Returns list of intersections for the given ray.
     * @param ray The ray to test intersections
     * @return List of Intersection objects or null
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }
}
