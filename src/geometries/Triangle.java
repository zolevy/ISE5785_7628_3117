package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Represents a triangle in 3D space, defined as a specific case of a polygon with three vertices.
 */
public class Triangle extends Polygon {
    public Triangle(Point... vertices) {
        super(vertices);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        // plane intersection
        Vector n = getNormal(p0);
        double nDotDir = alignZero(n.dotProduct(dir));
        if (isZero(nDotDir)) return null; // Ray is parallel to the triangle's plane

        double t = alignZero(n.dotProduct(v0.subtract(p0)) / nDotDir);
        if (t <= 0) return null; // Intersection point is behind the ray's origin

        Point p = ray.getPoint(t); // Intersection point with the plane

        // Inside-triangle test using edge-crossing method
        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v1);
        Vector edge3 = v0.subtract(v2);

        Vector vp1 = p.subtract(v0);
        Vector vp2 = p.subtract(v1);
        Vector vp3 = p.subtract(v2);

        double s1 = alignZero(n.dotProduct(edge1.crossProduct(vp1)));
        double s2 = alignZero(n.dotProduct(edge2.crossProduct(vp2)));
        double s3 = alignZero(n.dotProduct(edge3.crossProduct(vp3)));

        // All signs must be the same or zero
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(p);
        }

        return null;
    }


}
