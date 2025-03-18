package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YGMeasureCacheTest {

    private static YogaSize measureMax(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        int[] measureCount = (int[]) node.getData();
        measureCount[0]++;

        return new YogaSize(
                widthMode == YogaMeasureMode.UNDEFINED ? 10 : width,
                heightMode == YogaMeasureMode.UNDEFINED ? 10 : height
        );
    }

    private static YogaSize measureMin(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        int[] measureCount = (int[]) node.getData();
        measureCount[0]++;

        return new YogaSize(
                widthMode == YogaMeasureMode.UNDEFINED ||
                        (widthMode == YogaMeasureMode.AT_MOST && width > 10)
                        ? 10
                        : width,
                heightMode == YogaMeasureMode.UNDEFINED ||
                        (heightMode == YogaMeasureMode.AT_MOST && height > 10)
                        ? 10
                        : height
        );
    }

    private static YogaSize measure84_49(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        int[] measureCount = (int[]) node.getData();
        if (measureCount != null) {
            measureCount[0]++;
        }

        return new YogaSize(84f, 49f);
    }

    @Test
    public void testMeasureOnceSingleFlexibleChild() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        int[] measureCount = {0};
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureCacheTest::measureMax);
        rootChild0.setFlexGrow(1);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testRemeasureWithSameExactWidthLargerThanNeededHeight() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        int[] measureCount = {0};
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureCacheTest::measureMin);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(100, 100, YogaDirection.LTR);
        root.calculateLayout(100, 50, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testRemeasureWithSameAtMostWidthLargerThanNeededHeight() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);

        YogaNode rootChild0 = new YogaNode();
        int[] measureCount = {0};
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureCacheTest::measureMin);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(100, 100, YogaDirection.LTR);
        root.calculateLayout(100, 50, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testRemeasureWithComputedWidthLargerThanNeededHeight() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);

        YogaNode rootChild0 = new YogaNode();
        int[] measureCount = {0};
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureCacheTest::measureMin);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(100, 100, YogaDirection.LTR);
        root.setAlignItems(YogaAlign.STRETCH);
        root.calculateLayout(10, 50, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testRemeasureWithAtMostComputedWidthUndefinedHeight() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);

        YogaNode rootChild0 = new YogaNode();
        int[] measureCount = {0};
        rootChild0.setData(measureCount);
        rootChild0.setMeasureFunction(YGMeasureCacheTest::measureMin);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(100, YogaConstants.UNDEFINED, YogaDirection.LTR);
        root.calculateLayout(10, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }

    @Test
    public void testRemeasureWithAlreadyMeasuredValueSmallerButStillFloatEqual() {
        int[] measureCount = {0};

        YogaNode root = new YogaNode();
        root.setWidth(288f);
        root.setHeight(288f);
        root.setFlexDirection(YogaFlexDirection.ROW);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setPadding(YogaEdge.ALL, 2.88f);
        rootChild0.setFlexDirection(YogaFlexDirection.ROW);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setData(measureCount);
        rootChild0Child0.setMeasureFunction(YGMeasureCacheTest::measure84_49);
        rootChild0.addChildAt(rootChild0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, measureCount[0]);
    }
}