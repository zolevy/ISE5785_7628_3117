package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracers.
 * Provides the framework for tracing rays within a scene to determine color.
 */
public abstract class RayTracerBase  {

    /**
     * The scene to be rendered by the ray tracer.
     */
    protected Scene scene; //final

    /**
     * Constructor that initializes the ray tracer with a scene.
     *
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray into the scene and calculates the resulting color.
     *
     * @param ray the ray to trace
     * @return the color resulting from tracing the ray
     */
    public Color traceRay(Ray ray) {
        return null;
    }
}
