package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;

public class AABB {
    public final Point min;
    public final Point max;

    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

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

}
