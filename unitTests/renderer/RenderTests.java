package renderer;
import lighting.*;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * @author Dan
 */
public class RenderTests {
   /** Default constructor to satisfy JavaDoc generator */
   public RenderTests() { /* to satisfy JavaDoc generator */ }

   /** Camera builder of the tests */
   private final Camera.Builder camera = Camera.getBuilder() //
           .setLocation(Point.ZERO).setDirection(new Point(0.0, 0.0, -1.0), Vector.AXIS_Y) //
           .setVpDistance(100) //
           .setVpSize(500, 500);

   /**
    * Produce a scene with basic 3D model and render it into a png image with a
    * grid
    */
   @Test
   public void renderTwoColorTest() {
      Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
              .setAmbientLight(new AmbientLight(new Color(255, 191, 191)));

      scene.geometries.add(
              // center
              new Sphere(50.0, new Point(0.0, 0.0, -100.0)),
              // up left
              new Triangle(
                      new Point(-100.0, 0.0, -100.0),
                      new Point(0.0, 100.0, -100.0),
                      new Point(-100.0, 100.0, -100.0)),
              // down left
              new Triangle(
                      new Point(-100.0, 0.0, -100.0),
                      new Point(0.0, -100.0, -100.0),
                      new Point(-100.0, -100.0, -100.0)),
              // down right
              new Triangle(
                      new Point(100.0, 0.0, -100.0),
                      new Point(0.0, -100.0, -100.0),
                      new Point(100.0, -100.0, -100.0))
      );

      camera //
              .setRayTracer(scene, RayTracerType.SIMPLE) //
              .setResolution(1000, 1000) //
              .build() //
              .renderImage() //
              .printGrid(100, new Color(YELLOW)) //
              .writeToImage("Two color render test");
   }
}
