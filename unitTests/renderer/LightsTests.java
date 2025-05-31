package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test rendering images of spheres and triangles with different lighting configurations.
 * Includes directional, point, and spot lights, and combinations of multiple lights.
 *
 * Author: Dan Zilberstein
 */
class LightsTests {
   /** Default constructor to satisfy JavaDoc generator */
   LightsTests() { /* to satisfy JavaDoc generator */ }

   /** First scene for the lighting tests */
   private final Scene scene1 = new Scene("Test scene");

   /** Second scene with ambient light for the lighting tests */
   private final Scene scene2 = new Scene("Test scene")
           .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

   /** First camera builder */
   private final Camera.Builder camera1 = Camera.getBuilder()
           .setRayTracer(scene1, RayTracerType.SIMPLE)
           .setLocation(new Point(0.0, .0, 1000.0))
           .setDirection(Point.ZERO, Vector.AXIS_Y)
           .setVpSize(150, 150).setVpDistance(1000);

   /** Second camera builder */
   private final Camera.Builder camera2 = Camera.getBuilder()
           .setRayTracer(scene2, RayTracerType.SIMPLE)
           .setLocation(new Point(0.0, 0.0, 1000.0))
           .setDirection(Point.ZERO, Vector.AXIS_Y)
           .setVpSize(200, 200).setVpDistance(1000);

   /** Shininess coefficient */
   private static final int SHININESS = 301;

   /** Diffuse coefficient as a scalar */
   private static final double KD = 0.5;

   /** Diffuse coefficient as a color vector */
   private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

   /** Specular coefficient as a scalar */
   private static final double KS = 0.5;

   /** Specular coefficient as a color vector */
   private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

   /** Material for geometries */
   private final Material material = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);

   /** Light color for triangle tests */
   private final Color trianglesLightColor = new Color(800, 500, 250);

   /** Light color for sphere tests */
   private final Color sphereLightColor = new Color(800, 500, 0);

   /** Color of the sphere */
   private final Color sphereColor = new Color(BLUE).reduce(2);

   /** Sphere center point */
   private final Point sphereCenter = new Point(0.0, 0.0, -50.0);

   /** Radius of the sphere */
   private static final double SPHERE_RADIUS = 50d;

   /** Triangle vertices */
   private final Point[] vertices = {
           new Point(-110.0, -110.0, -150.0),
           new Point(95.0, 100.0, -150.0),
           new Point(110.0, -110.0, -150.0),
           new Point(-75.0, 78.0, 100.0)
   };

   /** Light position for the sphere tests */
   private final Point sphereLightPosition = new Point(-50.0, -50.0, 25.0);

   /** Light direction for the sphere tests */
   private final Vector sphereLightDirection = new Vector(1, 1, -0.5);

   /** Light position for the triangle tests */
   private final Point trianglesLightPosition = new Point(30.0, 10.0, -100.0);

   /** Light direction for the triangle tests */
   private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

   /** Sphere geometry */
   private final Geometry sphere = new Sphere(SPHERE_RADIUS, sphereCenter)
           .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));

   /** First triangle geometry */
   private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
           .setMaterial(material);

   /** Second triangle geometry */
   private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
           .setMaterial(material);

   /** Test: Sphere with directional light */
   @Test
   void sphereDirectional() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

      camera1
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightSphereDirectional");
   }

   /** Test: Sphere with point light */
   @Test
   void spherePoint() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition)
              .setKl(0.001).setKq(0.0002));

      camera1
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightSpherePoint");
   }

   /** Test: Sphere with spotlight */
   @Test
   void sphereSpot() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection)
              .setKl(0.001).setKq(0.0001));

      camera1
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightSphereSpot");
   }

   /** Test: Triangles with directional light */
   @Test
   void trianglesDirectional() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightTrianglesDirectional");
   }

   /** Test: Triangles with point light */
   @Test
   void trianglesPoint() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition)
              .setKl(0.001).setKq(0.0002));

      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightTrianglesPoint");
   }

   /** Test: Triangles with spotlight */
   @Test
   void trianglesSpot() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
              .setKl(0.001).setKq(0.0001));

      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightTrianglesSpot");
   }

   /** Test: Sphere with multiple light sources */
   @Test
   void sphereMany() {
      final Color sphereLightColorTwo = new Color(255, 120, 60);
      final Color sphereLightColorThree = new Color(60, 180, 255);
      final Color sphereLightColorFour = new Color(180, 60, 200);

      final Vector sphereLightDirectionTwo = new Vector(2.5, -1, -2);
      final Vector sphereLightDirectionThree = new Vector(-1, -3, 2);

      final Point sphereLightPositionTwo = new Point(5.0, 30.0, -60.0);
      final Point sphereLightPositionThree = new Point(-20.0, 5.0, 30.0);

      scene1.geometries.add(sphere);
      scene1.lights.add(new DirectionalLight(sphereLightColorTwo, sphereLightDirectionTwo));
      scene1.lights.add(new SpotLight(sphereLightColorThree, sphereLightPositionTwo, sphereLightDirectionThree)
              .setKl(0.003).setKq(0.0003));
      scene1.lights.add(new PointLight(sphereLightColorFour, sphereLightPositionThree)
              .setKl(0.002).setKq(0.0004));

      camera1
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightSphereMany");
   }

   /** Test: Triangles with multiple light sources */
   @Test
   void triangleMany() {
      final Color trianglesLightColorTwo = new Color(700, 100, 100);
      final Color trianglesLightColorThree = new Color(900, 300, 500);
      final Color trianglesLightColorFour = new Color(1000, 100, 600);

      final Vector trianglesLightDirectionTwo = new Vector(2, -2, -1);
      final Vector trianglesLightDirectionThree = new Vector(-3, -2, 1);

      final Point trianglesLightPositionTwo = new Point(50.0, 30.0, -100.0);
      final Point trianglesLightPositionThree = new Point(-60.0, 120.0, 10.0);

      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new DirectionalLight(trianglesLightColorTwo, trianglesLightDirectionTwo));
      scene2.lights.add(new SpotLight(trianglesLightColorThree, trianglesLightPositionTwo, trianglesLightDirectionThree)
              .setKl(0.005).setKq(0.00005));
      scene2.lights.add(new PointLight(trianglesLightColorFour, trianglesLightPositionThree)
              .setKl(0.004).setKq(0.0001));

      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("lightTrianglesMany");
   }
}
