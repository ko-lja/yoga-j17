package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGBaselineFuncTest {

    private static float baseline(YogaNode node, float width, float height) {
        float[] baseline = (float[]) node.getData();
        return baseline[0];
    }

    @Test
    public void testAlignBaselineCustomerFunc() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.BASELINE);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setWidth(50);
        rootChild0.setHeight(50);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode();
        rootChild1.setWidth(50);
        rootChild1.setHeight(20);
        root.addChildAt(rootChild1, 1);

        float[] baselineValue = {10};
        YogaNode rootChild1Child0 = new YogaNode();
        rootChild1Child0.setData(baselineValue);
        rootChild1Child0.setWidth(50);
        rootChild1Child0.setBaselineFunction(YGBaselineFuncTest::baseline);
        rootChild1Child0.setHeight(20);
        rootChild1.addChildAt(rootChild1Child0, 0);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(100, root.getLayoutWidth(), 0.0f);
        assertEquals(100, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(50, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(50, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(50, rootChild1.getLayoutX(), 0.0f);
        assertEquals(40, rootChild1.getLayoutY(), 0.0f);
        assertEquals(50, rootChild1.getLayoutWidth(), 0.0f);
        assertEquals(20, rootChild1.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild1Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild1Child0.getLayoutY(), 0.0f);
        assertEquals(50, rootChild1Child0.getLayoutWidth(), 0.0f);
        assertEquals(20, rootChild1Child0.getLayoutHeight(), 0.0f);
    }
}