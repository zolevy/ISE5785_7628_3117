package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Testing basic shadows with different configurations of geometries and light sources.
 * The class includes various test cases to evaluate the behavior of shadow rendering.
 */
class ShadowTests {

   /** Default constructor */
   ShadowTests() { }

   /** Shared scene for the test cases */
   private final Scene scene = new Scene("Test scene");

   /** Shared camera builder with common settings */
   private final Camera.Builder camera = Camera.getBuilder()
           .setLocation(new Point(0.0, 0.0, 1000.0))
           .setDirection(Point.ZERO, Vector.AXIS_Y)
           .setVpDistance(1000.0)
           .setVpSize(200.0, 200.0)
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /** A blue sphere used in several tests */
   private final Intersectable sphere = new Sphere(60.0, new Point(0.0, 0.0, -200.0))
           .setEmission(new Color(BLUE))
           .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));

   /** Shared material used for triangles */
   private final Material trMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);

   /**
    * Helper method to test a scene with a sphere and a triangle under a spot light.
    *
    * @param pictName     name of the output image
    * @param triangle     the triangle to add
    * @param spotLocation the position of the spot light
    */
   private void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) {
      scene.geometries.add(sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));
      scene.lights
              .add(new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1.0, 1.0, -3.0))
                      .setKl(1E-5).setKq(1.5E-7));
      camera
              .setResolution(400, 400)
              .build()
              .renderImage()
              .writeToImage(pictName);
   }

   /** Test: initial triangle and spot light position */
   @Test
   void sphereTriangleInitial() {
      sphereTriangleHelper("shadowSphereTriangleInitial",
              new Triangle(new Point(-70.0, -40.0, 0.0), new Point(-40.0, -70.0, 0.0), new Point(-68.0, -68.0, -4.0)),
              new Point(-100.0, -100.0, 200.0));
   }

   /** Test: move triangle slightly */
   @Test
   void sphereTriangleMove1() {
      sphereTriangleHelper("shadowSphereTriangleMove1",
              new Triangle(new Point(-68.0, -38.0, 1.0), new Point(-38.0, -68.0, 1.0), new Point(-66.0, -66.0, -3.0)),
              new Point(-100.0, -100.0, 200.0));
   }

   /** Test: move triangle further */
   @Test
   void sphereTriangleMove2() {
      sphereTriangleHelper("shadowSphereTriangleMove2",
              new Triangle(new Point(-60.0, -30.0, 10.0), new Point(-30.0, -60.0, 10.0), new Point(-60.0, -60.0, 5.0)),
              new Point(-100.0, -100.0, 200.0));
   }

   /** Test: move spot light closer */
   @Test
   void sphereTriangleSpot1() {
      sphereTriangleHelper("shadowSphereTriangleSpot1",
              new Triangle(new Point(-70.0, -40.0, 0.0), new Point(-40.0, -70.0, 0.0), new Point(-68.0, -68.0, -4.0)),
              new Point(-80.0, -80.0, 100.0));
   }

   /** Test: move spot light to the side */
   @Test
   void sphereTriangleSpot2() {
      sphereTriangleHelper("shadowSphereTriangleSpot2",
              new Triangle(new Point(-70.0, -40.0, 0.0), new Point(-40.0, -70.0, 0.0), new Point(-68.0, -68.0, -4.0)),
              new Point(-70.0, -50.0, 20.0));
   }

   /**
    * Test: Two large triangles and a small sphere under a spot light,
    * to test partial and complex shadow casting.
    */
   @Test
   void trianglesSphere() {
      scene.geometries
              .add(
                      new Triangle(new Point(-150.0, -150.0, -115.0), new Point(150.0, -150.0, -135.0), new Point(75.0, 75.0, -150.0))
                              .setMaterial(new Material().setKS(0.8).setShininess(60)),
                      new Triangle(new Point(-150.0, -150.0, -115.0), new Point(-70.0, 70.0, -140.0), new Point(75.0, 75.0, -150.0))
                              .setMaterial(new Material().setKS(0.8).setShininess(60)),
                      new Sphere(30.0, new Point(0.0, 0.0, -11.0))
                              .setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30))
              );
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights
              .add(new SpotLight(new Color(700, 400, 400), new Point(40.0, 40.0, 115.0), new Vector(-1.0, -1.0, -4.0))
                      .setKl(4E-4).setKq(2E-5));

      camera
              .setResolution(600, 600)
              .build()
              .renderImage()
              .writeToImage("shadowTrianglesSphere");
   }
}
