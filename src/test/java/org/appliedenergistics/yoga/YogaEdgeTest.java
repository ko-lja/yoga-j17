package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YogaEdgeTest {

    @Test
    public void startOverrides() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.START, 10);
        rootChild0.setMargin(YogaEdge.LEFT, 20);
        rootChild0.setMargin(YogaEdge.RIGHT, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
    }

    @Test
    public void endOverrides() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.END, 10);
        rootChild0.setMargin(YogaEdge.LEFT, 20);
        rootChild0.setMargin(YogaEdge.RIGHT, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
    }

    @Test
    public void horizontalOverridden() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.HORIZONTAL, 10);
        rootChild0.setMargin(YogaEdge.LEFT, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
    }

    @Test
    public void verticalOverridden() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.VERTICAL, 10);
        rootChild0.setMargin(YogaEdge.TOP, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.BOTTOM), 0.0f);
    }

    @Test
    public void horizontalOverridesAll() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.HORIZONTAL, 10);
        rootChild0.setMargin(YogaEdge.ALL, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.BOTTOM), 0.0f);
    }

    @Test
    public void verticalOverridesAll() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.VERTICAL, 10);
        rootChild0.setMargin(YogaEdge.ALL, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(20, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.BOTTOM), 0.0f);
    }

    @Test
    public void allOverridden() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setFlexGrow(1);
        rootChild0.setMargin(YogaEdge.LEFT, 10);
        rootChild0.setMargin(YogaEdge.TOP, 10);
        rootChild0.setMargin(YogaEdge.RIGHT, 10);
        rootChild0.setMargin(YogaEdge.BOTTOM, 10);
        rootChild0.setMargin(YogaEdge.ALL, 20);
        root.addChildAt(rootChild0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
        assertEquals(10, rootChild0.getLayoutMargin(YogaEdge.BOTTOM), 0.0f);
    }
}