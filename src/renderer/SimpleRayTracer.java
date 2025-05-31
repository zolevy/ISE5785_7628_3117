package renderer;
import geometries.Intersectable.Intersection;

import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import primitives.Point;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.lang.Math.abs;
import static primitives.Util.alignZero;

/**
 * {@code SimpleRayTracer} is a basic implementation of the {@link RayTracerBase} class.
 * <p>
 * It calculates the color of a point by checking for intersections along a ray.
 * If no intersection is found, it returns the scene's background color.
 * Otherwise, it returns the ambient light intensity at the closest intersection point,
 * combined with local lighting effects such as diffuse and specular reflections.
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
            return calcColor(ray.findClosestIntersection(intersectionList), ray);
        }
    }

    /**
     * Computes the color at the given intersection point.
     * <p>
     * If the intersection is not valid based on ray direction and surface normal,
     * it returns black. Otherwise, it adds ambient light intensity scaled by
     * material absorption and local lighting effects.
     *
     * @param intersection the intersection point with geometry
     * @param ray the ray that hit the intersection
     * @return the computed color at the intersection
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        Vector rayDirection = ray.getDirection();
        if(preprocessoIntersection(intersection, rayDirection) == false) {
            return Color.BLACK;
        }
        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(calcColorLocalEffects(intersection));
    }

    /**
     * Preprocesses the intersection by setting ray direction, normal,
     * and their dot product, to determine if the intersection is valid.
     *
     * @param cut the intersection to preprocess
     * @param rayDirection the direction of the incoming ray
     * @return {@code false} if the ray is parallel to the surface (invalid intersection),
     *         {@code true} otherwise
     */
    public boolean preprocessoIntersection (Intersection cut, Vector rayDirection){
        cut.rayDirection = rayDirection;
        cut.normal = cut.geometry.getNormal(cut.point);
        cut.rayDirectionDPNormal = cut.rayDirection.dotProduct(cut.normal);
        if (Util.isZero(cut.rayDirectionDPNormal)) {
            return false; // Ray is parallel to the surface
        }
        return true;
    }

    /**
     * Sets the lighting parameters for the intersection relative to a given light source.
     *
     * @param cut the intersection to update
     * @param lightSource the light source to set
     * @return {@code false} if light is parallel to surface and ray direction,
     *         {@code true} otherwise
     */
    public boolean setLightSource(Intersection cut, LightSource lightSource) {
        cut.lightSource = lightSource;
        cut.lightDirection = lightSource.getL(cut.point);
        cut.lightDirectionDPNormal = cut.lightDirection.dotProduct(cut.normal);

        if (Util.isZero(cut.lightDirectionDPNormal) && Util.isZero(cut.rayDirectionDPNormal)) {
            return false; // Light source is parallel to the surface
        }
        return true;
    }

    /**
     * Calculates local lighting effects (diffuse and specular) at the intersection.
     *
     * @param intersection the intersection to compute lighting effects for
     * @return the color contribution from local lighting
     */
    private Color calcColorLocalEffects(Intersection intersection){
        Vector n = intersection.normal;
        double nv = intersection.rayDirectionDPNormal;
        Color color = intersection.geometry.getEmission();

        if (Util.isZero(nv)) {
            return color;
        }

        for (LightSource lightSource: scene.lights){
            if(setLightSource(intersection, lightSource) == false){
                continue;
            }
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Color iL = lightSource.getIntensity(intersection.point);
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse light component at the intersection.
     *
     * @param intersection the intersection to compute diffuse component for
     * @return a {@link Double3} representing diffuse scaling factors
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double nl = intersection.lightDirectionDPNormal;
        if (Util.isZero(nl)) {
            return Double3.ZERO; // No contribution if light direction is parallel to normal
        }
        return intersection.material.kD.scale(abs(nl));
    }

    /**
     * Calculates the specular light component at the intersection.
     *
     * @param intersection the intersection to compute specular component for
     * @return a {@link Double3} representing specular scaling factors
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector n = intersection.normal;
        Vector v = intersection.rayDirection;
        Vector l = intersection.lightDirection;

        double nl = intersection.lightDirectionDPNormal;

        if (Util.isZero(nl)) {
            return Double3.ZERO; // No contribution if light direction is parallel to normal
        }

        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        double vr = -alignZero(v.dotProduct(r));
        if (vr <= 0) {
            return Double3.ZERO; // No contribution if view direction is not aligned with reflection
        }

        return intersection.material.kS.scale(Math.pow(vr, intersection.material.nsh));

    }
}
