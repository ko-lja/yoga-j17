package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGComputedPaddingTest {

    @Test
    public void testComputedLayoutPadding() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);
        root.setPaddingPercent(YogaEdge.START, 10);

        root.calculateLayout(100, 100, YogaDirection.LTR);

        assertEquals(10, root.getLayoutPadding(YogaEdge.LEFT), 0.0f);
        assertEquals(0, root.getLayoutPadding(YogaEdge.RIGHT), 0.0f);

        root.calculateLayout(100, 100, YogaDirection.RTL);

        assertEquals(0, root.getLayoutPadding(YogaEdge.LEFT), 0.0f);
        assertEquals(10, root.getLayoutPadding(YogaEdge.RIGHT), 0.0f);
    }

    @Test
    public void testPaddingSideOverridesHorizontalAndVertical() {
        YogaEdge[] edges = {
            YogaEdge.TOP,
            YogaEdge.BOTTOM,
            YogaEdge.START,
            YogaEdge.END,
            YogaEdge.LEFT,
            YogaEdge.RIGHT
        };

        for (float edgeValue = 0; edgeValue < 2; edgeValue++) {
            for (YogaEdge edge : edges) {
                YogaEdge horizontalOrVertical = (edge == YogaEdge.TOP || edge == YogaEdge.BOTTOM) 
                    ? YogaEdge.VERTICAL 
                    : YogaEdge.HORIZONTAL;

                YogaNode root = new YogaNode();
                root.setWidth(100);
                root.setHeight(100);
                root.setPadding(horizontalOrVertical, 10);
                root.setPadding(edge, edgeValue);

                root.calculateLayout(100, 100, YogaDirection.LTR);

                assertEquals(edgeValue, root.getLayoutPadding(edge), 0.0f);
            }
        }
    }

    @Test
    public void testPaddingSideOverridesAll() {
        YogaEdge[] edges = {
            YogaEdge.TOP,
            YogaEdge.BOTTOM,
            YogaEdge.START,
            YogaEdge.END,
            YogaEdge.LEFT,
            YogaEdge.RIGHT
        };

        for (float edgeValue = 0; edgeValue < 2; edgeValue++) {
            for (YogaEdge edge : edges) {
                YogaNode root = new YogaNode();
                root.setWidth(100);
                root.setHeight(100);
                root.setPadding(YogaEdge.ALL, 10);
                root.setPadding(edge, edgeValue);

                root.calculateLayout(100, 100, YogaDirection.LTR);

                assertEquals(edgeValue, root.getLayoutPadding(edge), 0.0f);
            }
        }
    }

    @Test
    public void testPaddingHorizontalAndVerticalOverridesAll() {
        YogaEdge[] directions = {YogaEdge.HORIZONTAL, YogaEdge.VERTICAL};

        for (float directionValue = 0; directionValue < 2; directionValue++) {
            for (YogaEdge direction : directions) {
                YogaNode root = new YogaNode();
                root.setWidth(100);
                root.setHeight(100);
                root.setPadding(YogaEdge.ALL, 10);
                root.setPadding(direction, directionValue);

                root.calculateLayout(100, 100, YogaDirection.LTR);

                if (direction == YogaEdge.VERTICAL) {
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.TOP), 0.0f);
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.BOTTOM), 0.0f);
                } else {
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.START), 0.0f);
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.END), 0.0f);
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.LEFT), 0.0f);
                    assertEquals(directionValue, root.getLayoutPadding(YogaEdge.RIGHT), 0.0f);
                }
            }
        }
    }
}