package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;
import renderer.Camera;

public class Scene {
    public String name;
    public Color backround = Color.BLACK;
    public AmbientLight ambientLight  = AmbientLight.NONE;
    public Geometries geometries = new Geometries();
    public Scene(String name)
    {
        this.name = name;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /*public Scene setBackround(Color backround) {
        this.backround = backround;
        return this;
    }*/

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setBackground(Color color) {
        this.backround = color;
        return this;

    }


}
