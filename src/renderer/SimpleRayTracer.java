package renderer;

import lighting.AmbientLight;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> interPointList = this.scene.geometries.findIntersections(ray);
        if(interPointList == null){
            return this.scene.backround;
        }
        else
        {
            return calcColor(ray.findClosestPoint(interPointList));
        }



    }

    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }


}
