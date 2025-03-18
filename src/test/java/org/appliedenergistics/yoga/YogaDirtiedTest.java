package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YogaDirtiedTest {

    @Test
    public void dirtied() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        AtomicInteger dirtiedCount = new AtomicInteger(0);
        root.setDirtiedFunc(node -> dirtiedCount.incrementAndGet());

        assertEquals(0, dirtiedCount.get());

        // `dirtiedListener` MUST be called in case of explicit dirtying
        root.setDirty(true);
        assertEquals(1, dirtiedCount.get());

        // `dirtiedListener` MUST be called ONCE
        root.setDirty(true);
        assertEquals(1, dirtiedCount.get());
    }

    @Test
    public void dirtiedPropagation() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setWidth(50);
        rootChild0.setHeight(20);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode();
        rootChild1.setWidth(50);
        rootChild1.setHeight(20);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        AtomicInteger dirtiedCount = new AtomicInteger(0);
        root.setDirtiedFunc(node -> dirtiedCount.incrementAndGet());

        assertEquals(0, dirtiedCount.get());

        // `dirtiedListener` MUST be called for the first time
        rootChild0.markDirtyAndPropagate();
        assertEquals(1, dirtiedCount.get());

        // `dirtiedListener` must NOT be called for the second time
        rootChild0.markDirtyAndPropagate();
        assertEquals(1, dirtiedCount.get());
    }

    @Test
    public void dirtiedHierarchy() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setWidth(50);
        rootChild0.setHeight(20);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode();
        rootChild1.setWidth(50);
        rootChild1.setHeight(20);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        AtomicInteger dirtiedCount = new AtomicInteger(0);
        rootChild0.setDirtiedFunc(node -> dirtiedCount.incrementAndGet());

        assertEquals(0, dirtiedCount.get());

        // `dirtiedListener` must NOT be called for descendants
        root.markDirtyAndPropagate();
        assertEquals(0, dirtiedCount.get());

        // `dirtiedListener` must NOT be called for the sibling node
        rootChild1.markDirtyAndPropagate();
        assertEquals(0, dirtiedCount.get());

        // `dirtiedListener` MUST be called in case of explicit dirtying
        rootChild0.markDirtyAndPropagate();
        assertEquals(1, dirtiedCount.get());
    }
}