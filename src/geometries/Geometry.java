package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for all geometric shapes.
 * Contains common properties: emission color and material.
 * Subclasses must implement {@code getNormal(Point)} to define the surface normal.
 *
 * Also includes an internal list for use by composite geometries.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The base emission color of the geometry.
     * Default is {@code Color.BLACK}.
     */
    protected Color emission = Color.BLACK;

    /**
     * The material properties of the geometry (diffuse, specular, shininess).
     */
    private Material material = new Material();

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the current geometry (for method chaining)
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Returns the material of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param newEmission the color to set
     * @return the current geometry (for method chaining)
     */
    public Geometry setEmission(Color newEmission) {
        this.emission = newEmission;
        return this;
    }

    /**
     * Calculates the normal vector to the surface at a given point.
     *
     * @param point the point on the surface of the geometry
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);

    /**
     * A list that holds other intersectable geometries.
     * Used in subclasses for managing multiple geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<>();
}
