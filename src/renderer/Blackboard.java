package renderer;
import primitives.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Blackboard class generates a grid of 2D sampling points within a pixel.
 * It can be reused for all rendering improvements that require 2D sample distributions.
 */
public class Blackboard {
    private final List<Point2D> points;
    private final double pixelLength;
    private final int gridSize; // e.g., 4 for 4x4 = 16 samples

    /**
     * Constructs a Blackboard with a uniform grid of 2D points inside a pixel.
     *
     * @param pixelLength the size (width/height) of the pixel in world units
     * @param gridSize number of rows/columns in the grid (e.g., 4 means 4x4)
     */
    public Blackboard(double pixelLength, int gridSize) {
        this.pixelLength = pixelLength;
        this.gridSize = gridSize;
        this.points = generateGridPoints();
    }

    /**
     * @return list of 2D sampling points (centered within each sub-cell)
     */
    public List<Point2D> getPoints() {
        return Collections.unmodifiableList(points);
    }

    private List<Point2D> generateGridPoints() {
        List<Point2D> result = new ArrayList<>(gridSize * gridSize);
        double step = pixelLength / gridSize;
        double halfPixel = pixelLength / 2;

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                double x = -halfPixel + (col + 0.5) * step;
                double y = -halfPixel + (row + 0.5) * step;
                result.add(new Point2D(x, y));
            }
        }

        return result;
    }
}
