package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGScaleChangeTest {

    @Test
    public void testScaleChangeInvalidatesLayout() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        config.setPointScaleFactor(1.0f);

        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(50);
        root.setHeight(50);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setFlexGrow(1);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setFlexGrow(1);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(25, rootChild1.getLayoutX(), 0.0f);

        config.setPointScaleFactor(1.5f);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        // Left should change due to pixel alignment of new scale factor
        assertEquals(25.333334f, rootChild1.getLayoutX(), 0.0001f);
    }

    @Test
    public void testErrataConfigChangeRelayout() {
        MutableYogaConfig config = YogaConfig.create();
        config.setErrata(YogaErrata.STRETCH_FLEX_BASIS, true);
        YogaNode root = new YogaNode(config);
        root.setWidth(500);
        root.setHeight(500);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setAlignItems(YogaAlign.FLEX_START);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode(config);
        rootChild0Child0.setFlexGrow(1);
        rootChild0Child0.setFlexShrink(1);
        rootChild0.addChildAt(rootChild0Child0, 0);

        YogaNode rootChild0Child0Child0 = new YogaNode(config);
        rootChild0Child0Child0.setFlexGrow(1);
        rootChild0Child0Child0.setFlexShrink(1);
        rootChild0Child0.addChildAt(rootChild0Child0Child0, 0);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(500, root.getLayoutWidth(), 0.0f);
        assertEquals(500, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(500, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0Child0Child0.getLayoutHeight(), 0.0f);

        config.setErrata(YogaErrata.STRETCH_FLEX_BASIS, false);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(500, root.getLayoutWidth(), 0.0f);
        assertEquals(500, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(500, rootChild0.getLayoutWidth(), 0.0f);
        // This should be modified by the lack of the errata
        assertEquals(0, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutWidth(), 0.0f);
        // This should be modified by the lack of the errata
        assertEquals(0, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        // This should be modified by the lack of the errata
        assertEquals(0, rootChild0Child0Child0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testSettingCompatibleConfigMaintainsLayoutCache() {
        final int[] measureCallCount = {0};
        YogaMeasureFunction measureCustom = (node, width, widthMode, height, heightMode) -> {
            measureCallCount[0]++;
            return new YogaSize(25.0f, 25.0f);
        };

        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        config.setPointScaleFactor(1.0f);

        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(50);
        root.setHeight(50);

        YogaNode rootChild0 = new YogaNode(config);
        assertEquals(0, measureCallCount[0]);

        rootChild0.setMeasureFunction(measureCustom);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setFlexGrow(1);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(1, measureCallCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(25, rootChild1.getLayoutX(), 0.0f);

        MutableYogaConfig config2 = YogaConfig.create();
        // Calling setPointScaleFactor multiple times, ensures that config2
        // gets a different config version that config1
        config2.setPointScaleFactor(1.0f);
        config2.setPointScaleFactor(1.5f);
        config2.setPointScaleFactor(1.0f);

        root.setConfig(config2);
        rootChild0.setConfig(config2);
        rootChild1.setConfig(config2);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        // Measure should not be called again, as layout should have been cached since
        // config is functionally the same as before
        assertEquals(1, measureCallCount[0]);
        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(25, rootChild1.getLayoutX(), 0.0f);
    }
}