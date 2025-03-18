package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGRoundingMeasureFuncTest {

    private static YogaSize measureFloor(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(10.2f, 10.2f);
    }

    private static YogaSize measureCeil(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(10.5f, 10.5f);
    }

    private static YogaSize measureFractial(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(0.5f, 0.5f);
    }

    @Test
    public void testRoundingFeatureWithCustomMeasureFuncFloor() {
        MutableYogaConfig config = YogaConfig.create();
        YogaNode root = new YogaNode(config);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setMeasureFunction(YGRoundingMeasureFuncTest::measureFloor);
        root.addChildAt(rootChild0, 0);

        config.setPointScaleFactor(0.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(10.2f, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10.2f, rootChild0.getLayoutHeight(), 0.0f);

        config.setPointScaleFactor(1.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(11, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(11, rootChild0.getLayoutHeight(), 0.0f);

        config.setPointScaleFactor(2.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(10.5f, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10.5f, rootChild0.getLayoutHeight(), 0.0f);

        config.setPointScaleFactor(4.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(10.25f, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(10.25f, rootChild0.getLayoutHeight(), 0.0f);

        config.setPointScaleFactor(1.0f / 3.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(12.0f, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(12.0f, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testRoundingFeatureWithCustomMeasureFuncCeil() {
        MutableYogaConfig config = YogaConfig.create();
        YogaNode root = new YogaNode(config);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setMeasureFunction(YGRoundingMeasureFuncTest::measureCeil);
        root.addChildAt(rootChild0, 0);

        config.setPointScaleFactor(1.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(11, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(11, rootChild0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testRoundingFeatureWithCustomMeasureAndFractialMatchingScale() {
        MutableYogaConfig config = YogaConfig.create();
        YogaNode root = new YogaNode(config);
        root.setPositionType(YogaPositionType.ABSOLUTE);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setPosition(YogaEdge.LEFT, 73.625f);
        rootChild0.setPositionType(YogaPositionType.RELATIVE);
        rootChild0.setMeasureFunction(YGRoundingMeasureFuncTest::measureFractial);
        root.addChildAt(rootChild0, 0);

        config.setPointScaleFactor(2.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0.5f, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(0.5f, rootChild0.getLayoutHeight(), 0.0f);
        assertEquals(73.5f, rootChild0.getLayoutX(), 0.0f);
    }
}