package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry{
    private final Point q;
    private final Vector normal;

    public Plane(Point q1, Point q2, Point q3) {
        this.q = q1;
        this.normal = null;
    }//where to include the normal calculation.

    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) { // point is not in used
        return this.normal;
    }
}
