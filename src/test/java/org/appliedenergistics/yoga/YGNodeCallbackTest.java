package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGNodeCallbackTest {

    @Test
    public void testHasMeasureFuncInitial() {
        YogaNode node = new YogaNode();
        assertFalse(node.hasMeasureFunc());
    }

    @Test
    public void testHasMeasureFuncWithMeasureFn() {
        YogaNode node = new YogaNode();
        node.setMeasureFunction((yogaNode, width, widthMode, height, heightMode) -> 
            new YogaSize(0f, 0f));
        assertTrue(node.hasMeasureFunc());
    }

    @Test
    public void testMeasureWithMeasureFn() {
        YogaNode node = new YogaNode();
        
        node.setMeasureFunction((yogaNode, width, widthMode, height, heightMode) -> 
            new YogaSize(
                width * widthMode.ordinal(), 
                height / heightMode.ordinal())
        );

        YogaSize size = node.measure(23, YogaMeasureMode.EXACTLY, 24, YogaMeasureMode.AT_MOST);
        assertEquals(23, size.width(), 0.01f);
        assertEquals(12, size.height(), 0.01f);
    }

    @Test
    public void testHasMeasureFuncAfterUnset() {
        YogaNode node = new YogaNode();
        node.setMeasureFunction((yogaNode, width, widthMode, height, heightMode) -> 
            new YogaSize(0f, 0f));
        
        node.setMeasureFunction(null);
        assertFalse(node.hasMeasureFunc());
    }

    @Test
    public void testHasBaselineFuncInitial() {
        YogaNode node = new YogaNode();
        assertFalse(node.isBaselineDefined());
    }

    @Test
    public void testHasBaselineFuncWithBaselineFn() {
        YogaNode node = new YogaNode();
        node.setBaselineFunction((yogaNode, width, height) -> 0.0f);
        assertTrue(node.isBaselineDefined());
    }

    @Test
    public void testBaselineWithBaselineFn() {
        YogaNode node = new YogaNode();
        node.setBaselineFunction((yogaNode, width, height) -> width + height);
        
        assertEquals(3.75f, node.baseline(1.25f, 2.5f), 0.001f);
    }

    @Test
    public void testHasBaselineFuncAfterUnset() {
        YogaNode node = new YogaNode();
        node.setBaselineFunction((yogaNode, width, height) -> 0.0f);
        
        node.setBaselineFunction(null);
        assertFalse(node.isBaselineDefined());
    }

    @Test
    public void testBaseline() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.BASELINE);
        root.setWidth(100);
        root.setHeight(100);
        root.setData("root");

        YogaNode child1 = new YogaNode();
        child1.setWidth(40);
        child1.setHeight(40);
        child1.setData("child1");
        root.addChildAt(child1, 0);

        YogaNode child2 = new YogaNode();
        child2.setWidth(40);
        child2.setHeight(40);
        child2.setData("child2");
        child2.setBaselineFunction((node, width, height) -> 0.0f);
        root.addChildAt(child2, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, (int) child1.getLayoutY());
        assertEquals(40, (int) child2.getLayoutY());
    }
}