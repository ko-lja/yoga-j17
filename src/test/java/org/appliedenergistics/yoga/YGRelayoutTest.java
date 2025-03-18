package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGRelayoutTest {

    @Test
    public void testDontCacheComputedFlexBasisBetweenLayouts() {
        MutableYogaConfig config = YogaConfig.create();
        config.setExperimentalFeatureEnabled(YogaExperimentalFeature.WEB_FLEX_BASIS, true);

        YogaNode root = new YogaNode(config);
        root.setHeightPercent(100);
        root.setWidthPercent(100);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setFlexBasisPercent(100);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(100, YogaConstants.UNDEFINED, YogaDirection.LTR);
        root.calculateLayout(100, 100, YogaDirection.LTR);

        assertEquals(100, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testRecalculateResolvedDimensionOnChange() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setMinHeight(10);
        rootChild0.setMaxHeight(10);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);

        rootChild0.setMinHeight(YogaConstants.UNDEFINED);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testRelayoutContainingBlockSizeChanges() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setPositionType(YogaPositionType.ABSOLUTE);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setPositionType(YogaPositionType.RELATIVE);
        rootChild0.setMargin(YogaEdge.LEFT, 4);
        rootChild0.setMargin(YogaEdge.TOP, 5);
        rootChild0.setMargin(YogaEdge.RIGHT, 9);
        rootChild0.setMargin(YogaEdge.BOTTOM, 1);
        rootChild0.setPadding(YogaEdge.LEFT, 2);
        rootChild0.setPadding(YogaEdge.TOP, 9);
        rootChild0.setPadding(YogaEdge.RIGHT, 11);
        rootChild0.setPadding(YogaEdge.BOTTOM, 13);
        rootChild0.setBorder(YogaEdge.LEFT, 5);
        rootChild0.setBorder(YogaEdge.TOP, 6);
        rootChild0.setBorder(YogaEdge.RIGHT, 7);
        rootChild0.setBorder(YogaEdge.BOTTOM, 8);
        rootChild0.setWidth(500);
        rootChild0.setHeight(500);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode(config);
        rootChild0Child0.setPositionType(YogaPositionType.STATIC);
        rootChild0Child0.setMargin(YogaEdge.LEFT, 8);
        rootChild0Child0.setMargin(YogaEdge.TOP, 6);
        rootChild0Child0.setMargin(YogaEdge.RIGHT, 3);
        rootChild0Child0.setMargin(YogaEdge.BOTTOM, 9);
        rootChild0Child0.setPadding(YogaEdge.LEFT, 1);
        rootChild0Child0.setPadding(YogaEdge.TOP, 7);
        rootChild0Child0.setPadding(YogaEdge.RIGHT, 9);
        rootChild0Child0.setPadding(YogaEdge.BOTTOM, 4);
        rootChild0Child0.setBorder(YogaEdge.LEFT, 8);
        rootChild0Child0.setBorder(YogaEdge.TOP, 10);
        rootChild0Child0.setBorder(YogaEdge.RIGHT, 2);
        rootChild0Child0.setBorder(YogaEdge.BOTTOM, 1);
        rootChild0Child0.setWidth(200);
        rootChild0Child0.setHeight(200);
        rootChild0.addChildAt(rootChild0Child0, 0);

        YogaNode rootChild0Child0Child0 = new YogaNode(config);
        rootChild0Child0Child0.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child0Child0.setPosition(YogaEdge.LEFT, 2);
        rootChild0Child0Child0.setPosition(YogaEdge.RIGHT, 12);
        rootChild0Child0Child0.setMargin(YogaEdge.LEFT, 9);
        rootChild0Child0Child0.setMargin(YogaEdge.TOP, 12);
        rootChild0Child0Child0.setMargin(YogaEdge.RIGHT, 4);
        rootChild0Child0Child0.setMargin(YogaEdge.BOTTOM, 7);
        rootChild0Child0Child0.setPadding(YogaEdge.LEFT, 5);
        rootChild0Child0Child0.setPadding(YogaEdge.TOP, 3);
        rootChild0Child0Child0.setPadding(YogaEdge.RIGHT, 8);
        rootChild0Child0Child0.setPadding(YogaEdge.BOTTOM, 10);
        rootChild0Child0Child0.setBorder(YogaEdge.LEFT, 2);
        rootChild0Child0Child0.setBorder(YogaEdge.TOP, 1);
        rootChild0Child0Child0.setBorder(YogaEdge.RIGHT, 5);
        rootChild0Child0Child0.setBorder(YogaEdge.BOTTOM, 9);
        rootChild0Child0Child0.setWidthPercent(41);
        rootChild0Child0Child0.setHeightPercent(63);
        rootChild0Child0.addChildAt(rootChild0Child0Child0, 0);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(513, root.getLayoutWidth(), 0.0f);
        assertEquals(506, root.getLayoutHeight(), 0.0f);

        assertEquals(4, rootChild0.getLayoutX(), 0.0f);
        assertEquals(5, rootChild0.getLayoutY(), 0.0f);
        assertEquals(500, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(15, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(21, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(1, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(29, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(306, rootChild0Child0Child0.getLayoutHeight(), 0.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(513, root.getLayoutWidth(), 0.0f);
        assertEquals(506, root.getLayoutHeight(), 0.0f);

        assertEquals(4, rootChild0.getLayoutX(), 0.0f);
        assertEquals(5, rootChild0.getLayoutY(), 0.0f);
        assertEquals(500, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(279, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(21, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(-2, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(29, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(306, rootChild0Child0Child0.getLayoutHeight(), 0.0f);

        // Relayout starts here
        rootChild0.setWidth(456);
        rootChild0.setHeight(432);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(469, root.getLayoutWidth(), 0.0f);
        assertEquals(438, root.getLayoutHeight(), 0.0f);

        assertEquals(4, rootChild0.getLayoutX(), 0.0f);
        assertEquals(5, rootChild0.getLayoutY(), 0.0f);
        assertEquals(456, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(432, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(15, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(21, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(1, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(29, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(182, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(263, rootChild0Child0Child0.getLayoutHeight(), 0.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(469, root.getLayoutWidth(), 0.0f);
        assertEquals(438, root.getLayoutHeight(), 0.0f);

        assertEquals(4, rootChild0.getLayoutX(), 0.0f);
        assertEquals(5, rootChild0.getLayoutY(), 0.0f);
        assertEquals(456, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(432, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(235, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(21, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(200, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(16, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(29, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(182, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(263, rootChild0Child0Child0.getLayoutHeight(), 0.0f);
    }
    
    @Test
    public void testHasNewLayoutFlagSetStatic() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setPositionType(YogaPositionType.STATIC);
        rootChild0.setWidth(10);
        rootChild0.setHeight(10);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child1 = new YogaNode();
        rootChild0Child1.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child1.setWidth(5);
        rootChild0Child1.setHeight(5);
        rootChild0.addChildAt(rootChild0Child1, 0);

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setPositionType(YogaPositionType.STATIC);
        rootChild0Child0.setWidth(5);
        rootChild0Child0.setHeight(5);
        rootChild0.addChildAt(rootChild0Child0, 1);

        YogaNode rootChild0Child0Child0 = new YogaNode();
        rootChild0Child0Child0.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child0Child0.setWidthPercent(1);
        rootChild0Child0Child0.setHeight(1);
        rootChild0Child0.addChildAt(rootChild0Child0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        root.setHasNewLayout(false);
        rootChild0.setHasNewLayout(false);
        rootChild0Child0.setHasNewLayout(false);
        rootChild0Child0Child0.setHasNewLayout(false);

        root.setWidth(110);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertTrue(root.hasNewLayout());
        assertTrue(rootChild0.hasNewLayout());
        assertTrue(rootChild0Child0.hasNewLayout());
        assertTrue(rootChild0Child0Child0.hasNewLayout());
    }
}