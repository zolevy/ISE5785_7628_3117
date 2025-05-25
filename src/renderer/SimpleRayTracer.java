package renderer;
import geometries.Intersectable. Intersection;

import lighting.AmbientLight;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * {@code SimpleRayTracer} is a basic implementation of the {@link RayTracerBase} class.
 * <p>
 * It calculates the color of a point by checking for intersections along a ray.
 * If no intersection is found, it returns the scene's background color.
 * Otherwise, it returns the ambient light intensity at the closest intersection point.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a {@code SimpleRayTracer} with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a given ray and computes the resulting color at the intersection point.
     *
     * @param ray the ray to trace
     * @return the computed color at the closest intersection point,
     *         or the background color if no intersections are found
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersectionList = this.scene.geometries.calculateIntersectionsHelper(ray);
        if (intersectionList == null) {
            return this.scene.backround;
        } else {
            return calcColor(ray.findClosestIntersection(intersectionList));
        }
    }

    /**
     * Computes the color at the given point.
     * Currently, this method returns only the ambient light intensity.
     *
     * @param point the point at which to compute the color
     * @return the ambient light color
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(intersection.geometry.getEmission());
    }
}
