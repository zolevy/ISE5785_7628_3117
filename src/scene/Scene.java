package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import renderer.Camera;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene containing geometries, lights, and environment settings.
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene.
     */
    public Color backround = Color.BLACK;

    /**
     * The ambient light of the scene.
     */
    public AmbientLight ambientLight  = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * The list of light sources illuminating the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a scene with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the list of light sources for the scene.
     *
     * @param lights list of light sources
     * @return this scene, for chaining
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Sets the ambient light for the scene.
     *
     * @param ambientLight the ambient light
     * @return this scene, for chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     *
     * @param geometries the geometries collection
     * @return this scene, for chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param color the background color
     * @return this scene, for chaining
     */
    public Scene setBackground(Color color) {
        this.backround = color;
        return this;
    }
}
