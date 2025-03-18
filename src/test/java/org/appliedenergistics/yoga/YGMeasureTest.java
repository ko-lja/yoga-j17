package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGMeasureTest {

    private static YogaSize measure(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        int[] measureCount = (int[]) node.getData();
        if (measureCount != null) {
            measureCount[0]++;
        }

        return new YogaSize(10, 10);
    }

    private static YogaSize simulateWrappingText(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        if (widthMode == YogaMeasureMode.UNDEFINED || width >= 68) {
            return new YogaSize(68, 16);
        }

        return new YogaSize(50, 32);
    }

    private static YogaSize measureAssertNegative(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        assertTrue(width >= 0);
        assertTrue(height >= 0);

        return new YogaSize(0, 0);
    }

    @Test
    public void testDontMeasureSingleGrowShrinkChild() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setFlexGrow(1);
        rootChild0.setFlexShrink(1);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, measureCount[0]);
    }

    @Test
    public void testMeasureAbsoluteChildWithNoConstraints() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        root.addChildAt(rootChild0, 0);

        int[] measureCount = {0};

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child0.setData(measureCount);
        rootChild0Child0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.addChildAt(rootChild0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testDontMeasureWhenMinEqualsMax() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setMinWidth(10);
        rootChild0.setMaxWidth(10);
        rootChild0.setMinHeight(10);
        rootChild0.setMaxHeight(10);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, measureCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(10, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testDontMeasureWhenMinEqualsMaxPercentages() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setMinWidthPercent(10);
        rootChild0.setMaxWidthPercent(10);
        rootChild0.setMinHeightPercent(10);
        rootChild0.setMaxHeightPercent(10);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, measureCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(10, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMeasureNodesWithMarginAutoAndStretch() {
        YogaNode root = new YogaNode();
        root.setWidth(500);
        root.setHeight(500);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setMarginAuto(YogaEdge.LEFT);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(490, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(10, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testDontMeasureWhenMinEqualsMaxMixedWidthPercent() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setMinWidthPercent(10);
        rootChild0.setMaxWidthPercent(10);
        rootChild0.setMinHeight(10);
        rootChild0.setMaxHeight(10);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, measureCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(10, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testDontMeasureWhenMinEqualsMaxMixedHeightPercent() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        rootChild0.setMinWidth(10);
        rootChild0.setMaxWidth(10);
        rootChild0.setMinHeightPercent(10);
        rootChild0.setMaxHeightPercent(10);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, measureCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(10, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMeasureEnoughSizeShouldBeInSingleLine() {
        YogaNode root = new YogaNode();
        root.setWidth(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setAlignSelf(YogaAlign.FLEX_START);
        rootChild0.setMeasureFunction(YGMeasureTest::simulateWrappingText);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(68, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(16, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMeasureNotEnoughSizeShouldWrap() {
        YogaNode root = new YogaNode();
        root.setWidth(55);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setAlignSelf(YogaAlign.FLEX_START);
        rootChild0.setMeasureFunction(YGMeasureTest::simulateWrappingText);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(50, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(32, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMeasureZeroSpaceShouldGrow() {
        YogaNode root = new YogaNode();
        root.setHeight(200);
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setFlexGrow(0);

        int[] measureCount = {0};

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexDirection(YogaFlexDirection.COLUMN);
        rootChild0.setPadding(YogaEdge.ALL, 100);
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(282, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(282, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
    }

    @Test
    public void testMeasureFlexDirectionRowAndPadding() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setPadding(YogaEdge.LEFT, 25);
        root.setPadding(YogaEdge.TOP, 25);
        root.setPadding(YogaEdge.RIGHT, 25);
        root.setPadding(YogaEdge.BOTTOM, 25);
        root.setWidth(50);
        root.setHeight(50);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setMeasureFunction(YGMeasureTest::simulateWrappingText);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setWidth(5);
        rootChild1.setHeight(5);
        root.addChildAt(rootChild1, 1);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(50, root.getLayoutWidth(), 0.0f);
        assertEquals(50, root.getLayoutHeight(), 0.0f);

        assertEquals(25, rootChild0.getLayoutX(), 0.0f);
        assertEquals(25, rootChild0.getLayoutY(), 0.0f);
        assertEquals(50, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(0, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(75, rootChild1.getLayoutX(), 0.0f);
        assertEquals(25, rootChild1.getLayoutY(), 0.0f);
        assertEquals(5, rootChild1.getLayoutWidth(), 0.0f);
        assertEquals(5, rootChild1.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMeasureFlexDirectionColumnAndPadding() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setMargin(YogaEdge.TOP, 20);
        root.setPadding(YogaEdge.ALL, 25);
        root.setWidth(50);
        root.setHeight(50);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setMeasureFunction(YGMeasureTest::simulateWrappingText);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setWidth(5);
        rootChild1.setHeight(5);
        root.addChildAt(rootChild1, 1);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(20, root.getLayoutY(), 0.0f);
        assertEquals(50, root.getLayoutWidth(), 0.0f);
        assertEquals(50, root.getLayoutHeight(), 0.0f);

        assertEquals(25, rootChild0.getLayoutX(), 0.0f);
        assertEquals(25, rootChild0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(32, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(25, rootChild1.getLayoutX(), 0.0f);
        assertEquals(57, rootChild1.getLayoutY(), 0.0f);
        assertEquals(5, rootChild1.getLayoutWidth(), 0.0f);
        assertEquals(5, rootChild1.getLayoutHeight(), 0.0f);
    }

    private static YogaSize measureHalfWidthHeight(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(0.5f * width, 0.5f * height);
    }

    @Test
    public void measureContentBox() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(200);
        root.setBoxSizing(YogaBoxSizing.CONTENT_BOX);
        root.setPadding(YogaEdge.ALL, 5);
        root.setBorder(YogaEdge.ALL, 10);

        final int[] measureCount = {0}; // Using array to allow modification in lambda

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setMeasureFunction((node, width, widthMode, height, heightMode) -> {
            measureCount[0]++;
            return measureHalfWidthHeight(node, width, widthMode, height, heightMode);
        });
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(130, root.getLayoutWidth(), 0.0f);
        assertEquals(230, root.getLayoutHeight(), 0.0f);

        assertEquals(15, rootChild0.getLayoutX(), 0.0f);
        assertEquals(15, rootChild0.getLayoutY(), 0.0f);
        assertEquals(100, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(100, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void measureBorderBox() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(200);
        root.setBoxSizing(YogaBoxSizing.BORDER_BOX);
        root.setPadding(YogaEdge.ALL, 5);
        root.setBorder(YogaEdge.ALL, 10);
        root.setDebugName("root");

        final int[] measureCount = {0}; // Using array to allow modification in lambda

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setDebugName("rootChild0");
        rootChild0.setMeasureFunction((node, width, widthMode, height, heightMode) -> {
            measureCount[0]++;
            return measureHalfWidthHeight(node, width, widthMode, height, heightMode);
        });
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(100, root.getLayoutWidth(), 0.0f);
        assertEquals(200, root.getLayoutHeight(), 0.0f);

        assertEquals(15, rootChild0.getLayoutX(), 0.0f);
        assertEquals(15, rootChild0.getLayoutY(), 0.0f);
        assertEquals(70, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(85, rootChild0.getLayoutHeight(), 0.0f);
    }
}