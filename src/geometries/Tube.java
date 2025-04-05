package geometries;

import primitives.*;

public class Tube extends RadialGeometry {
    protected final Ray axis;

    public Tube(Double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal (Point givenPoint)
    {
        Point point0 = axis.getHead();
        Vector vector = axis.getDirection().normalize();
        //calculate center point
        Vector u = givenPoint.subtract(point0); // Vector from axis base to P
        double t = vector.dotProduct(u); // Projection scalar
        Point center = point0.add(vector.scale(t)); // Closest point on the axis
        return givenPoint.subtract(center).normalize();
    }
}
