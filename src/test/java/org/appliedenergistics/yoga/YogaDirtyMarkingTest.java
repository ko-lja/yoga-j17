package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class YogaDirtyMarkingTest {

    @Test
    public void dirtyPropagation() {
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

        rootChild0.setWidth(20);

        assertTrue(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertTrue(root.isDirty());

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(root.isDirty());
    }

    @Test
    public void dirtyPropagationOnlyIfPropChanged() {
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

        rootChild0.setWidth(50);

        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(root.isDirty());
    }

    @Test
    public void dirtyPropagationChangingLayoutConfig() {
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

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setWidth(25);
        rootChild0Child0.setHeight(20);
        root.addChildAt(rootChild0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertFalse(root.isDirty());
        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(rootChild0Child0.isDirty());

        MutableYogaConfig newConfig = YogaConfig.create();
        newConfig.setErrata(YogaErrata.STRETCH_FLEX_BASIS);
        rootChild0.setConfig(newConfig);

        assertTrue(root.isDirty());
        assertTrue(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(rootChild0Child0.isDirty());

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertFalse(root.isDirty());
        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(rootChild0Child0.isDirty());
    }

    @Test
    public void dirtyPropagationChangingBenignConfig() {
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

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setWidth(25);
        rootChild0Child0.setHeight(20);
        root.addChildAt(rootChild0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertFalse(root.isDirty());
        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(rootChild0Child0.isDirty());

        MutableYogaConfig newConfig = YogaConfig.create();
        newConfig.setLogger((config, node, level, format, args) -> {});
        rootChild0.setConfig(newConfig);

        assertFalse(root.isDirty());
        assertFalse(rootChild0.isDirty());
        assertFalse(rootChild1.isDirty());
        assertFalse(rootChild0Child0.isDirty());
    }

    @Test
    public void dirtyMarkAllChildrenAsDirtyWhenDisplayChanges() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setHeight(100);

        YogaNode child0 = new YogaNode();
        child0.setFlexGrow(1);
        YogaNode child1 = new YogaNode();
        child1.setFlexGrow(1);

        YogaNode child1Child0 = new YogaNode();
        YogaNode child1Child0Child0 = new YogaNode();
        child1Child0Child0.setWidth(8);
        child1Child0Child0.setHeight(16);

        child1Child0.addChildAt(child1Child0Child0, 0);

        child1.addChildAt(child1Child0, 0);
        root.addChildAt(child0, 0);
        root.addChildAt(child1, 0);

        child0.setDisplay(YogaDisplay.FLEX);
        child1.setDisplay(YogaDisplay.NONE);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(0, child1Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(0, child1Child0Child0.getLayoutHeight(), 0.0f);

        child0.setDisplay(YogaDisplay.NONE);
        child1.setDisplay(YogaDisplay.FLEX);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(8, child1Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(16, child1Child0Child0.getLayoutHeight(), 0.0f);

        child0.setDisplay(YogaDisplay.FLEX);
        child1.setDisplay(YogaDisplay.NONE);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(0, child1Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(0, child1Child0Child0.getLayoutHeight(), 0.0f);

        child0.setDisplay(YogaDisplay.NONE);
        child1.setDisplay(YogaDisplay.FLEX);
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertEquals(8, child1Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(16, child1Child0Child0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void dirtyNodeOnlyIfChildrenAreActuallyRemoved() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(50);
        root.setHeight(50);

        YogaNode child0 = new YogaNode();
        child0.setWidth(50);
        child0.setHeight(25);
        root.addChildAt(child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        YogaNode child1 = new YogaNode();
        root.removeChildAndInvalidate(child1);
        assertFalse(root.isDirty());

        root.removeChildAndInvalidate(child0);
        assertTrue(root.isDirty());
    }

    @Test
    public void dirtyNodeOnlyIfUndefinedValuesGetsSetToUndefined() {
        YogaNode root = new YogaNode();
        root.setWidth(50);
        root.setHeight(50);
        root.setMinWidth(YogaConstants.UNDEFINED);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertFalse(root.isDirty());

        root.setMinWidth(YogaConstants.UNDEFINED);

        assertFalse(root.isDirty());
    }
}