package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase  {

    /**
     * The scene to be rendered.
     */
    protected Scene scene; //final

    public RayTracerBase(Scene scene) {
        this.scene = scene;

    }

    public Color traceRay(Ray ray) {return null;}
}
