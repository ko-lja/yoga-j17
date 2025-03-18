package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGNodeChildTest {

    @Test
    public void testResetLayoutWhenChildRemoved() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setWidth(100);
        rootChild0.setHeight(100);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(100, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(100, rootChild0.getLayoutHeight(), 0.0f);

        root.removeChildAndInvalidate(rootChild0);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertTrue(YogaConstants.isUndefined(rootChild0.getLayoutWidth()));
        assertTrue(YogaConstants.isUndefined(rootChild0.getLayoutHeight()));
    }
}