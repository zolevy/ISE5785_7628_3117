package geometries;

import primitives.*;
import primitives.Vector;

import java.util.*;

/**
 * Represents a node in a Bounding Volume Hierarchy (BVH) tree.
 * Each node can be either a leaf containing geometries or an internal node with child nodes.
 */
public class BVHNode extends Intersectable {

    // The bounding box that contains all geometries in this node
    private final AABB box;

    // Child nodes (null for leaf nodes)
    private final BVHNode left;
    private final BVHNode right;

    // Geometries stored in this node (null for internal nodes)
    private final List<Intersectable> geometries;

    // Maximum number of geometries allowed in a leaf node before splitting
    private static final int MAX_GEOMETRIES_IN_LEAF = 4;

    /**
     * Constructor for internal nodes with left and right children.
     * @param left The left child node.
     * @param right The right child node.
     */
    public BVHNode(BVHNode left, BVHNode right) {
        this.left = left;
        this.right = right;
        this.geometries = null;
        // Combine bounding boxes of children
        this.box = AABB.combine(left.box, right.box);
    }

    /**
     * Constructor for leaf nodes containing geometries.
     * @param geometries The list of geometries in this leaf.
     */
    public BVHNode(List<Intersectable> geometries) {
        this.geometries = geometries;
        this.left = null;
        this.right = null;
        // Create bounding box that contains all geometries
        this.box = AABB.combineAll(geometries);
    }

    /**
     * Builds a BVH tree from a list of geometries using recursive subdivision.
     * @param geometries The list of geometries to organize into a BVH tree.
     * @return The root node of the constructed BVH tree.
     */
    public static BVHNode buildBVHTree(List<Intersectable> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        // וידוא חישוב תיבות גבול עבור כל גיאומטריה:
        for (Intersectable g : geometries) {
            g.computeBoundingBoxIfNeeded();
            if (g.getBoundingBox() == null) {
                throw new IllegalStateException("Geometry without bounding box detected in BVH build");
            }
        }

        // אם יש מעט גיאומטריות, יוצרים עלה
        if (geometries.size() <= MAX_GEOMETRIES_IN_LEAF) {
            return new BVHNode(geometries);
        }

        // מציאת הציר הארוך ביותר לחיתוך
        AABB globalBox = AABB.combineAll(geometries);
        Vector size = globalBox.getSize();
        int axis = getMaxAxisIndex(size);

        // מיון הגיאומטריות לפי מרכז תיבת הגבול בציר שנבחר
        geometries.sort(Comparator.comparingDouble(
                g -> getCoordinate(g.getBoundingBox().getCenter(), axis)
        ));

        // חלוקה לשני חלקים
        int mid = geometries.size() / 2;
        List<Intersectable> leftList = new ArrayList<>(geometries.subList(0, mid));
        List<Intersectable> rightList = new ArrayList<>(geometries.subList(mid, geometries.size()));

        // קריאה רקורסיבית לבניית תת-העצים
        BVHNode leftChild = buildBVHTree(leftList);
        BVHNode rightChild = buildBVHTree(rightList);

        return new BVHNode(leftChild, rightChild);
    }

    private static int getMaxAxisIndex(Vector size) {
        double x = Math.abs(size.getX());
        double y = Math.abs(size.getY());
        double z = Math.abs(size.getZ());

        if (x >= y && x >= z) return 0;
        if (y >= z) return 1;
        return 2;
    }

    private static double getCoordinate(Point point, int axis) {
        switch (axis) {
            case 0: return point.getX();
            case 1: return point.getY();
            case 2: return point.getZ();
            default: throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }

    @Override
    protected AABB createBoundingBox() {
        return box;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        if (box != null && !box.intersects(ray)) {
            return null;
        }

        if (isLeaf()) {
            List<Intersection> result = null;
            for (Intersectable geometry : geometries) {
                if (geometry.getBoundingBox() != null &&
                        !geometry.getBoundingBox().intersects(ray)) {
                    continue;
                }
                List<Intersection> intersections = geometry.calculateIntersections(ray);
                if (intersections != null && !intersections.isEmpty()) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.addAll(intersections);
                }
            }
            return result;
        }

        List<Intersection> result = null;
        if (left != null) {
            List<Intersection> leftIntersections = left.calculateIntersectionsHelper(ray);
            if (leftIntersections != null && !leftIntersections.isEmpty()) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.addAll(leftIntersections);
            }
        }
        if (right != null) {
            List<Intersection> rightIntersections = right.calculateIntersectionsHelper(ray);
            if (rightIntersections != null && !rightIntersections.isEmpty()) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.addAll(rightIntersections);
            }
        }
        return result;
    }

    private boolean isLeaf() {
        return geometries != null;
    }

    public Intersection findClosestIntersection(Ray ray) {
        return findClosestIntersectionOptimal(ray, Double.POSITIVE_INFINITY);
    }

    private Intersection findClosestIntersectionOptimal(Ray ray, double maxDistanceSquared) {
        if (box != null && !box.intersects(ray)) {
            return null;
        }

        if (isLeaf()) {
            Intersection closest = null;
            double closestDistanceSquared = maxDistanceSquared;

            for (Intersectable geometry : geometries) {
                if (geometry.getBoundingBox() != null &&
                        !geometry.getBoundingBox().intersects(ray)) {
                    continue;
                }
                List<Intersection> intersections = geometry.calculateIntersections(ray);
                if (intersections != null) {
                    for (Intersection intersection : intersections) {
                        double distanceSquared = intersection.getPoint().distanceSquared(ray.getHead());
                        if (distanceSquared < closestDistanceSquared) {
                            closest = intersection;
                            closestDistanceSquared = distanceSquared;
                        }
                    }
                }
            }
            return closest;
        }

        Intersection closest = null;
        double closestDistanceSquared = maxDistanceSquared;

        BVHNode firstChild, secondChild;
        if (shouldVisitLeftFirst(ray)) {
            firstChild = left;
            secondChild = right;
        } else {
            firstChild = right;
            secondChild = left;
        }

        if (firstChild != null) {
            Intersection firstClosest = firstChild.findClosestIntersectionOptimal(ray, closestDistanceSquared);
            if (firstClosest != null) {
                double distanceSquared = firstClosest.getPoint().distanceSquared(ray.getHead());
                if (distanceSquared < closestDistanceSquared) {
                    closest = firstClosest;
                    closestDistanceSquared = distanceSquared;
                }
            }
        }

        if (secondChild != null) {
            Intersection secondClosest = secondChild.findClosestIntersectionOptimal(ray, closestDistanceSquared);
            if (secondClosest != null) {
                double distanceSquared = secondClosest.getPoint().distanceSquared(ray.getHead());
                if (distanceSquared < closestDistanceSquared) {
                    closest = secondClosest;
                }
            }
        }
        return closest;
    }

    private boolean shouldVisitLeftFirst(Ray ray) {
        if (left == null) return false;
        if (right == null) return true;

        Point rayOrigin = ray.getHead();
        Vector rayDir = ray.getDirection();

        Point leftCenter = left.box.getCenter();
        Point rightCenter = right.box.getCenter();

        Vector toLeft = leftCenter.subtract(rayOrigin);
        Vector toRight = rightCenter.subtract(rayOrigin);

        double leftProjection = toLeft.dotProduct(rayDir);
        double rightProjection = toRight.dotProduct(rayDir);

        return leftProjection < rightProjection;
    }
}
