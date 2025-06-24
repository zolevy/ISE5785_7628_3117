package renderer;

import primitives.Point2D;
import renderer.SimpleRayTracer;
import scene.*;
import primitives.*;
import renderer.Blackboard;

import java.util.List;

/**
 * A ray tracer that uses grid sampling for anti-aliasing.
 * <p>
 * This class extends {@link SimpleRayTracer} and overrides the ray tracing per pixel
 * by tracing multiple rays through sub-pixel sample points, arranged in a uniform grid.
 */
public class GridRayTracer extends SimpleRayTracer {
    private final Blackboard blackboard;     // Holds a list of 2D sampling points inside a pixel
    private final Vector xAxis, yAxis;       // Orthonormal vectors defining the target plane orientation
    private final List<Point2D> points2D;

    /**
     * Constructs a {@code GridRayTracer} that traces multiple rays through each pixel
     * using a regular grid of sample points.
     *
     * @param scene       the scene to render
     * @param pixelLength the size of a pixel in world units
     * @param xAxis       the X-direction vector of the view plane
     * @param yAxis       the Y-direction vector of the view plane
     */
    public GridRayTracer(Scene scene, double pixelLength, Vector xAxis, Vector yAxis) {
        super(scene);
        int gridSize = 9;
        this.blackboard = new Blackboard(pixelLength, gridSize); // Generates a 2D grid of sample points
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.points2D = blackboard.getPoints();
    }

    /**
     * Traces multiple rays through a single pixel using 2D grid sampling,
     * and returns the averaged color.
     *
     * @param cameraPos the position of the camera
     * @param camera    the camera object
     * @param nX        number of pixels in the X direction
     * @param nY        number of pixels in the Y direction
     * @param column    the column index of the pixel
     * @param row       the row index of the pixel
     * @return the averaged {@link Color} from all subpixel rays
     */
    public Color traceRayGrid(Point cameraPos, Camera camera, int nX, int nY, int column, int row) {
        Point origin3D = camera.pixelPointCalc(nX, nY, column, row);
        if (points2D == null || points2D.isEmpty()) {
            return Color.BLACK;
        }

        Color colorSum = Color.BLACK;

        // Loop over all 2D sample points
        for (Point2D p2d : points2D) {
            Vector xOffset = Util.isZero(p2d.getX()) ? null : xAxis.scale(p2d.getX());
            Vector yOffset = Util.isZero(p2d.getY()) ? null : yAxis.scale(p2d.getY());
            Point point3D = origin3D;
            if (xOffset != null) point3D = point3D.add(xOffset);
            if (yOffset != null) point3D = point3D.add(yOffset);

            // Create a ray from the camera through this 3D point
            Vector direction = point3D.subtract(cameraPos).normalize();
            Ray ray = new Ray(cameraPos, direction);

            // Trace the ray using the parent class and accumulate the color
            Color c = super.traceRay(ray);
            colorSum = colorSum.add(c);
        }

        // Return the average color from all rays
        return colorSum.reduce(points2D.size());
    }
}
