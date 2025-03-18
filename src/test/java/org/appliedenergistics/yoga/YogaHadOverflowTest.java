package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class YogaHadOverflowTest {

    private YogaNode root;
    private MutableYogaConfig config;

    @BeforeEach
    public void setUp() {
        config = YogaConfig.create();
        root = new YogaNode(config);
        root.setWidth(200);
        root.setHeight(100);
        root.setFlexDirection(YogaFlexDirection.COLUMN);
        root.setWrap(YogaWrap.NO_WRAP);
    }

    @Test
    public void childrenOverflowNoWrapAndNoFlexChildren() {
        YogaNode child0 = new YogaNode(config);
        child0.setWidth(80);
        child0.setHeight(40);
        child0.setMargin(YogaEdge.TOP, 10);
        child0.setMargin(YogaEdge.BOTTOM, 15);
        root.addChildAt(child0, 0);
        
        YogaNode child1 = new YogaNode(config);
        child1.setWidth(80);
        child1.setHeight(40);
        child1.setMargin(YogaEdge.BOTTOM, 5);
        root.addChildAt(child1, 1);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertTrue(root.getLayout().hadOverflow());
    }

    @Test
    public void spacingOverflowNoWrapAndNoFlexChildren() {
        YogaNode child0 = new YogaNode(config);
        child0.setWidth(80);
        child0.setHeight(40);
        child0.setMargin(YogaEdge.TOP, 10);
        child0.setMargin(YogaEdge.BOTTOM, 10);
        root.addChildAt(child0, 0);
        
        YogaNode child1 = new YogaNode(config);
        child1.setWidth(80);
        child1.setHeight(40);
        child1.setMargin(YogaEdge.BOTTOM, 5);
        root.addChildAt(child1, 1);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertTrue(root.getLayout().hadOverflow());
    }

    @Test
    public void noOverflowNoWrapAndFlexChildren() {
        YogaNode child0 = new YogaNode(config);
        child0.setWidth(80);
        child0.setHeight(40);
        child0.setMargin(YogaEdge.TOP, 10);
        child0.setMargin(YogaEdge.BOTTOM, 10);
        root.addChildAt(child0, 0);
        
        YogaNode child1 = new YogaNode(config);
        child1.setWidth(80);
        child1.setHeight(40);
        child1.setMargin(YogaEdge.BOTTOM, 5);
        child1.setFlexShrink(1);
        root.addChildAt(child1, 1);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertFalse(root.getLayout().hadOverflow());
    }

    @Test
    public void hadOverflowGetsResetIfNotLoggerValid() {
        YogaNode child0 = new YogaNode(config);
        child0.setWidth(80);
        child0.setHeight(40);
        child0.setMargin(YogaEdge.TOP, 10);
        child0.setMargin(YogaEdge.BOTTOM, 10);
        root.addChildAt(child0, 0);
        
        YogaNode child1 = new YogaNode(config);
        child1.setWidth(80);
        child1.setHeight(40);
        child1.setMargin(YogaEdge.BOTTOM, 5);
        root.addChildAt(child1, 1);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertTrue(root.getLayout().hadOverflow());

        child1.setFlexShrink(1);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertFalse(root.getLayout().hadOverflow());
    }

    @Test
    public void spacingOverflowInNestedNodes() {
        YogaNode child0 = new YogaNode(config);
        child0.setWidth(80);
        child0.setHeight(40);
        child0.setMargin(YogaEdge.TOP, 10);
        child0.setMargin(YogaEdge.BOTTOM, 10);
        root.addChildAt(child0, 0);
        
        YogaNode child1 = new YogaNode(config);
        child1.setWidth(80);
        child1.setHeight(40);
        root.addChildAt(child1, 1);
        
        YogaNode child1_1 = new YogaNode(config);
        child1_1.setWidth(80);
        child1_1.setHeight(40);
        child1_1.setMargin(YogaEdge.BOTTOM, 5);
        child1.addChildAt(child1_1, 0);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertTrue(root.getLayout().hadOverflow());
    }
}