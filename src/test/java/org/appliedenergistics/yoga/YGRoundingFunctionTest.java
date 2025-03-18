package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.algorithm.PixelGrid;
import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGRoundingFunctionTest {

    @Test
    public void testRoundingValue() {
        // Test that whole numbers are rounded to whole despite ceil/floor flags
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(6.000001, 2.0, false, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(6.000001, 2.0, true, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(6.000001, 2.0, false, true), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(5.999999, 2.0, false, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(5.999999, 2.0, true, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(5.999999, 2.0, false, true), 0.0f);
        
        // Same tests for negative numbers
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-6.000001, 2.0, false, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-6.000001, 2.0, true, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-6.000001, 2.0, false, true), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-5.999999, 2.0, false, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-5.999999, 2.0, true, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-5.999999, 2.0, false, true), 0.0f);

        // Test that numbers with fraction are rounded correctly accounting for ceil/floor flags
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(6.01, 2.0, false, false), 0.0f);
        assertEquals(6.5, PixelGrid.roundValueToPixelGrid(6.01, 2.0, true, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(6.01, 2.0, false, true), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(5.99, 2.0, false, false), 0.0f);
        assertEquals(6.0, PixelGrid.roundValueToPixelGrid(5.99, 2.0, true, false), 0.0f);
        assertEquals(5.5, PixelGrid.roundValueToPixelGrid(5.99, 2.0, false, true), 0.0f);
        
        // Same tests for negative numbers
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-6.01, 2.0, false, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-6.01, 2.0, true, false), 0.0f);
        assertEquals(-6.5, PixelGrid.roundValueToPixelGrid(-6.01, 2.0, false, true), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-5.99, 2.0, false, false), 0.0f);
        assertEquals(-5.5, PixelGrid.roundValueToPixelGrid(-5.99, 2.0, true, false), 0.0f);
        assertEquals(-6.0, PixelGrid.roundValueToPixelGrid(-5.99, 2.0, false, true), 0.0f);

        // Rounding up/down halfway values is as expected for both positive and negative numbers
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.5, 1.0, false, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.4, 1.0, false, false), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.6, 1.0, false, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.499999, 1.0, false, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.500001, 1.0, false, false), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.5001, 1.0, false, false), 0.0f);

        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.5, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.4, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.6, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.499999, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.500001, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.5001, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.00001, 1.0, true, false), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3, 1.0, true, false), 0.0f);

        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.5, 1.0, false, true), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.4, 1.0, false, true), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.6, 1.0, false, true), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.499999, 1.0, false, true), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.500001, 1.0, false, true), 0.0f);
        assertEquals(-4, PixelGrid.roundValueToPixelGrid(-3.5001, 1.0, false, true), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3.00001, 1.0, false, true), 0.0f);
        assertEquals(-3, PixelGrid.roundValueToPixelGrid(-3, 1.0, false, true), 0.0f);

        // NAN is treated as expected:
        assertTrue(Float.isNaN(PixelGrid.roundValueToPixelGrid(
            Float.NaN, 1.5f, false, false)));
        assertTrue(Float.isNaN(PixelGrid.roundValueToPixelGrid(
            1.5f, Float.NaN, false, false)));
        assertTrue(Float.isNaN(PixelGrid.roundValueToPixelGrid(
            Float.NaN, Float.NaN, false, false)));
    }

    private static YogaSize measureText(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(10, 10);
    }

    @Test
    public void testConsistentRoundingDuringRepeatedLayouts() {
        MutableYogaConfig config = YogaConfig.create();
        config.setPointScaleFactor(2);

        YogaNode root = new YogaNode(config);
        root.setMargin(YogaEdge.TOP, -1.49f);
        root.setWidth(500);
        root.setHeight(500);

        YogaNode node0 = new YogaNode(config);
        root.addChildAt(node0, 0);

        YogaNode node1 = new YogaNode(config);
        node1.setMeasureFunction(YGRoundingFunctionTest::measureText);
        node0.addChildAt(node1, 0);

        for (int i = 0; i < 5; i++) {
            // Dirty the tree so roundToPixelGrid runs again
            root.setMargin(YogaEdge.LEFT, (float)(i + 1));

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
            assertEquals(10, node1.getLayoutHeight(), 0.0f);
        }
    }

    @Test
    public void testPerNodePointScaleFactor() {
        MutableYogaConfig config1 = YogaConfig.create();
        config1.setPointScaleFactor(2);

        MutableYogaConfig config2 = YogaConfig.create();
        config2.setPointScaleFactor(1);

        MutableYogaConfig config3 = YogaConfig.create();
        config3.setPointScaleFactor(0.5f);

        YogaNode root = new YogaNode(config1);
        root.setWidth(11.5f);
        root.setHeight(11.5f);

        YogaNode node0 = new YogaNode(config2);
        node0.setWidth(9.5f);
        node0.setHeight(9.5f);
        root.addChildAt(node0, 0);

        YogaNode node1 = new YogaNode(config3);
        node1.setWidth(7);
        node1.setHeight(7);
        node0.addChildAt(node1, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(11.5f, root.getLayoutWidth(), 0.0f);
        assertEquals(11.5f, root.getLayoutHeight(), 0.0f);

        assertEquals(10, node0.getLayoutWidth(), 0.0f);
        assertEquals(10, node0.getLayoutHeight(), 0.0f);

        assertEquals(8, node1.getLayoutWidth(), 0.0f);
        assertEquals(8, node1.getLayoutHeight(), 0.0f);
    }
}