package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.appliedenergistics.yoga.config.MutableYogaConfig;


public class FlexGapTest {

    // TODO: move this to a fixture based test once it supports parsing negative values
    @Test
    public void testGapNegativeValue() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setGap(YogaGutter.ALL, -20);
        root.setHeight(200);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setWidth(20);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setWidth(20);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild2 = new YogaNode(config);
        rootChild2.setWidth(20);
        root.addChildAt(rootChild2, 2);

        YogaNode rootChild3 = new YogaNode(config);
        rootChild3.setWidth(20);
        root.addChildAt(rootChild3, 3);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0);
        assertEquals(0, root.getLayoutY(), 0);
        assertEquals(80, root.getLayoutWidth(), 0);
        assertEquals(200, root.getLayoutHeight(), 0);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);
        assertEquals(20, rootChild0.getLayoutWidth(), 0);
        assertEquals(200, rootChild0.getLayoutHeight(), 0);

        assertEquals(20, rootChild1.getLayoutX(), 0);
        assertEquals(0, rootChild1.getLayoutY(), 0);
        assertEquals(20, rootChild1.getLayoutWidth(), 0);
        assertEquals(200, rootChild1.getLayoutHeight(), 0);

        assertEquals(40, rootChild2.getLayoutX(), 0);
        assertEquals(0, rootChild2.getLayoutY(), 0);
        assertEquals(20, rootChild2.getLayoutWidth(), 0);
        assertEquals(200, rootChild2.getLayoutHeight(), 0);

        assertEquals(60, rootChild3.getLayoutX(), 0);
        assertEquals(0, rootChild3.getLayoutY(), 0);
        assertEquals(20, rootChild3.getLayoutWidth(), 0);
        assertEquals(200, rootChild3.getLayoutHeight(), 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.RTL);

        assertEquals(0, root.getLayoutX(), 0);
        assertEquals(0, root.getLayoutY(), 0);
        assertEquals(80, root.getLayoutWidth(), 0);
        assertEquals(200, root.getLayoutHeight(), 0);

        assertEquals(60, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);
        assertEquals(20, rootChild0.getLayoutWidth(), 0);
        assertEquals(200, rootChild0.getLayoutHeight(), 0);

        assertEquals(40, rootChild1.getLayoutX(), 0);
        assertEquals(0, rootChild1.getLayoutY(), 0);
        assertEquals(20, rootChild1.getLayoutWidth(), 0);
        assertEquals(200, rootChild1.getLayoutHeight(), 0);

        assertEquals(20, rootChild2.getLayoutX(), 0);
        assertEquals(0, rootChild2.getLayoutY(), 0);
        assertEquals(20, rootChild2.getLayoutWidth(), 0);
        assertEquals(200, rootChild2.getLayoutHeight(), 0);

        assertEquals(0, rootChild3.getLayoutX(), 0);
        assertEquals(0, rootChild3.getLayoutY(), 0);
        assertEquals(20, rootChild3.getLayoutWidth(), 0);
        assertEquals(200, rootChild3.getLayoutHeight(), 0);
    }
}