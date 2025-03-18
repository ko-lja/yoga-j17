package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGZeroOutLayoutRecursivelyTest {

    @Test
    public void testZeroOutLayout() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(200);
        root.setHeight(200);

        YogaNode child = new YogaNode();
        root.addChildAt(child, 0);
        child.setWidth(100);
        child.setHeight(100);
        child.setMargin(YogaEdge.TOP, 10);
        child.setPadding(YogaEdge.TOP, 10);

        root.calculateLayout(100, 100, YogaDirection.LTR);

        assertEquals(10, child.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(10, child.getLayoutPadding(YogaEdge.TOP), 0.0f);

        child.setDisplay(YogaDisplay.NONE);

        root.calculateLayout(100, 100, YogaDirection.LTR);

        assertEquals(0, child.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(0, child.getLayoutPadding(YogaEdge.TOP), 0.0f);
    }
}