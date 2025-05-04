package geometries;

import primitives.*;
import java.util.List;
import static primitives.Util.*;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with a specified radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere(Double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Computes the normal vector to the sphere at a given point on its surface.
     *
     * @param point a point on the surface of the sphere
     * @return the normal vector at the specified point
     */
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }

    /**
     * Returns the center point of the sphere.
     *
     * @return the center point
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Returns the radius of the sphere.
     *
     * @return the radius
     */
    public Double getRadius() {
        return super.getRadius();
    }

    /**
     * Finds the intersection points of a ray with the sphere.
     *
     * @param ray the ray to intersect with the sphere
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point rayOrigin = ray.getHead();
        Vector rayDirection = ray.getDirection();

        Point sphereCenter = getCenter();
        double sphereRadius = getRadius();
        if (rayOrigin.equals(center)) {
            return List.of(rayOrigin);
        }
        Vector u = sphereCenter.subtract(rayOrigin);

        double tm = alignZero(rayDirection.dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double radiusSquared = alignZero(sphereRadius * sphereRadius);

        if (dSquared >= radiusSquared) return null;

        double thSquared = radiusSquared - dSquared;
        if (isZero(thSquared)) return null;

        double th = Math.sqrt(thSquared);

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        boolean t1Valid = t1 > 0;
        boolean t2Valid = t2 > 0;

        if (!t1Valid && !t2Valid) return null;

        if (t1Valid && !t2Valid)
            return List.of(ray.getPoint(t1));

        if (t2Valid && !t1Valid)
            return List.of(ray.getPoint(t2));

        return List.of(ray.getPoint(t1), ray.getPoint(t2));
    }


}
