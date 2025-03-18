package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YGMeasureModeTest {

    private static class MeasureConstraint {
        float width;
        YogaMeasureMode widthMode;
        float height;
        YogaMeasureMode heightMode;
    }

    private static class MeasureConstraintList {
        int length;
        MeasureConstraint[] constraints;
    }

    private static YogaSize measure(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        MeasureConstraintList constraintList = (MeasureConstraintList) node.getData();
        MeasureConstraint[] constraints = constraintList.constraints;
        int currentIndex = constraintList.length;
        constraints[currentIndex] = new MeasureConstraint();
        constraints[currentIndex].width = width;
        constraints[currentIndex].widthMode = widthMode;
        constraints[currentIndex].height = height;
        constraints[currentIndex].heightMode = heightMode;
        constraintList.length = currentIndex + 1;

        return new YogaSize(
                widthMode == YogaMeasureMode.UNDEFINED ? 10 : width,
                heightMode == YogaMeasureMode.UNDEFINED ? 10 : width
        );
    }

    @Test
    public void testExactlyMeasureStretchedChildColumn() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].width, 0.0f);
        assertEquals(YogaMeasureMode.EXACTLY, constraintList.constraints[0].widthMode);
    }

    @Test
    public void testExactlyMeasureStretchedChildRow() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.EXACTLY, constraintList.constraints[0].heightMode);
    }

    @Test
    public void testAtMostMainAxisColumn() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].heightMode);
    }

    @Test
    public void testAtMostCrossAxisColumn() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].width, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].widthMode);
    }

    @Test
    public void testAtMostMainAxisRow() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].width, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].widthMode);
    }

    @Test
    public void testAtMostCrossAxisRow() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].heightMode);
    }

    @Test
    public void testFlexChild() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(2, constraintList.length);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].heightMode);
        assertEquals(100, constraintList.constraints[1].height, 0.0f);
        assertEquals(YogaMeasureMode.EXACTLY, constraintList.constraints[1].heightMode);
    }

    @Test
    public void testFlexChildWithFlexBasis() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setFlexBasis(0);
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.EXACTLY, constraintList.constraints[0].heightMode);
    }

    @Test
    public void testOverflowScrollColumn() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setOverflow(YogaOverflow.SCROLL);
        root.setHeight(100);
        root.setWidth(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(100, constraintList.constraints[0].width, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].widthMode);
        assertEquals(YogaConstants.UNDEFINED, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.UNDEFINED, constraintList.constraints[0].heightMode);
    }

    @Test
    public void testOverflowScrollRow() {
        MeasureConstraintList constraintList = new MeasureConstraintList();
        constraintList.length = 0;
        constraintList.constraints = new MeasureConstraint[10];

        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setOverflow(YogaOverflow.SCROLL);
        root.setHeight(100);
        root.setWidth(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setData(constraintList);
        rootChild0.setMeasureFunction(YGMeasureModeTest::measure);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(1, constraintList.length);
        assertEquals(YogaConstants.UNDEFINED, constraintList.constraints[0].width, 0.0f);
        assertEquals(YogaMeasureMode.UNDEFINED, constraintList.constraints[0].widthMode);
        assertEquals(100, constraintList.constraints[0].height, 0.0f);
        assertEquals(YogaMeasureMode.AT_MOST, constraintList.constraints[0].heightMode);
    }
}