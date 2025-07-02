package geometries;

import primitives.*;

import java.util.List;

/**
 * Axis-Aligned Bounding Box (AABB) representing a 3D box defined by minimum and maximum points.
 */
public class AABB {
    /** The minimum point (corner) of the bounding box. */
    public final Point min;
    /** The maximum point (corner) of the bounding box. */
    public final Point max;

    /**
     * Constructs an AABB given the minimum and maximum points.
     * @param min The minimum point of the box.
     * @param max The maximum point of the box.
     */
    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Checks if the given ray intersects this bounding box.
     * Uses the slab method for intersection testing.
     * @param ray The ray to test for intersection.
     * @return true if the ray intersects the bounding box, false otherwise.
     */
    public boolean intersects(Ray ray) {
        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        double originX = ray.getHead().getX();
        double originY = ray.getHead().getY();
        double originZ = ray.getHead().getZ();

        double dirX = ray.getDirection().getX();
        double dirY = ray.getDirection().getY();
        double dirZ = ray.getDirection().getZ();

        double minX = min.getX();
        double minY = min.getY();
        double minZ = min.getZ();

        double maxX = max.getX();
        double maxY = max.getY();
        double maxZ = max.getZ();

        // X axis
        if (Util.isZero(dirX)) {
            if (originX < minX || originX > maxX) return false;
        } else {
            double invDx = 1.0 / dirX;
            double t0 = (minX - originX) * invDx;
            double t1 = (maxX - originX) * invDx;
            if (invDx < 0) {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            tMin = Math.max(tMin, t0);
            tMax = Math.min(tMax, t1);
            if (tMax < tMin) return false;
        }

        // Y axis
        if (Util.isZero(dirY)) {
            if (originY < minY || originY > maxY) return false;
        } else {
            double invDy = 1.0 / dirY;
            double t0 = (minY - originY) * invDy;
            double t1 = (maxY - originY) * invDy;
            if (invDy < 0) {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            tMin = Math.max(tMin, t0);
            tMax = Math.min(tMax, t1);
            if (tMax < tMin) return false;
        }

        // Z axis
        if (Util.isZero(dirZ)) {
            if (originZ < minZ || originZ > maxZ) return false;
        } else {
            double invDz = 1.0 / dirZ;
            double t0 = (minZ - originZ) * invDz;
            double t1 = (maxZ - originZ) * invDz;
            if (invDz < 0) {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            tMin = Math.max(tMin, t0);
            tMax = Math.min(tMax, t1);
            if (tMax < tMin) return false;
        }

        return true;
    }

    /**
     * Combines two AABBs into one that encloses both.
     * @param a The first bounding box.
     * @param b The second bounding box.
     * @return A new AABB that contains both input boxes.
     */
    public static AABB combine(AABB a, AABB b) {
        if (a == null) return b;
        if (b == null) return a;

        Point min = new Point(
                Math.min(a.min.getX(), b.min.getX()),
                Math.min(a.min.getY(), b.min.getY()),
                Math.min(a.min.getZ(), b.min.getZ())
        );
        Point max = new Point(
                Math.max(a.max.getX(), b.max.getX()),
                Math.max(a.max.getY(), b.max.getY()),
                Math.max(a.max.getZ(), b.max.getZ())
        );
        return new AABB(min, max);
    }

    /**
     * Combines the bounding boxes of all geometries into one encompassing AABB.
     * @param geometries The list of geometries to combine.
     * @return An AABB enclosing all geometries.
     */
    public static AABB combineAll(List<Intersectable> geometries) {
        AABB result = null;
        for (Intersectable g : geometries) {
            g.computeBoundingBoxIfNeeded();
            result = combine(result, g.getBoundingBox());
        }
        return result;
    }

    /**
     * Gets the size vector (width, height, depth) of the bounding box.
     * @return The size vector.
     */
    public Vector getSize() {
        return max.subtract(min);
    }

    /**
     * Gets the center point of the bounding box.
     * @return The center point.
     */
    public Point getCenter() {
        return min.add(max.subtract(min).scale(0.5));
    }
}
