package geometries;

import primitives.*;

import java.util.ArrayList;
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();
        Point O = this.center;

        Vector u;
        try {
            u = O.subtract(P0);
        } catch (IllegalArgumentException e) {
            // Ray starts at the center of the sphere
            Point intersection = P0.add(v.scale(this.radius));
            return List.of(new Intersection(this, intersection));
        }

        double tm = v.dotProduct(u);
        double dSquared = u.lengthSquared() - tm * tm;
        double rSquared = this.radius * this.radius;

        if (dSquared > rSquared) return null;

        double thSquared = rSquared - dSquared;
        if (isZero(thSquared)) thSquared = 0; // for safety in sqrt
        if (thSquared < 0) return null;

        double th = Math.sqrt(thSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        List<Intersection> result = new ArrayList<>();
        if (t1 > 0)
            result.add(new Intersection(this, P0.add(v.scale(t1))));
        if (t2 > 0)
            result.add(new Intersection(this, P0.add(v.scale(t2))));

        return result.isEmpty() ? null : result;
    }

}
