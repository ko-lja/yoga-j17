package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGStyleTest {

    @Test
    public void testCopyStyleSame() {
        YogaNode node0 = new YogaNode();
        YogaNode node1 = new YogaNode();

        node0.copyStyle(node1);
    }

    @Test
    public void testCopyStyleModified() {
        YogaNode node0 = new YogaNode();
        assertEquals(YogaFlexDirection.COLUMN, node0.getFlexDirection());
        assertTrue(YogaConstants.isUndefined(node0.getMaxHeight()));

        YogaNode node1 = new YogaNode();
        node1.setFlexDirection(YogaFlexDirection.ROW);
        node1.setMaxHeight(10);

        node0.copyStyle(node1);
        assertEquals(YogaFlexDirection.ROW, node0.getFlexDirection());
        assertEquals(10, node0.getMaxHeight().value);
    }

    @Test
    public void testCopyStyleModifiedSame() {
        YogaNode node0 = new YogaNode();
        node0.setFlexDirection(YogaFlexDirection.ROW);
        node0.setMaxHeight(10);
        node0.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        YogaNode node1 = new YogaNode();
        node1.setFlexDirection(YogaFlexDirection.ROW);
        node1.setMaxHeight(10);

        node0.copyStyle(node1);
    }

    @Test
    public void testInitialiseFlexShrinkFlexGrow() {
        YogaNode node0 = new YogaNode();
        node0.setFlexShrink(1);
        assertEquals(1, node0.getFlexShrink(), 0.0f);

        node0.setFlexShrink(YogaConstants.UNDEFINED);
        node0.setFlexGrow(3);
        assertEquals(0, node0.getFlexShrink(), 0.0f); // Default value is Zero, if flex shrink is not defined
        assertEquals(3, node0.getFlexGrow(), 0.0f);

        node0.setFlexGrow(YogaConstants.UNDEFINED);
        node0.setFlexShrink(3);
        assertEquals(0, node0.getFlexGrow(), 0.0f); // Default value is Zero, if flex grow is not defined
        assertEquals(3, node0.getFlexShrink(), 0.0f);
    }
}