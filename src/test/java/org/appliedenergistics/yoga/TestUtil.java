package org.appliedenergistics.yoga;

/**
 * Utility class for testing Yoga nodes
 */
public class TestUtil {
    
    private static int nodeCount = 0;
    private static boolean isCountingNodes = false;
    
    /**
     * Start counting Yoga nodes
     */
    public static void startCountingNodes() {
        nodeCount = 0;
        isCountingNodes = true;
    }
    
    /**
     * Stop counting Yoga nodes and return the count
     * 
     * @return The number of nodes counted since startCountingNodes was called
     */
    public static int stopCountingNodes() {
        isCountingNodes = false;
        return nodeCount;
    }
    
    /**
     * Increment the node count
     */
    public static void incrementNodeCount() {
        if (isCountingNodes) {
            nodeCount++;
        }
    }
    
    /**
     * Decrement the node count
     */
    public static void decrementNodeCount() {
        if (isCountingNodes) {
            nodeCount--;
        }
    }
}