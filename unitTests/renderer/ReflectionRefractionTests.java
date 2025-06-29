package renderer;

import static java.awt.Color.*;
import static renderer.RayTracerType.GRID;
import static renderer.RayTracerType.SIMPLE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows (with transparency)
 */
class ReflectionRefractionTests {
   ReflectionRefractionTests() {}

   private final Scene scene = new Scene("Test scene");
   private final Camera.Builder cameraBuilder = Camera.getBuilder()
           .setRayTracer(scene, SIMPLE);

   @Test
   void twoSpheres() {
      scene.geometries.add(
              new Sphere(50d, new Point(0.0, 0.0, -50.0)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setkT(0.3)),
              new Sphere(25d,new Point(0.0, 0.0, -50.0)).setEmission(new Color(RED))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))
      );
      scene.lights.add(
              new SpotLight(new Color(1000, 600, 0), new Point(-100.0, -100.0, 500.0), new Vector(-1, -1, -2))
                      .setKl(0.0004).setKq(0.0000006)
      );

      cameraBuilder

              .setLocation(new Point(0.0, 0.0, 1000.0))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(1000).setVpSize(150, 150)
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("refractionTwoSpheres");
   }

   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add(
              new Sphere(400d, new Point(-950.0, -900.0, -1000.0)).setEmission(new Color(0, 50, 100))
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)
                              .setkT(new Double3(0.5, 0, 0))),
              new Sphere(200d, new Point(-950.0, -900.0, -1000.0)).setEmission(new Color(100, 50, 20))
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)),
              new Triangle(new Point(1500.0, -1500.0, -1500.0), new Point(-1500.0, 1500.0, -1500.0),
                      new Point(670.0, 670.0, 3000.0))
                      .setEmission(new Color(20, 20, 20))
                      .setMaterial(new Material().setkR(1.0)),
              new Triangle(new Point(1500.0, -1500.0, -1500.0), new Point(-1500.0, 1500.0, -1500.0),
                      new Point(-1500.0, -1500.0, -2000.0))
                      .setEmission(new Color(20, 20, 20))
                      .setMaterial(new Material().setkR(new Double3(0.5, 0.0, 0.4)))
      );
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750.0, -750.0, -150.0), new Vector(-1, -1, -4))
              .setKl(0.00001).setKq(0.000005));

      cameraBuilder
              .setLocation(new Point(0.0, 0.0, 10000.0))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(10000).setVpSize(2500, 2500)
              .setResolution(500, 500)
              .setRayTracer(scene, SIMPLE)
              .build()
              .renderImage()
              .writeToImage("reflectionTwoSpheresMirrored");
   }

   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150.0, -150.0, -115.0), new Point(150.0, -150.0, -135.0), new Point(75.0, 75.0, -150.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Triangle(new Point(-150.0, -150.0, -115.0), new Point(-70.0, 70.0, -140.0), new Point(75.0, 75.0, -150.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Sphere(30d, new Point(60.0, 50.0, -50.0)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setkT(0.6))
      );
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(60.0, 50.0, 0.0), new Vector(0, 0, -1))
                      .setKl(4E-5).setKq(2E-7));

      cameraBuilder
              .setLocation(new Point(0.0, 0.0, 1000.0))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(1000).setVpSize(200, 200)
              .setResolution(600, 600)
              .build()
              .renderImage()
              .writeToImage("refractionShadow");
   }


   @Test
   void customScene() {
      scene.geometries.add(
              new Sphere(80.0, new Point(-40.0, 0.0, -100.0))
                      .setEmission(new Color(100, 100, 250))
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(80)),

              new Plane(new Point(0.0, -70.0, 0.0), new Vector(0.0, 1.0, 0.0))
                      .setEmission(new Color(80, 80, 80))
                      .setMaterial(new Material().setKD(0.5).setKS(0.1).setkR(0.3)),

              new Triangle(new Point(100.0, 0.0, -150.0), new Point(50.0, 80.0, -140.0), new Point(140.0, 70.0, -160.0))
                      .setEmission(new Color(200, 50, 50))
                      .setMaterial(new Material().setKD(0.3).setKS(0.6).setShininess(100).setkT(0.4)),

              new Sphere(40.0, new Point(70.0, 20.0, -120.0))
                      .setEmission(new Color(50, 200, 50))
                      .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(60).setkR(0.5))
      );

      scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

      scene.lights.addAll(List.of(
              new SpotLight(new Color(900, 600, 600), new Point(0.0, 200.0, 0.0), new Vector(-0.3, -1.0, -0.3))
                      .setKl(0.0001).setKq(0.00001),
              new PointLight(new Color(300, 300, 300), new Point(-100.0, 150.0, 100.0))
                      .setKl(0.0005).setKq(0.0005)
      ));

      cameraBuilder

              .setLocation(new Point(0.0, 0.0, 700.0))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(700.0).setVpSize(200.0, 200.0)
              .setResolution(600, 600)
              .build()
              .renderImage()
              .writeToImage("customTestScene");
   }

   @Test
   void bonusBigSceneBalancedLight() {
      scene.geometries.add(
              new Sphere(40.0, new Point(-120.0, 0.0, -120.0))
                      .setEmission(new Color(70, 50, 110))
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(70)),

              new Plane(new Point(0.0, -70.0, 0.0), new Vector(0.0, 1.0, 0.0))
                      .setEmission(new Color(50, 50, 50))
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setkR(0.3)),

              new Triangle(new Point(50.0, 60.0, -150.0), new Point(100.0, 150.0, -140.0), new Point(30.0, 120.0, -200.0))
                      .setEmission(new Color(150, 130, 40))
                      .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(80)),

              new Cylinder(15.0, new Ray(new Point(70.0, -70.0, -100.0), new Vector(0.0, 1.0, 0.0)), 60.0)
                      .setEmission(new Color(40, 100, 140))
                      .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(60).setkT(0.3)),

              new Tube(10.0, new Ray(new Point(-60.0, -70.0, -110.0), new Vector(0.0, 1.0, 0.0)))
                      .setEmission(new Color(160, 70, 40))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(90).setkR(0.5)),

              new Sphere(30.0, new Point(130.0, -40.0, -130.0))
                      .setEmission(new Color(130, 40, 40))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(80).setkR(0.5)),

              new Triangle(new Point(-80.0, -80.0, -140.0), new Point(-30.0, -150.0, -130.0), new Point(-60.0, -120.0, -190.0))
                      .setEmission(new Color(80, 130, 170))
                      .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(70).setkT(0.4)),

              new Plane(new Point(0.0, 0.0, -200.0), new Vector(0.0, 0.0, 1.0))
                      .setEmission(new Color(40, 40, 40))
                      .setMaterial(new Material().setKD(0.6).setKS(0.3)),

              new Sphere(35.0, new Point(0.0, 50.0, -100.0))
                      .setEmission(new Color(140, 140, 50))
                      .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(70).setkR(0.5)),

              new Tube(8.0, new Ray(new Point(0.0, -50.0, -90.0), new Vector(1.0, 1.0, 0.0)))
                      .setEmission(new Color(50, 180, 130))
                      .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(60))
      );

      scene.setAmbientLight(new AmbientLight(new Color(18, 18, 18)));

      scene.lights.clear();
      scene.lights.addAll(List.of(
              new SpotLight(new Color(700, 500, 500), new Point(0.0, 250.0, 0.0), new Vector(-0.3, -1.0, -0.2))
                      .setKl(0.00008).setKq(0.00003),
              new PointLight(new Color(350, 350, 350), new Point(-150.0, 120.0, 50.0))
                      .setKl(0.0004).setKq(0.0004),
              new DirectionalLight(new Color(120, 120, 120), new Vector(-1.0, -1.0, -1.0))
      ));

      cameraBuilder
              .setLocation(new Point(0.0, 0.0, 800.0))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(800.0).setVpSize(300.0, 300.0)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("bonusBigSceneBalancedLight");
   }

   @Test
   void bonusComplexScene() {
      scene.geometries.add(
              // 1-2. רצפה מחזירה אור
              new Triangle(new Point(-300.0, -120.0, -300.0), new Point(300.0, -120.0, -300.0),
                      new Point(300.0, -120.0, 300.0))
                      .setEmission(new Color(8.0, 8.0, 12.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setkR(0.3)),

              new Triangle(new Point(-300.0, -120.0, -300.0), new Point(-300.0, -120.0, 300.0),
                      new Point(300.0, -120.0, 300.0))
                      .setEmission(new Color(8.0, 8.0, 12.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setkR(0.3)),

              // 3-4. קיר אחורי
              new Triangle(new Point(-200.0, -120.0, -150.0), new Point(200.0, -120.0, -150.0),
                      new Point(200.0, 150.0, -150.0))
                      .setEmission(new Color(25.0, 20.0, 15.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40)),

              new Triangle(new Point(-200.0, -120.0, -150.0), new Point(-200.0, 150.0, -150.0),
                      new Point(200.0, 150.0, -150.0))
                      .setEmission(new Color(25.0, 20.0, 15.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40)),

              // 5. כדור זכוכית מרכזי
              new Sphere(30.0, new Point(0.0, -50.0, -20.0)).setEmission(new Color(5.0, 15.0, 25.0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(300).setkT(0.8)),

              // 6. כדור מתכת זהב
              new Sphere(22.0, new Point(-80.0, -70.0, 30.0)).setEmission(new Color(80.0, 70.0, 20.0))
                      .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(150).setkR(0.6)),

              // 7. כדור אדום מבריק
              new Sphere(18.0, new Point(70.0, -80.0, -10.0)).setEmission(new Color(120.0, 30.0, 30.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(100)),

              // 8. כדור ירוק קטן
              new Sphere(12.0, new Point(-40.0, -20.0, 80.0)).setEmission(new Color(20.0, 100.0, 20.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)),

              // 9. כדור כחול מרחף
              new Sphere(15.0, new Point(50.0, 20.0, 50.0)).setEmission(new Color(30.0, 50.0, 150.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(120)),

              // 10. גליל מתכתי גבוה
              new Cylinder(15.0, new Ray(new Point(-120.0, -120.0, -60.0), new Vector(0.0, 1.0, 0.0)), 100.0)
                      .setEmission(new Color(60.0, 60.0, 80.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(120).setkR(0.4)),

              // 11. גליל זכוכית
              new Cylinder(10.0, new Ray(new Point(100.0, -120.0, -40.0), new Vector(0.0, 1.0, 0.0)), 70.0)
                      .setEmission(new Color(10.0, 30.0, 50.0))
                      .setMaterial(new Material().setKD(0.2).setKS(0.4).setShininess(200).setkT(0.7)),

              // 12. גליל קטן צבעוני
              new Cylinder(8.0, new Ray(new Point(-150.0, -120.0, 50.0), new Vector(0.0, 1.0, 0.0)), 50.0)
                      .setEmission(new Color(100.0, 50.0, 100.0))
                      .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(60)),

              // 13-16. פירמידה אדומה (4 משולשים)
              new Triangle(new Point(-60.0, -120.0, 60.0), new Point(-20.0, -120.0, 60.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-20.0, -120.0, 60.0), new Point(-20.0, -120.0, 100.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-20.0, -120.0, 100.0), new Point(-60.0, -120.0, 100.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-60.0, -120.0, 100.0), new Point(-60.0, -120.0, 60.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              // 17-24. קוביה מורכבת (8 משולשים - 4 פנים)
              // פן קדמי
              new Triangle(new Point(40.0, -120.0, 100.0), new Point(80.0, -120.0, 100.0),
                      new Point(80.0, -80.0, 100.0))
                      .setEmission(new Color(40.0, 120.0, 40.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(40.0, -120.0, 100.0), new Point(40.0, -80.0, 100.0),
                      new Point(80.0, -80.0, 100.0))
                      .setEmission(new Color(40.0, 120.0, 40.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן ימני
              new Triangle(new Point(80.0, -120.0, 100.0), new Point(80.0, -120.0, 140.0),
                      new Point(80.0, -80.0, 140.0))
                      .setEmission(new Color(30.0, 100.0, 30.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(80.0, -120.0, 100.0), new Point(80.0, -80.0, 100.0),
                      new Point(80.0, -80.0, 140.0))
                      .setEmission(new Color(30.0, 100.0, 30.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן אחורי
              new Triangle(new Point(80.0, -120.0, 140.0), new Point(40.0, -120.0, 140.0),
                      new Point(40.0, -80.0, 140.0))
                      .setEmission(new Color(20.0, 80.0, 20.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(80.0, -120.0, 140.0), new Point(80.0, -80.0, 140.0),
                      new Point(40.0, -80.0, 140.0))
                      .setEmission(new Color(20.0, 80.0, 20.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן שמאלי
              new Triangle(new Point(40.0, -120.0, 140.0), new Point(40.0, -120.0, 100.0),
                      new Point(40.0, -80.0, 100.0))
                      .setEmission(new Color(35.0, 90.0, 35.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(40.0, -120.0, 140.0), new Point(40.0, -80.0, 140.0),
                      new Point(40.0, -80.0, 100.0))
                      .setEmission(new Color(35.0, 90.0, 35.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // 25. מישור מראה אלכסוני
              new Polygon(new Point(120.0, -120.0, -80.0), new Point(160.0, -120.0, -40.0),
                      new Point(140.0, -40.0, -60.0), new Point(100.0, -40.0, -100.0))
                      .setEmission(new Color(40.0, 40.0, 60.0))
                      .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(250).setkR(0.7)),

              // 26. פנל זכוכית שקוף
              new Polygon(new Point(-30.0, -60.0, 120.0), new Point(30.0, -60.0, 120.0),
                      new Point(30.0, 60.0, 120.0), new Point(-30.0, 60.0, 120.0))
                      .setEmission(new Color(5.0, 20.0, 35.0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.2).setShininess(400).setkT(0.85))
      );

      // תאורה מאוזנת
      scene.setAmbientLight(new AmbientLight(new Color(12.0, 12.0, 15.0)));

      // אור ראשי
      scene.lights.add(
              new SpotLight(new Color(400.0, 350.0, 300.0), new Point(-100.0, 100.0, 150.0),
                      new Vector(1.0, -1.0, -1.0))
                      .setKl(0.0002).setKq(0.000005)
      );

      // אור משני
      scene.lights.add(
              new PointLight(new Color(200.0, 250.0, 300.0), new Point(120.0, 80.0, 80.0))
                      .setKl(0.0004).setKq(0.00001)
      );

      cameraBuilder
              .setLocation(new Point(0.0, 50.0, 250.0))
              .setDirection(new Point(0.0, -20.0, 0.0), Vector.AXIS_Y)
              .setVpDistance(250.0).setVpSize(200.0, 200.0)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("bonusComplexScene");
   }


   @Test
   void newbonusComplexScene() {
      scene.geometries.add(
              // 1-2. רצפה מחזירה אור
              new Triangle(new Point(-300.0, -120.0, -300.0), new Point(300.0, -120.0, -300.0),
                      new Point(300.0, -120.0, 300.0))
                      .setEmission(new Color(8.0, 8.0, 12.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setkR(0.3)),

              new Triangle(new Point(-300.0, -120.0, -300.0), new Point(-300.0, -120.0, 300.0),
                      new Point(300.0, -120.0, 300.0))
                      .setEmission(new Color(8.0, 8.0, 12.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setkR(0.3)),

              // 3-4. קיר אחורי
              new Triangle(new Point(-200.0, -120.0, -150.0), new Point(200.0, -120.0, -150.0),
                      new Point(200.0, 150.0, -150.0))
                      .setEmission(new Color(25.0, 20.0, 15.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40)),

              new Triangle(new Point(-200.0, -120.0, -150.0), new Point(-200.0, 150.0, -150.0),
                      new Point(200.0, 150.0, -150.0))
                      .setEmission(new Color(25.0, 20.0, 15.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40)),

              // 5. כדור זכוכית מרכזי גדול
              new Sphere(35.0, new Point(0.0, -50.0, -20.0)).setEmission(new Color(5.0, 15.0, 25.0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(300).setkT(0.8)),

              // 6. כדור קטן בתוך הכדור הגדול - גוף בתוך גוף!
              new Sphere(15.0, new Point(0.0, -50.0, -20.0)).setEmission(new Color(255.0, 100.0, 50.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(200)),

              // 7. כדור מתכת זהב עם כדור פנימי
              new Sphere(22.0, new Point(-80.0, -70.0, 30.0)).setEmission(new Color(80.0, 70.0, 20.0))
                      .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(150).setkR(0.6)),

              // 8. כדור זעיר בתוך הכדור הזהב
              new Sphere(8.0, new Point(-80.0, -70.0, 30.0)).setEmission(new Color(200.0, 50.0, 200.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(100)),

              // 9. כדור אדום מבריק
              new Sphere(18.0, new Point(70.0, -80.0, -10.0)).setEmission(new Color(120.0, 30.0, 30.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(100)),

              // 10. כדור ירוק קטן
              new Sphere(12.0, new Point(-40.0, -20.0, 80.0)).setEmission(new Color(20.0, 100.0, 20.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)),

              // 11. כדור כחול מרחף
              new Sphere(15.0, new Point(50.0, 20.0, 50.0)).setEmission(new Color(30.0, 50.0, 150.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(120)),

              // 12. גליל מתכתי גבוה
              new Cylinder(15.0, new Ray(new Point(-120.0, -120.0, -60.0), new Vector(0.0, 1.0, 0.0)), 100.0)
                      .setEmission(new Color(60.0, 60.0, 80.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(120).setkR(0.4)),

              // 13. גליל דק בתוך הגליל הגדול - גוף בתוך גוף!
              new Cylinder(6.0, new Ray(new Point(-120.0, -120.0, -60.0), new Vector(0.0, 1.0, 0.0)), 80.0)
                      .setEmission(new Color(255.0, 200.0, 100.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(150)),

              // 14. גליל זכוכית
              new Cylinder(10.0, new Ray(new Point(100.0, -120.0, -40.0), new Vector(0.0, 1.0, 0.0)), 70.0)
                      .setEmission(new Color(10.0, 30.0, 50.0))
                      .setMaterial(new Material().setKD(0.2).setKS(0.4).setShininess(200).setkT(0.7)),

              // 15. גליל קטן צבעוני
              new Cylinder(8.0, new Ray(new Point(-150.0, -120.0, 50.0), new Vector(0.0, 1.0, 0.0)), 50.0)
                      .setEmission(new Color(100.0, 50.0, 100.0))
                      .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(60)),

              // 16-19. פירמידה אדומה (4 משולשים)
              new Triangle(new Point(-60.0, -120.0, 60.0), new Point(-20.0, -120.0, 60.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-20.0, -120.0, 60.0), new Point(-20.0, -120.0, 100.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-20.0, -120.0, 100.0), new Point(-60.0, -120.0, 100.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              new Triangle(new Point(-60.0, -120.0, 100.0), new Point(-60.0, -120.0, 60.0),
                      new Point(-40.0, -70.0, 80.0))
                      .setEmission(new Color(150.0, 40.0, 40.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)),

              // 20. כדור קטן בתוך הפירמידה - גוף בתוך גוף!
              new Sphere(8.0, new Point(-40.0, -85.0, 80.0)).setEmission(new Color(100.0, 255.0, 100.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(120)),

              // 21-28. קוביה מורכבת (8 משולשים - 4 פנים)
              // פן קדמי
              new Triangle(new Point(40.0, -120.0, 100.0), new Point(80.0, -120.0, 100.0),
                      new Point(80.0, -80.0, 100.0))
                      .setEmission(new Color(40.0, 120.0, 40.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(40.0, -120.0, 100.0), new Point(40.0, -80.0, 100.0),
                      new Point(80.0, -80.0, 100.0))
                      .setEmission(new Color(40.0, 120.0, 40.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן ימני
              new Triangle(new Point(80.0, -120.0, 100.0), new Point(80.0, -120.0, 140.0),
                      new Point(80.0, -80.0, 140.0))
                      .setEmission(new Color(30.0, 100.0, 30.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(80.0, -120.0, 100.0), new Point(80.0, -80.0, 100.0),
                      new Point(80.0, -80.0, 140.0))
                      .setEmission(new Color(30.0, 100.0, 30.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן אחורי
              new Triangle(new Point(80.0, -120.0, 140.0), new Point(40.0, -120.0, 140.0),
                      new Point(40.0, -80.0, 140.0))
                      .setEmission(new Color(20.0, 80.0, 20.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(80.0, -120.0, 140.0), new Point(80.0, -80.0, 140.0),
                      new Point(40.0, -80.0, 140.0))
                      .setEmission(new Color(20.0, 80.0, 20.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // פן שמאלי
              new Triangle(new Point(40.0, -120.0, 140.0), new Point(40.0, -120.0, 100.0),
                      new Point(40.0, -80.0, 100.0))
                      .setEmission(new Color(35.0, 90.0, 35.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),
              new Triangle(new Point(40.0, -120.0, 140.0), new Point(40.0, -80.0, 140.0),
                      new Point(40.0, -80.0, 100.0))
                      .setEmission(new Color(35.0, 90.0, 35.0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90)),

              // 29. גליל קטן בתוך הקוביה - גוף בתוך גוף!
              new Cylinder(8.0, new Ray(new Point(60.0, -120.0, 120.0), new Vector(0.0, 1.0, 0.0)), 30.0)
                      .setEmission(new Color(255.0, 255.0, 0.0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(180)),

              // 30. מישור מראה אלכסוני
              new Polygon(new Point(120.0, -120.0, -80.0), new Point(160.0, -120.0, -40.0),
                      new Point(140.0, -40.0, -60.0), new Point(100.0, -40.0, -100.0))
                      .setEmission(new Color(40.0, 40.0, 60.0))
                      .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(250).setkR(0.7)),

              // 31. פנל זכוכית שקוף
              new Polygon(new Point(-30.0, -60.0, 120.0), new Point(30.0, -60.0, 120.0),
                      new Point(30.0, 60.0, 120.0), new Point(-30.0, 60.0, 120.0))
                      .setEmission(new Color(5.0, 20.0, 35.0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.2).setShininess(400).setkT(0.85)),

              // 32. מישור קטן בתוך הפנל הזכוכית - גוף בתוך גוף!
              new Polygon(new Point(-10.0, -20.0, 119.5), new Point(10.0, -20.0, 119.5),
                      new Point(10.0, 20.0, 119.5), new Point(-10.0, 20.0, 119.5))
                      .setEmission(new Color(200.0, 100.0, 255.0))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(100))
      );

      // תאורה מאוזנת
      scene.setAmbientLight(new AmbientLight(new Color(12.0, 12.0, 15.0)));

      // אור ראשי
      scene.lights.add(
              new SpotLight(new Color(400.0, 350.0, 300.0), new Point(-100.0, 100.0, 150.0),
                      new Vector(1.0, -1.0, -1.0))
                      .setKl(0.0002).setKq(0.000005)
      );

      // אור משני
      scene.lights.add(
              new PointLight(new Color(200.0, 250.0, 300.0), new Point(120.0, 80.0, 80.0))
                      .setKl(0.0004).setKq(0.00001)
      );

      cameraBuilder
              .setLocation(new Point(0.0, 50.0, 250.0))
              .setDirection(new Point(0.0, -20.0, 0.0), Vector.AXIS_Y)
              .setVpDistance(250.0).setVpSize(200.0, 200.0)
              .setResolution(800, 800)
              .setRayTracer(scene, GRID)
              .build()
              .renderImage()
              .writeToImage("new4bonusComplexScene");
   }
}