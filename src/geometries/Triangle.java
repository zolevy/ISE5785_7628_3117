package geometries;
import primitives.*;
/**
 * Represents a triangle in 3D space, defined as a specific case of a polygon with three vertices.
 */
public class Triangle extends Polygon{
    public Triangle(Point... vertices) {
        super(vertices);
    }
}