package Special;

import org.junit.jupiter.api.Test;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;
import geometries.*;

public class BonusSceneTests {

    private Camera.Builder cameraBuilder = Camera.getBuilder();

    private Scene createBigColorfulScene() {
        Scene scene = new Scene("Big Colorful Scene 1000+");

        // --- מישור בסיס ---
        scene.geometries.add(
                new Plane(new Point(0.0, -140.0, 0.0), new Vector(0.0, 1.0, 0.0))
                        .setEmission(new Color(30, 30, 35))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(60).setkR(0.2))
        );

        // --- 1. ספירות (10x10x5 = 500) ---
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                for (int y = 0; y < 5; y++) {
                    int r = clampColor(30 + 20 * (x + 5));
                    int g = clampColor(30 + 20 * y);
                    int b = clampColor(30 + 20 * (z + 5));

                    Material mat = new Material().setKD(0.4).setKS(0.3).setShininess(50);
                    if (y == 1) mat.setkT(0.6);    // שקיפות
                    if (y == 2) mat.setkR(0.5);    // השתקפות
                    // לא גם וגם בו זמנית, כי y=1 או y=2 בלבד

                    scene.geometries.add(
                            new Sphere(3.0, new Point(x * 12.0, -137.0 + y * 8.0, z * 12.0))
                                    .setEmission(new Color(r, g, b))
                                    .setMaterial(mat)
                    );
                }
            }
        }

        // --- 2. גלילים (6x6x4 = 144) ---
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                for (int h = 0; h < 4; h++) {
                    int r = clampColor(50 + 40 * (x + 3));
                    int g = clampColor(50 + 40 * h);
                    int b = clampColor(50 + 40 * (z + 3));

                    Material mat = new Material().setKD(0.5).setKS(0.4).setShininess(70);
                    if (h == 1) mat.setkT(0.7);
                    if (h == 2) mat.setkR(0.4);

                    scene.geometries.add(
                            new Cylinder(4.0, new Ray(new Point(x * 20.0, -140.0, z * 20.0), new Vector(0.0, 1.0, 0.0)), 20.0 + h * 10.0)
                                    .setEmission(new Color(r, g, b))
                                    .setMaterial(mat)
                    );
                }
            }
        }

        // --- 3. טיובים (Tube) (7x7 = 49) ---
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                int r = clampColor(80 + 30 * (x + 3));
                int g = clampColor(80 + 30 * (z + 3));
                int b = 200;

                scene.geometries.add(
                        new Tube(2.5, new Ray(new Point(x * 18.0, -140.0, z * 18.0), new Vector(0.0, 1.0, 0.7)))
                                .setEmission(new Color(r, g, b))
                                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(90))
                );
            }
        }

        // --- 4. פירמידות (8 פירמידות * 4 משולשים כל אחת = 32 משולשים) ---
        Point[][] pyramidBases = {
                {new Point(40.0, -140.0, 40.0), new Point(60.0, -140.0, 40.0), new Point(60.0, -140.0, 60.0), new Point(40.0, -140.0, 60.0)},
                {new Point(-60.0, -140.0, -40.0), new Point(-40.0, -140.0, -40.0), new Point(-40.0, -140.0, -60.0), new Point(-60.0, -140.0, -60.0)},
                {new Point(-60.0, -140.0, 40.0), new Point(-40.0, -140.0, 40.0), new Point(-40.0, -140.0, 60.0), new Point(-60.0, -140.0, 60.0)},
                {new Point(40.0, -140.0, -40.0), new Point(60.0, -140.0, -40.0), new Point(60.0, -140.0, -60.0), new Point(40.0, -140.0, -60.0)},
                {new Point(0.0, -140.0, 80.0), new Point(20.0, -140.0, 80.0), new Point(20.0, -140.0, 100.0), new Point(0.0, -140.0, 100.0)},
                {new Point(-20.0, -140.0, -80.0), new Point(0.0, -140.0, -80.0), new Point(0.0, -140.0, -100.0), new Point(-20.0, -140.0, -100.0)},
                {new Point(80.0, -140.0, 0.0), new Point(100.0, -140.0, 0.0), new Point(100.0, -140.0, 20.0), new Point(80.0, -140.0, 20.0)},
                {new Point(-100.0, -140.0, 0.0), new Point(-80.0, -140.0, 0.0), new Point(-80.0, -140.0, 20.0), new Point(-100.0, -140.0, 20.0)}
        };
        Point[] pyramidPeaks = {
                new Point(50.0, -110.0, 50.0),
                new Point(-50.0, -110.0, -50.0),
                new Point(-50.0, -110.0, 50.0),
                new Point(50.0, -110.0, -50.0),
                new Point(10.0, -110.0, 90.0),
                new Point(-10.0, -110.0, -90.0),
                new Point(90.0, -110.0, 10.0),
                new Point(-90.0, -110.0, 10.0)
        };

        for (int i = 0; i < pyramidBases.length; i++) {
            Point[] base = pyramidBases[i];
            Point peak = pyramidPeaks[i];
            Color baseColor = new Color(clampColor(150 - i * 15), clampColor(40 + i * 10), clampColor(40 + i * 15));

            scene.geometries.add(new Triangle(base[0], base[1], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            scene.geometries.add(new Triangle(base[1], base[2], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            scene.geometries.add(new Triangle(base[2], base[3], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            scene.geometries.add(new Triangle(base[3], base[0], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));
        }

        // --- 5. קוביות (15 קוביות * 12 משולשים = 180 משולשים) ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                double baseX = i * 15.0;
                double baseY = -140.0 + j * 12.0;
                double baseZ = 80.0;

                Point p000 = new Point(baseX, baseY, baseZ);
                Point p001 = new Point(baseX, baseY, baseZ + 12.0);
                Point p010 = new Point(baseX, baseY + 12.0, baseZ);
                Point p011 = new Point(baseX, baseY + 12.0, baseZ + 12.0);
                Point p100 = new Point(baseX + 12.0, baseY, baseZ);
                Point p101 = new Point(baseX + 12.0, baseY, baseZ + 12.0);
                Point p110 = new Point(baseX + 12.0, baseY + 12.0, baseZ);
                Point p111 = new Point(baseX + 12.0, baseY + 12.0, baseZ + 12.0);

                Color cubeColor = new Color(clampColor(50 + 10 * i), clampColor(100 + 10 * j), 150);

                scene.geometries.add(new Triangle(p000, p001, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p000, p101, p100).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                scene.geometries.add(new Triangle(p100, p101, p111).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p100, p111, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                scene.geometries.add(new Triangle(p110, p111, p011).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p110, p011, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                scene.geometries.add(new Triangle(p010, p011, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p010, p001, p000).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                scene.geometries.add(new Triangle(p011, p111, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p011, p101, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                scene.geometries.add(new Triangle(p000, p100, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                scene.geometries.add(new Triangle(p000, p110, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
            }
        }

        // --- 6. ספירות קטנות במרכזי הקוביות (15*2=30) ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                Point center = new Point(i * 15.0 + 6.0, -140.0 + j * 12.0 + 6.0, 80.0 + 6.0);
                scene.geometries.add(
                        new Sphere(1.5, center)
                                .setEmission(new Color(200, 50, 50))
                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90).setkR(0.3))
                );
            }
        }

        // --- 7. תאורות ---


        scene.lights.add(
                new SpotLight(new Color(600, 500, 500), new Point(-100.0, 120.0, 120.0), new Vector(1.0, -1.0, -1.0))
                        .setKl(0.00015).setKq(0.000005)
        );

        scene.lights.add(
                new DirectionalLight(new Color(150, 150, 255), new Vector(1.0, -0.8, -0.3))
        );

        scene.lights.add(
                new PointLight(new Color(200, 220, 255), new Point(0.0, 80.0, 140.0))
                        .setKl(0.0004).setKq(0.00001)
        );

        return scene;
    }

    // בדיקת גבול חוקי לצבעים (0..255)
    private int clampColor(int val) {
        if (val < 0) return 0;
        if (val > 255) return 255;
        return val;
    }

    private Camera.Builder baseCamera(Scene scene) {
        return Camera.getBuilder()
                .setLocation(new Point(0.0, 40.0, 250.0))
                .setDirection(new Point(0.0, -10.0, 0.0), Vector.AXIS_Y)
                .setVpDistance(250.0).setVpSize(220.0, 220.0)
                .setResolution(900, 900)
                .setRayTracer(scene, RayTracerType.SIMPLE);
    }

    @Test
    void testBigSceneBaseline() {
        Scene scene = createBigColorfulScene();
        baseCamera(scene)
                .build()
                .renderImage()
                .writeToImage("bigColorfulScene_baseline");
    }

    @Test
    void testBigSceneWithCBR() {
        Scene scene = createBigColorfulScene();
        baseCamera(scene)
                .enableCBR()
                .build()
                .renderImage()
                .writeToImage("bigColorfulScene_withCBR");
    }

    @Test
    void testBigSceneWithMultithreading() {
        Scene scene = createBigColorfulScene();
        baseCamera(scene)
                .setMultithreading(3)
                .build()
                .renderImage()
                .writeToImage("bigColorfulScene_withMultithreading");
    }

    @Test
    void testBigSceneWithCBRAndMultithreading() {
        Scene scene = createBigColorfulScene();
        baseCamera(scene)
                .setMultithreading(3)
                .enableCBR()
                .build()
                .renderImage()
                .writeToImage("bigColorfulScene_withCBR_and_Multithreading");
    }



/*BVH*/

    @Test
    void testBigSceneWithManualBVH() {
        Scene scene = new Scene("Big Colorful Scene with Manual BVH");

        Geometries allSpheres = new Geometries();
        Geometries allCylinders = new Geometries();
        Geometries allTubes = new Geometries();
        Geometries allTriangles = new Geometries();
        Geometries allCubesTriangles = new Geometries();

        // --- מישור בסיס ---
        Plane basePlane = (Plane) new Plane(new Point(0.0, -140.0, 0.0), new Vector(0.0, 1.0, 0.0))
                .setEmission(new Color(30, 30, 35))
                .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(60).setkR(0.2));
        scene.geometries.add(basePlane);

        // --- ספירות ---
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                for (int y = 0; y < 5; y++) {
                    int r = clampColor(30 + 20 * (x + 5));
                    int g = clampColor(30 + 20 * y);
                    int b = clampColor(30 + 20 * (z + 5));

                    Material mat = new Material().setKD(0.4).setKS(0.3).setShininess(50);
                    if (y == 1) mat.setkT(0.6);    // שקיפות
                    if (y == 2) mat.setkR(0.5);    // השתקפות

                    Sphere s = (Sphere) new Sphere(3.0, new Point(x * 12.0, -137.0 + y * 8.0, z * 12.0))
                            .setEmission(new Color(r, g, b))
                            .setMaterial(mat);
                    allSpheres.add(s);
                }
            }
        }

        // --- גלילים ---
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                for (int h = 0; h < 4; h++) {
                    int r = clampColor(50 + 40 * (x + 3));
                    int g = clampColor(50 + 40 * h);
                    int b = clampColor(50 + 40 * (z + 3));

                    Material mat = new Material().setKD(0.5).setKS(0.4).setShininess(70);
                    if (h == 1) mat.setkT(0.7);
                    if (h == 2) mat.setkR(0.4);

                    Cylinder c = (Cylinder) new Cylinder(4.0, new Ray(new Point(x * 20.0, -140.0, z * 20.0), new Vector(0.0, 1.0, 0.0)), 20.0 + h * 10.0)
                            .setEmission(new Color(r, g, b))
                            .setMaterial(mat);
                    allCylinders.add(c);
                }
            }
        }

        // --- טיובים ---
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                int r = clampColor(80 + 30 * (x + 3));
                int g = clampColor(80 + 30 * (z + 3));
                int b = 200;

                Tube tube = (Tube) new Tube(2.5, new Ray(new Point(x * 18.0, -140.0, z * 18.0), new Vector(0.0, 1.0, 0.7)))
                        .setEmission(new Color(r, g, b))
                        .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(90));
                allTubes.add(tube);
            }
        }

        // --- פירמידות (משולשים) ---
        Point[][] pyramidBases = {
                {new Point(40.0, -140.0, 40.0), new Point(60.0, -140.0, 40.0), new Point(60.0, -140.0, 60.0), new Point(40.0, -140.0, 60.0)},
                {new Point(-60.0, -140.0, -40.0), new Point(-40.0, -140.0, -40.0), new Point(-40.0, -140.0, -60.0), new Point(-60.0, -140.0, -60.0)},
                {new Point(-60.0, -140.0, 40.0), new Point(-40.0, -140.0, 40.0), new Point(-40.0, -140.0, 60.0), new Point(-60.0, -140.0, 60.0)},
                {new Point(40.0, -140.0, -40.0), new Point(60.0, -140.0, -40.0), new Point(60.0, -140.0, -60.0), new Point(40.0, -140.0, -60.0)},
                {new Point(0.0, -140.0, 80.0), new Point(20.0, -140.0, 80.0), new Point(20.0, -140.0, 100.0), new Point(0.0, -140.0, 100.0)},
                {new Point(-20.0, -140.0, -80.0), new Point(0.0, -140.0, -80.0), new Point(0.0, -140.0, -100.0), new Point(-20.0, -140.0, -100.0)},
                {new Point(80.0, -140.0, 0.0), new Point(100.0, -140.0, 0.0), new Point(100.0, -140.0, 20.0), new Point(80.0, -140.0, 20.0)},
                {new Point(-100.0, -140.0, 0.0), new Point(-80.0, -140.0, 0.0), new Point(-80.0, -140.0, 20.0), new Point(-100.0, -140.0, 20.0)}
        };
        Point[] pyramidPeaks = {
                new Point(50.0, -110.0, 50.0),
                new Point(-50.0, -110.0, -50.0),
                new Point(-50.0, -110.0, 50.0),
                new Point(50.0, -110.0, -50.0),
                new Point(10.0, -110.0, 90.0),
                new Point(-10.0, -110.0, -90.0),
                new Point(90.0, -110.0, 10.0),
                new Point(-90.0, -110.0, 10.0)
        };

        for (int i = 0; i < pyramidBases.length; i++) {
            Point[] base = pyramidBases[i];
            Point peak = pyramidPeaks[i];
            Color baseColor = new Color(clampColor(150 - i * 15), clampColor(40 + i * 10), clampColor(40 + i * 15));

            allTriangles.add(new Triangle(base[0], base[1], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[1], base[2], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[2], base[3], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[3], base[0], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));
        }

        // --- קוביות (כל קוביה היא 12 משולשים, יש 15 קוביות * 2 שורות = 30 קוביות, סה"כ 360 משולשים) ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                double baseX = i * 15.0;
                double baseY = -140.0 + j * 12.0;
                double baseZ = 80.0;

                Point p000 = new Point(baseX, baseY, baseZ);
                Point p001 = new Point(baseX, baseY, baseZ + 12.0);
                Point p010 = new Point(baseX, baseY + 12.0, baseZ);
                Point p011 = new Point(baseX, baseY + 12.0, baseZ + 12.0);
                Point p100 = new Point(baseX + 12.0, baseY, baseZ);
                Point p101 = new Point(baseX + 12.0, baseY, baseZ + 12.0);
                Point p110 = new Point(baseX + 12.0, baseY + 12.0, baseZ);
                Point p111 = new Point(baseX + 12.0, baseY + 12.0, baseZ + 12.0);

                Color cubeColor = new Color(clampColor(50 + 10 * i), clampColor(100 + 10 * j), 150);

                allCubesTriangles.add(new Triangle(p000, p001, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p000, p101, p100).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p100, p101, p111).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p100, p111, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p110, p111, p011).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p110, p011, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p010, p011, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p010, p001, p000).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p011, p111, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p011, p101, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p000, p100, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p000, p110, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
            }
        }

        // --- ספירות קטנות במרכזי הקוביות ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                Point center = new Point(i * 15.0 + 6.0, -140.0 + j * 12.0 + 6.0, 80.0 + 6.0);
                Sphere smallSphere = (Sphere) new Sphere(1.5, center)
                        .setEmission(new Color(200, 50, 50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90).setkR(0.3));
                allSpheres.add(smallSphere);
            }
        }

        // --- בניית ההיררכיה ---

        Geometries groupSpheres = new Geometries(allSpheres);
        Geometries groupCylinders = new Geometries(allCylinders);
        Geometries groupTubes = new Geometries(allTubes);
        Geometries groupTriangles = new Geometries(allTriangles);
        Geometries groupCubes = new Geometries(allCubesTriangles);

        Geometries rootGroup = new Geometries(groupSpheres, groupCylinders, groupTubes, groupTriangles, groupCubes);

        scene.setGeometries(rootGroup);

        // --- תאורה ---

        scene.lights.add(new SpotLight(new Color(600, 500, 500), new Point(-100.0, 120.0, 120.0), new Vector(1.0, -1.0, -1.0))
                .setKl(0.00015).setKq(0.000005));

        scene.lights.add(new DirectionalLight(new Color(150, 150, 255), new Vector(1.0, -0.8, -0.3)));

        scene.lights.add(new PointLight(new Color(200, 220, 255), new Point(0.0, 80.0, 140.0))
                .setKl(0.0004).setKq(0.00001));

        // --- מצלמה והרצה ---

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0.0, 40.0, 250.0))
                .setDirection(new Point(0.0, -10.0, 0.0), Vector.AXIS_Y)
                .setVpDistance(250.0).setVpSize(220.0, 220.0)
                .setResolution(900, 900)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage().writeToImage("bigColorfulScene_manualBVH");
    }


    /*BVH*/

    @Test
    void testBigSceneWithManualBVHMT() {
        Scene scene = new Scene("Big Colorful Scene with Manual BVH and multithreading");

        Geometries allSpheres = new Geometries();
        Geometries allCylinders = new Geometries();
        Geometries allTubes = new Geometries();
        Geometries allTriangles = new Geometries();
        Geometries allCubesTriangles = new Geometries();

        // --- מישור בסיס ---
        Plane basePlane = (Plane) new Plane(new Point(0.0, -140.0, 0.0), new Vector(0.0, 1.0, 0.0))
                .setEmission(new Color(30, 30, 35))
                .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(60).setkR(0.2));
        scene.geometries.add(basePlane);

        // --- ספירות ---
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                for (int y = 0; y < 5; y++) {
                    int r = clampColor(30 + 20 * (x + 5));
                    int g = clampColor(30 + 20 * y);
                    int b = clampColor(30 + 20 * (z + 5));

                    Material mat = new Material().setKD(0.4).setKS(0.3).setShininess(50);
                    if (y == 1) mat.setkT(0.6);    // שקיפות
                    if (y == 2) mat.setkR(0.5);    // השתקפות

                    Sphere s = (Sphere) new Sphere(3.0, new Point(x * 12.0, -137.0 + y * 8.0, z * 12.0))
                            .setEmission(new Color(r, g, b))
                            .setMaterial(mat);
                    allSpheres.add(s);
                }
            }
        }

        // --- גלילים ---
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                for (int h = 0; h < 4; h++) {
                    int r = clampColor(50 + 40 * (x + 3));
                    int g = clampColor(50 + 40 * h);
                    int b = clampColor(50 + 40 * (z + 3));

                    Material mat = new Material().setKD(0.5).setKS(0.4).setShininess(70);
                    if (h == 1) mat.setkT(0.7);
                    if (h == 2) mat.setkR(0.4);

                    Cylinder c = (Cylinder) new Cylinder(4.0, new Ray(new Point(x * 20.0, -140.0, z * 20.0), new Vector(0.0, 1.0, 0.0)), 20.0 + h * 10.0)
                            .setEmission(new Color(r, g, b))
                            .setMaterial(mat);
                    allCylinders.add(c);
                }
            }
        }

        // --- טיובים ---
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                int r = clampColor(80 + 30 * (x + 3));
                int g = clampColor(80 + 30 * (z + 3));
                int b = 200;

                Tube tube = (Tube) new Tube(2.5, new Ray(new Point(x * 18.0, -140.0, z * 18.0), new Vector(0.0, 1.0, 0.7)))
                        .setEmission(new Color(r, g, b))
                        .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(90));
                allTubes.add(tube);
            }
        }

        // --- פירמידות (משולשים) ---
        Point[][] pyramidBases = {
                {new Point(40.0, -140.0, 40.0), new Point(60.0, -140.0, 40.0), new Point(60.0, -140.0, 60.0), new Point(40.0, -140.0, 60.0)},
                {new Point(-60.0, -140.0, -40.0), new Point(-40.0, -140.0, -40.0), new Point(-40.0, -140.0, -60.0), new Point(-60.0, -140.0, -60.0)},
                {new Point(-60.0, -140.0, 40.0), new Point(-40.0, -140.0, 40.0), new Point(-40.0, -140.0, 60.0), new Point(-60.0, -140.0, 60.0)},
                {new Point(40.0, -140.0, -40.0), new Point(60.0, -140.0, -40.0), new Point(60.0, -140.0, -60.0), new Point(40.0, -140.0, -60.0)},
                {new Point(0.0, -140.0, 80.0), new Point(20.0, -140.0, 80.0), new Point(20.0, -140.0, 100.0), new Point(0.0, -140.0, 100.0)},
                {new Point(-20.0, -140.0, -80.0), new Point(0.0, -140.0, -80.0), new Point(0.0, -140.0, -100.0), new Point(-20.0, -140.0, -100.0)},
                {new Point(80.0, -140.0, 0.0), new Point(100.0, -140.0, 0.0), new Point(100.0, -140.0, 20.0), new Point(80.0, -140.0, 20.0)},
                {new Point(-100.0, -140.0, 0.0), new Point(-80.0, -140.0, 0.0), new Point(-80.0, -140.0, 20.0), new Point(-100.0, -140.0, 20.0)}
        };
        Point[] pyramidPeaks = {
                new Point(50.0, -110.0, 50.0),
                new Point(-50.0, -110.0, -50.0),
                new Point(-50.0, -110.0, 50.0),
                new Point(50.0, -110.0, -50.0),
                new Point(10.0, -110.0, 90.0),
                new Point(-10.0, -110.0, -90.0),
                new Point(90.0, -110.0, 10.0),
                new Point(-90.0, -110.0, 10.0)
        };

        for (int i = 0; i < pyramidBases.length; i++) {
            Point[] base = pyramidBases[i];
            Point peak = pyramidPeaks[i];
            Color baseColor = new Color(clampColor(150 - i * 15), clampColor(40 + i * 10), clampColor(40 + i * 15));

            allTriangles.add(new Triangle(base[0], base[1], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[1], base[2], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[2], base[3], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

            allTriangles.add(new Triangle(base[3], base[0], peak)
                    .setEmission(baseColor)
                    .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));
        }

        // --- קוביות (כל קוביה היא 12 משולשים, יש 15 קוביות * 2 שורות = 30 קוביות, סה"כ 360 משולשים) ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                double baseX = i * 15.0;
                double baseY = -140.0 + j * 12.0;
                double baseZ = 80.0;

                Point p000 = new Point(baseX, baseY, baseZ);
                Point p001 = new Point(baseX, baseY, baseZ + 12.0);
                Point p010 = new Point(baseX, baseY + 12.0, baseZ);
                Point p011 = new Point(baseX, baseY + 12.0, baseZ + 12.0);
                Point p100 = new Point(baseX + 12.0, baseY, baseZ);
                Point p101 = new Point(baseX + 12.0, baseY, baseZ + 12.0);
                Point p110 = new Point(baseX + 12.0, baseY + 12.0, baseZ);
                Point p111 = new Point(baseX + 12.0, baseY + 12.0, baseZ + 12.0);

                Color cubeColor = new Color(clampColor(50 + 10 * i), clampColor(100 + 10 * j), 150);

                allCubesTriangles.add(new Triangle(p000, p001, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p000, p101, p100).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p100, p101, p111).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p100, p111, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p110, p111, p011).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p110, p011, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p010, p011, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p010, p001, p000).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p011, p111, p101).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p011, p101, p001).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));

                allCubesTriangles.add(new Triangle(p000, p100, p110).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
                allCubesTriangles.add(new Triangle(p000, p110, p010).setEmission(cubeColor).setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(70)));
            }
        }

        // --- ספירות קטנות במרכזי הקוביות ---
        for (int i = -7; i < 8; i++) {
            for (int j = -1; j < 1; j++) {
                Point center = new Point(i * 15.0 + 6.0, -140.0 + j * 12.0 + 6.0, 80.0 + 6.0);
                Sphere smallSphere = (Sphere) new Sphere(1.5, center)
                        .setEmission(new Color(200, 50, 50))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(90).setkR(0.3));
                allSpheres.add(smallSphere);
            }
        }

        // --- בניית ההיררכיה ---

        Geometries groupSpheres = new Geometries(allSpheres);
        Geometries groupCylinders = new Geometries(allCylinders);
        Geometries groupTubes = new Geometries(allTubes);
        Geometries groupTriangles = new Geometries(allTriangles);
        Geometries groupCubes = new Geometries(allCubesTriangles);

        Geometries rootGroup = new Geometries(groupSpheres, groupCylinders, groupTubes, groupTriangles, groupCubes);

        scene.setGeometries(rootGroup);

        // --- תאורה ---

        scene.lights.add(new SpotLight(new Color(600, 500, 500), new Point(-100.0, 120.0, 120.0), new Vector(1.0, -1.0, -1.0))
                .setKl(0.00015).setKq(0.000005));

        scene.lights.add(new DirectionalLight(new Color(150, 150, 255), new Vector(1.0, -0.8, -0.3)));

        scene.lights.add(new PointLight(new Color(200, 220, 255), new Point(0.0, 80.0, 140.0))
                .setKl(0.0004).setKq(0.00001));

        // --- מצלמה והרצה ---

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0.0, 40.0, 250.0))
                .setDirection(new Point(0.0, -10.0, 0.0), Vector.AXIS_Y)
                .setVpDistance(250.0).setVpSize(220.0, 220.0)
                .setResolution(900, 900)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(3)
                .build();

        camera.renderImage().writeToImage("bigColorfulScene_manualBVH_MT");
    }


}
