package renderer;
import primitives.Material;
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
    private static final double DELTA = 0.1;

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;


    /**
     * Checks if a point is unshaded, meaning it is not blocked from the light source.
     *
     * @param intersection the intersection point
     * @param lightSource the light source to check against
     * @param l the vector from the point to the light
     * @param n the normal vector at the point
     * @param nl the dot product of n and l
     * @return {@code true} if the point is unshaded, {@code false} otherwise
     */

    /*private boolean unshaded(Intersection intersection, LightSource lightSource, Vector l, Vector n, double nl) {
        Vector pointLight = l.scale(-1.0);
        Ray lightRay = new Ray(intersection.point, pointLight, DELTA, n);
        List<Point> intersections = scene.geometries.findIntersections(lightRay);
        if (intersections == null)
            return true;
        for (Point p : intersections) {
            if (p.distance(intersection.point) < lightSource.getDistance(intersection.point) &&
                    //(intersection.material.kR.lowerThan(MIN_CALC_COLOR_K))&&
                    intersection.material.kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return true;
    }*/

    private boolean unshaded(Intersection intersection, LightSource lightSource, Vector l, Vector n, double nl) {
        Vector pointLight = l.scale(-1.0);
        Ray lightRay = new Ray(intersection.point, pointLight, DELTA, n);

        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(lightRay);
        if (intersections == null || intersections.isEmpty())
            return true;

        for (Intersection blocking : intersections) {
            if (blocking.point.distance(intersection.point) < lightSource.getDistance(intersection.point)) {
                if (blocking.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false;
                }
            }
        }

        return true;
    }

    private Double3 transparency(Intersection intersection) {
        Double3 ktr = Double3.ONE;

        Vector pointLight = intersection.lightDirection.scale(-1.0);
        Ray lightRay = new Ray(intersection.point, pointLight, DELTA, intersection.normal);

        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(lightRay);
        if (intersections == null || intersections.isEmpty())
            return ktr;

        for (Intersection blocking : intersections) {
            if (blocking.point.distance(intersection.point) < intersection.lightSource.getDistance(intersection.point)) {
                ktr = ktr.product(blocking.geometry.getMaterial().kT);
                if (blocking.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }

        return ktr;
    }


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
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return this.scene.backround;
        } else {
            return calcColor(intersection, ray);
        }
    }

    /**
     * Computes the color at the given intersection point.
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
        return scene.ambientLight.getIntensity().add(calcColor(intersection,MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /*private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));

    }*/
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        if (level == 0 || k.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        Color color = calcColorLocalEffects(intersection);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Preprocesses the intersection by setting the ray direction and normal.
     *
     * @param cut the intersection to preprocess
     * @param rayDirection the direction of the incoming ray
     * @return {@code false} if the ray is parallel to the surface, {@code true} otherwise
     */
    public boolean preprocessoIntersection (Intersection cut, Vector rayDirection){
        cut.rayDirection = rayDirection;
        cut.normal = cut.geometry.getNormal(cut.point);
        cut.rayDirectionDPNormal = cut.rayDirection.dotProduct(cut.normal);
        if (Util.isZero(cut.rayDirectionDPNormal)) {
            return false;
        }
        return true;
    }

    /**
     * Sets the lighting vectors for a given light source.
     *
     * @param cut the intersection to update
     * @param lightSource the light source to set
     * @return {@code false} if light and ray are both parallel to the surface, {@code true} otherwise
     */
    public boolean setLightSource(Intersection cut, LightSource lightSource) {
        cut.lightSource = lightSource;
        cut.lightDirection = lightSource.getL(cut.point);
        cut.lightDirectionDPNormal = cut.lightDirection.dotProduct(cut.normal);

        if (Util.isZero(cut.lightDirectionDPNormal) && Util.isZero(cut.rayDirectionDPNormal)) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the local lighting (diffuse + specular) at the intersection point.
     *
     * @param intersection the intersection to compute lighting for
     * @return the resulting color contribution from lights
     */
    private Color calcColorLocalEffects(Intersection intersection) {
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
            if (nl * nv > 0 ) {
                Double3 ktr = transparency(intersection);
                if (ktr.greaterThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
                }
            }
        }
        return color;
    }




    /**
     * Calculates the diffuse lighting component at a point.
     *
     * @param intersection the intersection point
     * @return the diffuse contribution as a {@link Double3}
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double nl = intersection.lightDirectionDPNormal;
        if (Util.isZero(nl)) {
            return Double3.ZERO;
        }
        return intersection.material.kD.scale(abs(nl));
    }

    /**
     * Calculates the specular lighting component at a point.
     *
     * @param intersection the intersection point
     * @return the specular contribution as a {@link Double3}
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector n = intersection.normal;
        Vector v = intersection.rayDirection;
        Vector l = intersection.lightDirection;

        double nl = intersection.lightDirectionDPNormal;

        if (Util.isZero(nl)) {
            return Double3.ZERO;
        }

        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        double vr = -alignZero(v.dotProduct(r));
        if (vr <= 0) {
            return Double3.ZERO;
        }

        return intersection.material.kS.scale(Math.pow(vr, intersection.material.nsh));

      }

    /*public Ray constructReflectedRay(Intersection intersection) {
        /*Vector n = intersection.normal;
        Vector v = intersection.rayDirection;
        double nv = intersection.rayDirectionDPNormal;
        if (Util.isZero(nv)) {
            return null; // Ray is parallel to the surface, no reflection
        }
        Vector r = v.subtract(n.scale(2 * nv)).normalize();//.scale(-1.0);
        return new Ray(intersection.point, r, DELTA, n);*
        Vector v = intersection.rayDirection.normalize();
        Vector n = intersection.normal.normalize();
        Vector r = v.add(n.scale(2 * n.dotProduct(v)).scale(-1.0)).normalize();
        return new Ray(intersection.point, r, DELTA, n);

    }*/
    public Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDirection.normalize();
        Vector n = intersection.normal.normalize();

        // Correct reflection formula: r = v - 2(v·n)n
        Vector r = v.subtract(n.scale(2 * n.dotProduct(v)));

        return new Ray(intersection.point, r, DELTA, n);
    }

    public Ray constructRefractedRay (Intersection intersection){
        /*Vector n = intersection.normal;
        Vector v = intersection.rayDirection;
        double nv = intersection.rayDirectionDPNormal;
        if (Util.isZero(nv)) {
            return null; // Ray is parallel to the surface, no refraction
        }
        Vector t = v.subtract(n.scale(nv)).normalize();//.scale(-1.0);
        return new Ray(intersection.point, t, DELTA, n);*/
        Vector v = intersection.rayDirection;
        Vector n = intersection.normal;
        double nv = intersection.rayDirectionDPNormal;

        Point startPoint = intersection.point.add(n.scale(nv > 0 ? DELTA : -DELTA));
        return new Ray(startPoint, v, DELTA, n);
    }

    /*private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcColorGLobalEffect(constructRefractedRay(intersection),
            level, k, intersection.material.kT)
            .add(calcColorGLobalEffect(constructReflectedRay(intersection),
                    level, k, intersection.material.kR));
    }

    private Color calcColorGLobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }
        Intersection intersection = ray.findClosestIntersection(scene.geometries.calculateIntersectionsHelper(ray));
        if (intersection == null){
            return scene.backround.scale(kx);
        }

        return preprocessoIntersection(intersection, ray.getDirection()) ?
                calcColor(intersection, level - 1, kkx) : Color.BLACK;
    }*/
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Ray refractedRay = constructRefractedRay(intersection);
        Ray reflectedRay = constructReflectedRay(intersection);

        Color refracted = calcColorGLobalEffect(refractedRay, level, k.product(intersection.material.kT), intersection.material.kT);
        Color reflected = calcColorGLobalEffect(reflectedRay, level, k.product(intersection.material.kR), intersection.material.kR);

        return refracted.add(reflected);
    }

    private Color calcColorGLobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        if (k.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        Intersection intersection = ray.findClosestIntersection(scene.geometries.calculateIntersectionsHelper(ray));
        if (intersection == null) {
            return scene.backround.scale(kx);
        }

        return preprocessoIntersection(intersection, ray.getDirection()) ?
                calcColor(intersection, level - 1, k).scale(kx) : Color.BLACK;
    }


    private Intersection findClosestIntersection(Ray ray){
        List<Intersection> intersectionList = scene.geometries.calculateIntersectionsHelper(ray);
        if (intersectionList == null || intersectionList.isEmpty()) {
            return null;
        }
        return ray.findClosestIntersection(intersectionList);
    }

}
