package geometries;

import primitives.Ray;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BoundingVolumeBuilder using a BVH (Bounding Volume Hierarchy) tree.
 * Builds a BVH tree to optimize intersection queries with a ray.
 */
public class BVHBoundingBoxBuilder implements BoundingVolumeBuilder {

    private BVHNode root;

    /**
     * Creates a bounding box that encloses all provided geometries using BVH tree structure.
     * This method builds the BVH tree and returns the root's bounding box.
     * @param geometries The list of geometries to enclose.
     * @return The bounding box of the root BVH node, or null if no geometries.
     */
    @Override
    public AABB createBoundingBox(List<Intersectable> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        // Build the BVH tree from the geometries
        root = BVHNode.buildBVHTree(new ArrayList<>(geometries));

        // Return the bounding box of the root node
        return root != null ? root.getBoundingBox() : null;
    }

    /**
     * Finds intersections between the ray and the list of geometries using the BVH hierarchy.
     * This method leverages the BVH tree for efficient intersection testing by first checking
     * bounding boxes before testing actual geometry intersections.
     * @param ray The ray to test intersections with.
     * @param geometries The list of geometries to test (used to build tree if not already built).
     * @return A list of intersections found between the ray and geometries.
     */
    @Override
    public List<Intersectable.Intersection> findIntersections(Ray ray, List<Intersectable> geometries) {
        // If no BVH tree exists yet, build it first
        if (root == null && geometries != null && !geometries.isEmpty()) {
            createBoundingBox(geometries);
        }

        // If still no root (empty geometries), return null
        if (root == null) {
            return null;
        }

        // Use the BVH tree's efficient intersection method
        return root.calculateIntersections(ray);
    }
}