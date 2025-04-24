package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> geoComposite = new LinkedList<>();

    public Geometries (){}

     public Geometries (Intersectable... geometries){
        add(geometries);
     }

    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            geoComposite.add(geometry);
        }
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionPoints = null;
        for (Intersectable geometry : geoComposite) {
            List<Point> tempPoints = geometry.findIntersections(ray);
            if (tempPoints != null) {
                if (intersectionPoints == null) {
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(tempPoints);
            }
        }
        return intersectionPoints;
    }

}
